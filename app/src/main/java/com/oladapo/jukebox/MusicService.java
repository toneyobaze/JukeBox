package com.oladapo.jukebox;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private ArrayList<Song> songs;
    private Random rand;
    public boolean shuffleOn;
    public int songPosn;

    private final IBinder musicBind = new MusicBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){

        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.start();

    }

    public void onCreate(){

        super.onCreate();
        songPosn=0;
        player = new MediaPlayer();

        rand = new Random();

        initMusicPlayer();
    }

    public void initMusicPlayer(){

        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

    }

    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void playNext(){

        if (shuffleOn) {
            int newSong = songPosn;
            while (newSong == songPosn) {
                newSong = rand.nextInt(songs.size());
            }
            songPosn = newSong;
        } else {
            songPosn++;
            if (songPosn >= songs.size()) songPosn = 0;
        }
        playSong();
    }

    public void playPrev(){
        songPosn--;
        if(songPosn <0) songPosn=songs.size()-1;
        playSong();
    }

    public void setShuffle(){
        if (shuffleOn) shuffleOn = true;
        else shuffleOn = false;
    }

    public void playSong(){

        player.reset();

        //get song
        Song playSong = songs.get(songPosn);
//get id
        long currSong = playSong.getID();
//set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();

    }

    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }
}
