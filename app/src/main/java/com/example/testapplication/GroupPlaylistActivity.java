package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.testapplication.R.raw.song1;


public class GroupPlaylistActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    DatabaseReference ref;


    private String groupId, myGroupRole;
    private Toolbar mToolbar;
    private TextView groupTitle;
    private ImageButton addSong;
    private ListView listView;

    ArrayList<String> arrayListSongsName = new ArrayList<>();
    ArrayList<String> arrayListSongsUrl = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    Music music;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();



    private ArrayList<ModelGroupPlaylist> groupPlaylist;
    private AdapterGroupPlaylist adapterGroupPlaylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_playlist);



        groupTitle = (TextView) findViewById(R.id.groupTitle);
        addSong = (ImageButton) findViewById(R.id.add_music_button);
        mToolbar = findViewById(R.id.groupPlaylistToolbar);
        listView = (ListView) findViewById(R.id.playlistsongs);

        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSongList();
            }
        });

        setSupportActionBar(mToolbar);

        
        //get Id of the group
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");


        firebaseAuth = FirebaseAuth.getInstance();
        loadGroupInfo();
        LoadMyGroupRole();
        retrievePlaylist();

        



    }

    private void retrievePlaylist() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GroupPlaylist");
        databaseReference.child(groupId).child("Songs")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds :dataSnapshot.getChildren()){

                    Music music = ds.getValue(Music.class);
                    arrayListSongsName.add(music.getSongName());
                    arrayListSongsUrl.add(music.getSongLink());
                }

                arrayAdapter = new ArrayAdapter<String>(GroupPlaylistActivity.this, android.R.layout.simple_list_item_1, arrayListSongsName);
                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void openSongList() {
        Intent songListIntent = new Intent(GroupPlaylistActivity.this, AddSong.class);
        songListIntent.putExtra("groupId", groupId);
        startActivity(songListIntent);
    }

    private void LoadMyGroupRole() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
        ref.child(groupId).child("Participants")
                .orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            myGroupRole = ""+ds.child("role").getValue();
                            //refresh menu items
                            invalidateOptionsMenu();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadGroupInfo() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        menu.findItem(R.id.add_contact).setVisible(false);

        if(myGroupRole.equals("creator") || myGroupRole.equals("admin")){
            menu.findItem(R.id.add_contact).setVisible(true);
        }
        else {
            menu.findItem(R.id.add_contact).setVisible(false);

        }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_contact){
            Intent intent = new Intent (this, ParticipantAddActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}



