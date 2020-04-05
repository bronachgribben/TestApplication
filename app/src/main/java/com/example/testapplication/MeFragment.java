package com.example.testapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;


import com.firebase.ui.auth.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    public MeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RequestFragmentView = inflater.inflate(layout.activity_me_fragment, container,
                false);

        myRequestList = (RecyclerView) RequestFragmentView.findViewById(R.id.friend_request_list);
        myRequestList.setLayoutManager(new LinearLayoutManager(getContext()));



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();

        UserRef = FirebaseDatabase.getInstance().getReference().child("User");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");


        return RequestFragmentView;
    }



    public static class RequestViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;
        Button AcceptButton, DeclineButton;

        public RequestViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(id.user_profile_name);
            userStatus = itemView.findViewById(id.user_profile_status);
            profileImage = itemView.findViewById(id.users_profile_image);
            AcceptButton = itemView.findViewById(id.accept_button);
            DeclineButton = itemView.findViewById(id.decline_button);
        }
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









