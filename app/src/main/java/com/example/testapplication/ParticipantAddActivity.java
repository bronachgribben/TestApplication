package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;




public class ParticipantAddActivity extends AppCompatActivity {

    private RecyclerView ContactsRv;

    private FirebaseAuth firebaseAuth;

    private String groupId;
    private String myGroupRole;

    private ArrayList<ModelUser> userList;
    private AdapterParticipantAdd adapterParticipantAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_add);


        firebaseAuth = FirebaseAuth.getInstance();

        ContactsRv = (RecyclerView) findViewById(R.id.ContactsRv);

        groupId = getIntent().getStringExtra("groupId");
        loadGroupInfo();

        
    }

    private void getAllUsers() {

        userList = new ArrayList<>();
        //load user from database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);

                    if (!firebaseAuth.getUid().equals(modelUser)){
                        //not currently signed in uid
                        userList.add(modelUser);
                    }
                }
                //setup adapter
                adapterParticipantAdd = new AdapterParticipantAdd(ParticipantAddActivity.this, userList, ""+groupId, ""+myGroupRole);
                ContactsRv.setAdapter((adapterParticipantAdd));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadGroupInfo() {
        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("GroupPlaylists");

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String groupId = ""+ds.child("groupId").getValue();
                    final String groupTitle = ""+ds.child("groupTitle").getValue();
                    String createdBy = ""+ds.child("createdBy").getValue();
                    String timestamp = ""+ds.child("timestamp").getValue();
                    //actionBar.setTitle("Add Participants");

                    ref1.child(groupId).child("Participants").child(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        myGroupRole = ""+dataSnapshot.child("role").getValue();
                                        //Toolbar.setTitle(groupTitle + "(" +myGroupRole+ ")");

                                        getAllUsers();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}