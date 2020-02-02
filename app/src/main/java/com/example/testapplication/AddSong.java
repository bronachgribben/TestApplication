package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ModuleInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddSong extends AppCompatActivity {

    RecyclerView songRecycler;
    private ArrayList<SongModel> arrayList;
    private ArrayList<ModelGroupPlaylist> modelGroupPlaylists;
    private AdapterSongAdd adapterSongAdd;

    private FirebaseDatabase myFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference musicReference;
    private DatabaseReference groupReference;
    private String myGroupRole;
    private String groupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        myFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = myFirebaseDatabase.getReference();

        musicReference = myFirebaseDatabase.getReference("Music");

        groupReference = myFirebaseDatabase.getReference("GroupPlaylists");

        songRecycler = (RecyclerView) findViewById(R.id.SongsRv);
        songRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        arrayList = new ArrayList<SongModel>();
        modelGroupPlaylists = new ArrayList<ModelGroupPlaylist>();

        groupId = getIntent().getStringExtra("groupId");

        retrieveSongTitle();
        retrieveGroups();
    }



    public void retrieveSongTitle(){
        musicReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    SongModel songModel = dataSnapshot1.getValue(SongModel.class);
                    arrayList.add(songModel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void retrieveGroups(){
        groupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                    ModelGroupPlaylist modelGroupPlaylist = dataSnapshot2.getValue(ModelGroupPlaylist.class);
                    modelGroupPlaylists.add(modelGroupPlaylist);
                }

                adapterSongAdd = new AdapterSongAdd(AddSong.this, arrayList, modelGroupPlaylists, groupId);
                songRecycler.setAdapter(adapterSongAdd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}














