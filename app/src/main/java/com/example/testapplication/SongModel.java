package com.example.testapplication;


import java.util.HashMap;

public class SongModel {

    private String artist;
    private String songName;
    private String songLink;
    private String songArt;
    private String songId;
   // boolean isBlocked = false;

    public SongModel() {
    }

    public SongModel(String artist, String songName, String songLink, String songArt, String songId) {
        this.artist = artist;
        this.songName = songName;
        this.songLink = songLink;
        this.songArt = songArt;
        this.songId = songId;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getSongArt() {
        return songArt;
    }

    public void setSongArt(String songArt) {
        this.songArt = songArt;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }


}


