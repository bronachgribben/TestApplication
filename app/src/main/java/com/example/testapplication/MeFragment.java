package com.example.testapplication;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View.OnClickListener;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.testapplication.R.*;
import static com.example.testapplication.R.raw.song1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class MeFragment extends Fragment
{

    private View RequestFragmentView;
    private RecyclerView myRequestList;

    private String currentUser;

    private CircleImageView userProfileImage;
    private TextView userProfileName, userProfileStatus;


    private DatabaseReference UserRef, ChatRequestRef, ContactsRef;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RequestFragmentView = inflater.inflate(layout.activity_me_fragment, container,
                false);

        myRequestList = (RecyclerView) RequestFragmentView.findViewById(R.id.friend_request_list);
        myRequestList.setLayoutManager(new LinearLayoutManager(getContext()));



        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("User");
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");


        currentUser = mAuth.getCurrentUser().getUid();

        userProfileImage = (CircleImageView) RequestFragmentView.findViewById(R.id.profile_image);
        userProfileName = (TextView) RequestFragmentView.findViewById(R.id.profile_name);
        userProfileStatus = (TextView) RequestFragmentView.findViewById(R.id.profile_status);


        CurrentUserInfo();
        return RequestFragmentView;
    }

    private void CurrentUserInfo() {
        UserRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if all information has been changed - show all
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {

                    String UserName = dataSnapshot.child("name").getValue().toString();
                    String Status = dataSnapshot.child("status").getValue().toString();


                    userProfileName.setText(UserName);
                    userProfileStatus.setText(Status);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}









