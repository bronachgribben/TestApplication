package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShareFragment extends Fragment {

    private RecyclerView groupPlaylist;

    private FirebaseUser mAuth;

    private ArrayList<ModelGroupPlaylist> groupPlaylists;
    private AdapterGroupPlaylist adapterGroupPlaylist;

    public ShareFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.share_fragment, container, false);


        groupPlaylist = (RecyclerView) view.findViewById(R.id.list_view);
        groupPlaylist.setHasFixedSize(true);
        groupPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));


        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        loadPlaylistList();

        return view;
    }

    private void loadPlaylistList() {
        groupPlaylists = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupPlaylists.size();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if (ds.child("Participants").child(mAuth.getUid()).exists()) {
                        ModelGroupPlaylist model = ds.getValue(ModelGroupPlaylist.class);
                        groupPlaylists.add(model);
                    }

                }

                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                adapterGroupPlaylist = new AdapterGroupPlaylist(getActivity(), groupPlaylists);
                groupPlaylist.setAdapter(adapterGroupPlaylist);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*

    private void searchPlaylistList(final String query) {
        groupPlaylists = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupPlaylists.size();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if (ds.child("Participants").child(mAuth.getUid()).exists()) {
                        if (ds.child("groupTitle").toString().toLowerCase().contains(query.toLowerCase())){
                            ModelGroupPlaylist model = ds.getValue(ModelGroupPlaylist.class);
                            groupPlaylists.add(model);
                        }

                    }

                }

                adapterGroupPlaylist = new AdapterGroupPlaylist(getActivity(), groupPlaylists);
                groupPlaylist.setAdapter(adapterGroupPlaylist);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}




