package com.example.testapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterGroupPlaylist extends RecyclerView.Adapter<AdapterGroupPlaylist.HolderGroupPlaylist> {

    private Context context;
    private ArrayList<ModelGroupPlaylist> groupPlaylists;

    public AdapterGroupPlaylist(Context context, ArrayList<ModelGroupPlaylist> groupPlaylists) {
        this.context = context;
        this.groupPlaylists = groupPlaylists;
    }

    @NonNull
    @Override
    public HolderGroupPlaylist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_list, parent, false);

        return new HolderGroupPlaylist(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroupPlaylist holder, int position) {
        //get data
        ModelGroupPlaylist model = groupPlaylists.get(position);
        final String groupId = model.getGroupId();
        String groupIcon = model.getGroupIcon();
        String groupTitle = model.getGroupTitle();

        //set data
        holder.playlistName.setText(groupTitle);
        try{
            Picasso.get().load(groupIcon).placeholder(R.drawable.groupplaylist).into(holder.groupIcon);
        }
        catch (Exception e) {
            holder.groupIcon.setImageResource(R.drawable.groupplaylist);
        }

        //handle group click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, GroupPlaylistActivity.class);
                intent.putExtra("groupId", groupId);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        return groupPlaylists.size();
    }


    //View holder class
    class HolderGroupPlaylist extends RecyclerView.ViewHolder {

        private ImageView groupIcon;
        private TextView playlistName;

        public HolderGroupPlaylist(@NonNull View itemView) {
            super(itemView);

            groupIcon = (ImageView) itemView.findViewById(R.id.music_note);
            playlistName = (TextView) itemView.findViewById(R.id.playlistName);
        }
    }
}
