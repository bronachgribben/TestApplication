package com.example.testapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSongAdd extends RecyclerView.Adapter<AdapterSongAdd.HolderSongAdd> {

    private Context context;
    private ArrayList<SongModel> songList;
    private ArrayList<ModelGroupPlaylist> modelGroupPlaylists;

    private FirebaseDatabase myFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference musicReference;
    private String groupId;

    public AdapterSongAdd(Context c, ArrayList<SongModel> s, ArrayList<ModelGroupPlaylist> m, String group) {
        context = c;
        songList = s;
        modelGroupPlaylists = m;
        groupId = group;

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser firebaseUser = mAuth.getCurrentUser();

        myFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = myFirebaseDatabase.getReference();
        musicReference = myFirebaseDatabase.getReference("Music");
    }

    @NonNull
    @Override
    public HolderSongAdd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout

        return new HolderSongAdd(LayoutInflater.from(context).inflate(R.layout.activity_song, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final HolderSongAdd holder, int position) {
        final SongModel songModel = songList.get(position);

        //get data
        holder.songTitle.setText(songList.get(position).getSongName());
        holder.songArtist.setText(songList.get(position).getArtist());
        Glide.with(context).load(songList.get(position).getSongArt()).into(holder.albumCover);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mDatabaseReference
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    addSong(songModel);
                                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
            }


        });


    }


    private void addSong(final SongModel songModel) {

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference("GroupPlaylists").child(groupId);

        mDatabaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                mutableData.child("Songs").child(songModel.getSongId()).child("songId").setValue(songModel.getSongId());
                mutableData.child("Songs").child(songModel.getSongId()).child("songName").setValue(songModel.getSongName());
                mutableData.child("Songs").child(songModel.getSongId()).child("songArtist").setValue(songModel.getArtist());
                mutableData.child("Songs").child(songModel.getSongId()).child("songArt").setValue(songModel.getSongArt());
                mutableData.child("Songs").child(songModel.getSongId()).child("songLink").setValue(songModel.getSongLink());
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                System.out.println("Transaction Completed");

            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class HolderSongAdd extends RecyclerView.ViewHolder {

        TextView songTitle;
        TextView songArtist;
        ImageView albumCover;


        public HolderSongAdd(@NonNull View itemView) {
            super(itemView);

            songTitle = (TextView) itemView.findViewById(R.id.mySongTitle);
            songArtist = (TextView) itemView.findViewById(R.id.myartist);
            albumCover = (ImageView) itemView.findViewById(R.id.mymusic_note);
        }
    }
}



