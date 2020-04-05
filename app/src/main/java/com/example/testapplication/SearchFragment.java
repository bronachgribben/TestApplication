package com.example.testapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.auth.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.testapplication.R.raw.song1;

public class SearchFragment extends Fragment  {

    private View SongListView;
    private RecyclerView SongList;

    private DatabaseReference Songref;


    JcPlayerView  jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();


    public SearchFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        SongListView = inflater.inflate(R.layout.search_fragment, view, false);

        SongList = (RecyclerView) SongListView.findViewById(R.id.mySongList);
        SongList.setLayoutManager(new LinearLayoutManager(getContext()));
        //jcPlayerView = view.findViewById(R.id.jcplayer);

        Songref = FirebaseDatabase.getInstance().getReference().child("Music");

        return SongListView;



    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Music>()
                        .setQuery(Songref, Music.class)
                        .build();


        FirebaseRecyclerAdapter<Music, SearchViewHolder> adapter
                = new FirebaseRecyclerAdapter<Music, SearchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final SearchViewHolder holder, final int position, final Music model)
            {
                holder.SongName.setText(model.getSongName());
                holder.ArtistName.setText(model.getArtist());
                /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        holder.jcAudios.add(JcAudio.createFromURL(model.getSongName(), model.getSongLink()));

                    }
                }); */
            }



            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_list, viewGroup, false);
                SearchViewHolder viewHolder = new SearchViewHolder(view);
                return viewHolder;
            }
        };

        //jcPlayerView.initPlaylist(jcAudios, null);
        SongList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class SearchViewHolder extends RecyclerView.ViewHolder
    {
        public List jcAudios;
        TextView SongName, ArtistName;
        ImageView AlbumImage;

        public SearchViewHolder(@NonNull View itemView)
        {
            super(itemView);

            SongName = itemView.findViewById(R.id.SongTitle);
            ArtistName = itemView.findViewById(R.id.artist);
            AlbumImage = itemView.findViewById(R.id.users_profile_image);



        }
    }


}

    /*

    ArrayList<String> arrayListSongName = new ArrayList<>();
    ArrayList<String> arrayListSongLink = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;

    JcPlayerView  jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SongListView = inflater.inflate(R.layout.search_fragment, container, false);

        SongList = (RecyclerView) SongListView.findViewById(R.id.mySongList);
        SongList.setLayoutManager(new LinearLayoutManager(getContext()));
        jcPlayerView = SongListView.findViewById(R.id.jcplayer);

        Songref = FirebaseDatabase.getInstance().getReference("Music");

        return SongListView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Music>()
                .setQuery(Songref, Music.class)
                .build();

        FirebaseRecyclerAdapter<Music, SearchViewHolder> adapter =
                new FirebaseRecyclerAdapter<Music, SearchViewHolder>(options) {
                    @NonNull
                    @Override
                    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return null;
                    }

                    @Override
                    protected void onBindViewHolder(SearchViewHolder searchViewHolder, int position, Music music) {

                        holder..setText(model.getSongName());
                        holder.userStatus.setText(model.getArtist());
                        //Glide.with(getApplicationContext()).load(model.getImage()).into(holder.profileImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String visit_user_id = getRef(position).getKey();

                                Intent profileIntent = new Intent(SearchFragment.this, ProfileActivity.class);
                                profileIntent.putExtra("visit_user_id", visit_user_id);
                                startActivity(profileIntent);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

                    @NonNull
                    @Override
                    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_list, viewGroup, false);
                        SearchViewHolder viewHolder = new SearchViewHolder(view);
                        return viewHolder;
                    }
                };

        SongList.setAdapter(adapter);
        adapter.startListening();
    }


                    public static class SearchViewHolder extends RecyclerView.ViewHolder
                    {
                        TextView SongName, SongArtist;
                        ImageView AlbumCoverImage;



                        public SearchViewHolder(@NonNull View itemView)
                        {
                            super(itemView);

                            SongName = itemView.findViewById(R.id.);
                            SongArtist = itemView.findViewById(R.id.);
                            AlbumCoverImage = itemView.findViewById(R.id.);

                        }
                    }



*/
















/*





                }SonglistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }

    private void retrieveSongs() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Music");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    music = ds.getValue(Music.class);
                    arrayListSongName.add(music.getSongName());
                    arrayListSongLink.add(music.getSongLink());
                    jcAudios.add(JcAudio.createFromURL(music.getSongName(), music.getSongLink()));

                }

                arrayAdapter = new ArrayAdapter<String>(listView.getContext(), android.R.layout.simple_list_item_1 , arrayListSongName){

                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        TextView textView = view.findViewById(android.R.id.text1);

                        textView.setSingleLine(true);
                        textView.setMaxLines(1);


                        return view;
                    }
                };
                jcPlayerView.initAnonPlaylist(jcAudios);
                listView.setAdapter(arrayAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });




    } */







