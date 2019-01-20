package com.oladapo.jukebox;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
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
import android.widget.Toast;

import com.oladapo.jukebox.Fragments.albumsFragment;
import com.oladapo.jukebox.Fragments.artistsFragment;
import com.oladapo.jukebox.Fragments.playlistsFragment;
import com.oladapo.jukebox.Fragments.songsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ImageButton play, pause, play_main, pause_main;
    private TabLayout tabLayout;
    private int[] tabIcons = {

            R.drawable.musical_note,
            R.drawable.album,
            R.drawable.singer,
            R.drawable.playlist
    };

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

        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "Song Is now Playing", Toast.LENGTH_SHORT).show();

                if (play_main.getVisibility() == View.VISIBLE) {
                    play_main.setVisibility(View.GONE);
                    pause_main.setVisibility(View.VISIBLE);

                }

            }

        });

        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "Song is Pause", Toast.LENGTH_SHORT).show();

                if (pause_main.getVisibility() == View.VISIBLE) {
                    pause_main.setVisibility(View.GONE);
                    play_main.setVisibility(View.VISIBLE);

                }

            }

        });

        play_main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                play_main.setVisibility(View.GONE);
                pause_main.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "Song Is now Playing", Toast.LENGTH_SHORT).show();

                if (play.getVisibility() == View.VISIBLE) {
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);

                }

            }

        });

        pause_main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pause_main.setVisibility(View.GONE);
                play_main.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "Song is Pause", Toast.LENGTH_SHORT).show();

                if (pause.getVisibility() == View.VISIBLE) {
                    pause.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                }
            }
        });
    }

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
}
