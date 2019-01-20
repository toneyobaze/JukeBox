package com.oladapo.jukebox;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;

import com.oladapo.jukebox.Fragments.albumsFragment;
import com.oladapo.jukebox.Fragments.artistsFragment;
import com.oladapo.jukebox.Fragments.playlistsFragment;
import com.oladapo.jukebox.Fragments.songsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    public MainActivity() {
    }

    ImageButton play, pause, play_main, pause_main,
            shuffle_on, shuffle_off, repeat_on, repeat_off, next, previous;

    TextView current_song;
    TextView current_artist;

    private TabLayout tabLayout;
    private int[] tabIcons = {

            R.drawable.musical_note,
            R.drawable.album,
            R.drawable.singer,
            R.drawable.playlist
    };

    private MusicService musicSrv;
    private boolean musicBound;
    private Intent playIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons();

        play = findViewById(R.id.toolbar_play_button);
        pause = findViewById(R.id.toolbar_pause_button);
        play_main = findViewById(R.id.play_button_main);
        pause_main = findViewById(R.id.pause_button_main);
        shuffle_on = findViewById(R.id.shuffle_on);
        shuffle_off = findViewById(R.id.shuffle_off);
        repeat_on = findViewById(R.id.repeat_on);
        repeat_off = findViewById(R.id.repeat_off);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        current_song = findViewById(R.id.song_title);
        current_artist = findViewById(R.id.songs_artist_name);

        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);

                start();

                if (play.getVisibility() == View.VISIBLE) {
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);

                if (pause.getVisibility() == View.VISIBLE) {
                    pause.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                }

            }

        });

        play_main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                play_main.setVisibility(View.GONE);
                pause_main.setVisibility(View.VISIBLE);

                if (play_main.getVisibility() == View.VISIBLE) {
                    play_main.setVisibility(View.GONE);
                    pause_main.setVisibility(View.VISIBLE);
                }

            }

        });

        pause_main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pause_main.setVisibility(View.GONE);
                play_main.setVisibility(View.VISIBLE);

                if (pause_main.getVisibility() == View.VISIBLE) {
                    pause_main.setVisibility(View.GONE);
                    play_main.setVisibility(View.VISIBLE);
                }
            }
        });

        shuffle_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffle_off.setVisibility(View.GONE);
                shuffle_on.setVisibility(View.VISIBLE);

                musicSrv.shuffleOn = false;
                musicSrv.setShuffle();

                if (shuffle_off.getVisibility() == View.GONE) {
                    shuffle_off.setVisibility(View.GONE);
                    shuffle_on.setVisibility(View.VISIBLE);
                }
            }
        });

        shuffle_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffle_on.setVisibility(View.GONE);
                shuffle_off.setVisibility(View.VISIBLE);

                musicSrv.shuffleOn = true;
                musicSrv.setShuffle();

                if (shuffle_on.getVisibility() == View.GONE) {
                    shuffle_on.setVisibility(View.GONE);
                    shuffle_off.setVisibility(View.VISIBLE);
                }
            }
        });

        repeat_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeat_on.setVisibility(View.GONE);
                repeat_off.setVisibility(View.VISIBLE);

                if (repeat_on.getVisibility() == View.VISIBLE) {
                    repeat_on.setVisibility(View.GONE);
                    repeat_off.setVisibility(View.VISIBLE);
                }
            }
        });

        repeat_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeat_off.setVisibility(View.GONE);
                repeat_on.setVisibility(View.VISIBLE);

                if (repeat_off.getVisibility() == View.VISIBLE) {
                    repeat_off.setVisibility(View.GONE);
                    repeat_on.setVisibility(View.VISIBLE);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void setTabIcons() {

        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(tabIcons[2]);
        Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(tabIcons[3]);

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new songsFragment(), "SONGS");
        adapter.addFragment(new albumsFragment(), "ALBUMS");
        adapter.addFragment(new artistsFragment(), "ARTISTS");
        adapter.addFragment(new playlistsFragment(), "PLAYLISTS");

        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            this.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            this.startService(playIntent);
        }
    }

    public void playNext() {
        musicSrv.playNext();
    }

    public void playPrev() {
        musicSrv.playPrev();
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if (musicSrv != null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onDestroy() {

        if (musicBound) musicSrv.unbindService(musicConnection);
        this.stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }
}
