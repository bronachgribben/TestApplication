package com.example.testapplication;

public class Music {

    private String songName;
    private String artist;
    private String songLink;
    private String songArt;


    public Music() {
    }

    public Music(String songName, String artist, String songLink, String songArt) {
        this.songName = songName;
        this.artist = artist;
        this.songLink = songLink;
        this.songArt = songArt;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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
}
