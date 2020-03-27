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


    private String currentUserID;
    private FirebaseAuth mAuth;
    private View user;

    private TextView userProfileName, userProfileStatus;


    private DatabaseReference rootRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle savedInstanceState) {
        user = inflater.inflate(layout.activity_me_fragment, view, false);

        userProfileName = (TextView) user.findViewById(R.id.my_profile_name);
        userProfileStatus = (TextView) user.findViewById(R.id.my_profile_status);

        rootRef = FirebaseDatabase.getInstance().getReference().child("User");;
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        CurrentUserInfo();
        return user;


    }
//get users information
    private void CurrentUserInfo()
    {
        rootRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //if all information has been changed - show all
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                {

                    String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                    String retrieveStatus = dataSnapshot.child("status").getValue().toString();


                    userProfileName.setText(retrieveUserName);
                    userProfileStatus.setText(retrieveStatus);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }



}









