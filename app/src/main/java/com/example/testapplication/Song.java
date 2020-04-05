package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Song extends AppCompatActivity {

    private String artist;
    private String song;
    private String songLink;



    public Song() {

    }

    public Song(String artist, String song, String songLink) {
        this.artist = artist;
        this.song = song;
        this.songLink = songLink;
    }

    public String getArtist() { return artist; }

    public void setArtist(String artist) { this.artist = artist; }

    public String getSong() { return song; }

    public void setSong(String song) { this.song = song; }

    public String getSongLink() { return songLink; }

    public void getSongLink(String songLink) { this.songLink = songLink; }

}


