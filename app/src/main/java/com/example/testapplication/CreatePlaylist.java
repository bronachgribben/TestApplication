package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreatePlaylist extends AppCompatActivity {

    private Toolbar mToolbar;

    private FirebaseAuth firebaseAuth;

    private EditText playlistName;
    private Button createButton;
    String userID;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);


        playlistName = (EditText) findViewById(R.id.playlistName1);
        createButton = (Button) findViewById(R.id.createButton);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser.getUid();


        //handle click event

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreatingGroup();

            }
        });
    }

    private void startCreatingGroup() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Group");

        String playlistTitle = playlistName.getText().toString().trim();

        if (TextUtils.isEmpty(playlistTitle)) {
            Toast.makeText(this, "Please enter playlist title..", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();

        String g_timestamp = ""+System.currentTimeMillis();

        createGroup(
                ""+g_timestamp,
                ""+playlistTitle
        );

    }

    private void createGroup(final String g_timestamp, String playlistTitle) {
        //Setup info of group
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("groupId",""+g_timestamp);
        hashMap.put("groupTitle",""+playlistTitle);
        hashMap.put("timestamp",""+g_timestamp);
        hashMap.put("createdBy",""+firebaseAuth.getUid());

        //create group
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
        ref.child(g_timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //setup member info (add current user in contact list)
                        HashMap<String, String> hashMap1 = new HashMap<>();
                        hashMap1.put("uid", firebaseAuth.getUid());
                        hashMap1.put("role", "creator");
                        hashMap1.put("timestamp", g_timestamp);

                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Groups");
                        ref1.child(g_timestamp).child("Participants").child(firebaseAuth.getUid())
                                .setValue(hashMap1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //participant added
                                        progressDialog.dismiss();
                                        Toast.makeText(CreatePlaylist.this, "Group playlist created", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //failed to add
                                        progressDialog.dismiss();
                                        Toast.makeText(CreatePlaylist.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       //failed
                       progressDialog.dismiss();
                        Toast.makeText(CreatePlaylist.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}


