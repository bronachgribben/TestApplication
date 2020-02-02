package com.example.testapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterParticipantAdd extends RecyclerView.Adapter<AdapterParticipantAdd.HolderParticipantAdd> {

    private ParticipantAddActivity context;
    private ArrayList<ModelUser> userList;
    private String groupId, myGroupRole; //creator, admin, participant

    public AdapterParticipantAdd(ParticipantAddActivity context, ArrayList<ModelUser> userList, String groupId, String myGroupRole) {
        this.context = context;
        this.userList = userList;
        this.groupId = groupId;
        this.myGroupRole = myGroupRole;
    }

    public AdapterParticipantAdd(AddSong addSong, ArrayList<SongModel> arrayList, String groupId, String myGroupRole) {
    }

    @NonNull
    @Override
    public HolderParticipantAdd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.participant_display_layout, parent, false);

        return new HolderParticipantAdd(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderParticipantAdd holder, int position) {
        //get data
        final ModelUser modelUser = userList.get(position);
        String name = modelUser.getName();
        String image = modelUser.getImage();
        final String uid = modelUser.getUid();

        //set data
        holder.contactName.setText(name);
        holder.contactImage.setImageResource(R.drawable.profile_image);

        checkIfAlreadyExists(modelUser, holder);

        //handle click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user not added show in participant option list
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
                ref.child(groupId).child("Participants").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    //user exists/ participant
                                    String hisPreviousRole = ""+dataSnapshot.child("role").getValue();

                                    //options to display dialog
                                    String[] options;

                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Choose Option");
                                    if (myGroupRole.equals("creator")){
                                        if(hisPreviousRole.equals("admin")){
                                            //im creator
                                            options = new String[] {"Remove Admin", "Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    //handle item clicks
                                                    if (which ==0){
                                                        //Remove admin clicked
                                                        removeAdmin(modelUser);
                                                    }
                                                    else {
                                                        //Remove user clicked
                                                        removeParticipant(modelUser);

                                                    }
                                                }
                                            }).show();

                                        }
                                        else if (hisPreviousRole.equals("participant")){
                                            //im participant
                                            options = new String[]{"Make Admin", "Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    //handle item clicks
                                                    if (which ==0){
                                                        //Make Admin clicked
                                                        makaeAdmin(modelUser);
                                                    }
                                                    else {
                                                        //Remove user clicked
                                                        removeParticipant(modelUser);

                                                    }
                                                }
                                            }).show();
                                        }

                                    }
                                    else if (myGroupRole.equals("admin")){
                                        if (hisPreviousRole.equals("creator")){
                                            //im admin
                                            Toast.makeText(context, "Creator of Group", Toast.LENGTH_SHORT).show();
                                        }
                                        else if (hisPreviousRole.equals("admin")){
                                            // im admin, he is admin too
                                            options = new String[] {"Remove Admin", "Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    //handle item clicks
                                                    if (which ==0){
                                                        //Remove admin clicked
                                                        removeAdmin(modelUser);
                                                    }
                                                    else {
                                                        //Remove user clicked
                                                        removeParticipant(modelUser);

                                                    }
                                                }
                                            }).show();

                                        }
                                        else if ( hisPreviousRole.equals("participant")){
                                            //im admin, he is participant
                                            options = new String []{"Make Admin", "Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    //handle item clicks
                                                    if (which ==0){
                                                        //Make Admin clicked
                                                        makaeAdmin(modelUser);
                                                    }
                                                    else {
                                                        //Remove user clicked
                                                        removeParticipant(modelUser);

                                                    }
                                                }
                                            }).show();
                                        }
                                    }



                                }
                                else {
                                    //user doesnt exist/ not participant: add
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Add Participant")
                                            .setMessage("Add this user in this group?")
                                            .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    //add user
                                                    addParticipant(modelUser);

                                                }
                                            })
                                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    dialogInterface.dismiss();

                                                }
                                            }).show();



                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    private void addParticipant(ModelUser modelUser) {
        //set up use data - add user in group
        String timestamp = ""+System.currentTimeMillis();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", modelUser.getUid());
        hashMap.put("role", "participant");
        hashMap.put("timestamp", ""+timestamp);
        //add that user in group>groupid>participant
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
        ref.child(groupId).child("Participants").child(modelUser.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added successfully
                        Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void makaeAdmin(ModelUser modelUser) {
        //set data - change role
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("role", "admin");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
        reference.child(groupId).child("Participants").child(modelUser.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //made admin
                        Toast.makeText(context, "This user is now admin", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //making admin
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void removeParticipant(ModelUser modelUser) {
        //remove participant from group
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
        reference.child(groupId).child("Participants").child(modelUser.getUid()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //removed successfully

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to remove

                    }
                });
    }

    private void removeAdmin(ModelUser modelUser) {
        //set data - remove admin - change role
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("role", "participant");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
        reference.child(groupId).child("Participants").child(modelUser.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //made admin
                        Toast.makeText(context, "This user is no longer admin", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //making admin
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void checkIfAlreadyExists(ModelUser modelUser, final HolderParticipantAdd holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GroupPlaylists");
        ref.child(groupId).child("Participants").child(modelUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            //already exists
                            String hisrole = ""+dataSnapshot.child("role").getValue();
                            holder.contactStatus.setText(hisrole);
                        }
                        else {
                            //doesnt exist
                            holder.contactStatus.setText("");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class HolderParticipantAdd extends RecyclerView.ViewHolder {

        private CircleImageView contactImage;
        private TextView contactName;
        private TextView contactStatus;



        public HolderParticipantAdd(@NonNull View itemView) {
            super(itemView);

            contactImage = itemView.findViewById(R.id.contact_image);
            contactName = itemView.findViewById(R.id.contact_name);
            contactStatus = itemView.findViewById(R.id.contact_status);
        }
    }
}
