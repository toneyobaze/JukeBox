package com.oladapo.jukebox.Fragments;

public class Song {

    private long id;
    private String title;
    private String artist;
    private String duration;

    public Song(long songID, String songTitle, String songArtist, String songDuration) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        duration=songDuration;
    }

    long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getDuration(){return duration;}

}
