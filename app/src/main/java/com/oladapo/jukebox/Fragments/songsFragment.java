package com.oladapo.jukebox.Fragments;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.oladapo.jukebox.MusicService;
import com.oladapo.jukebox.R;
import com.oladapo.jukebox.Song;
import com.oladapo.jukebox.SongAdapter;

import java.util.ArrayList;
import java.util.Objects;

import static android.provider.MediaStore.MediaColumns.TITLE;

public class songsFragment extends Fragment {

    private ArrayList<Song> songList;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    RelativeLayout trackRelLay;


    public songsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.songs_fragment, container, false);
        trackRelLay = v.findViewById(R.id.trackRelLay);

        return v;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView songView = view.findViewById(R.id.song_list);
        songList = new ArrayList<>();

        getSongList();

        SongAdapter songAdt = new SongAdapter(getActivity(), songList);
        songView.setAdapter(songAdt);

        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songPicked(view);
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
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void getSongList() {

        ContentResolver musicResolver = Objects.requireNonNull(getActivity()).getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("Recycle") Cursor musicCursor = musicResolver.query(musicUri, null, null, null, TITLE);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);

                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(getContext(), MusicService.class);
            Objects.requireNonNull(getActivity()).bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    private void songPicked(View view) {

        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();

    }

    @Override
    public void onDestroy() {

        if (musicBound) musicSrv.unbindService(musicConnection);
        Objects.requireNonNull(getActivity()).stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }
}
