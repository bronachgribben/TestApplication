package com.example.testapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.auth.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.example.testapplication.R.raw.song1;

public class SearchFragment extends Fragment {

    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Music music;

    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_fragment, container,
                false);
        listView = (ListView) rootView.findViewById(R.id.mySongList);
        jcPlayerView = rootView.findViewById(R.id.jcplayer);

//get music from firebase
        music = new Music();
        listView = (ListView) rootView.findViewById(R.id.mySongList);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Music");
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.music_info, R.id.musicInfo, list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    music = ds.getValue(Music.class);
                    list.add(music.getSongName().toString() + "-" + music.getArtist());
                    //used to play music
                    jcAudios.add(JcAudio.createFromURL(music.getSongName(), music.getSongLink()));
                }

                jcPlayerView.initAnonPlaylist(jcAudios);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                final MediaPlayer sound = MediaPlayer.create(getActivity(), song1);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                        jcPlayerView.playAudio(jcAudios.get(position));





                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }


}





