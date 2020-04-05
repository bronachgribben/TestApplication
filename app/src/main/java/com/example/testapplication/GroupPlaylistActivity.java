package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupPlaylistActivity extends AppCompatActivity
{
    private Toolbar mToolbar;
    private ImageButton SendMessageButton;
    private EditText UserMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, GroupNameRef, GroupMessageKeyRef;

    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_playlist);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(GroupPlaylistActivity.this, currentGroupName, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("User");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);
        
        InitializeFields();
        
        GetUserInfo();

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SaveMessageInfoToDatabase();

                UserMessageInput.setText("");

                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                if (dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) 
            {
                if (dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void InitializeFields()
    {
        mToolbar = (Toolbar) findViewById(R.id.group_playlist_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGroupName);

        SendMessageButton = (ImageButton) findViewById(R.id.send_message_button);
        UserMessageInput = (EditText) findViewById(R.id.input_song_name);
        displayTextMessages = (TextView) findViewById(R.id.group_playlist_text_display);
        mScrollView = (ScrollView) findViewById(R.id.my_scroll_view);
    }

    private void GetUserInfo()
    {
        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    currentUserName = dataSnapshot.child("name").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SaveMessageInfoToDatabase()
    {
       String message = UserMessageInput.getText().toString();
       String messageKey = GroupNameRef.push().getKey();

       if (TextUtils.isEmpty(message))
       {
           Toast.makeText(this, "Please enter song first..", Toast.LENGTH_SHORT).show();
       }
       else
       {
           Calendar calForDate = Calendar.getInstance();
           SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
           currentDate = currentDateFormat.format(calForDate.getTime());

           Calendar calForTime = Calendar.getInstance();
           SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
           currentTime = currentTimeFormat.format(calForTime.getTime());

           HashMap<String, Object> groupPlaylistKey = new HashMap<>();
           GroupNameRef.updateChildren(groupPlaylistKey);

           GroupMessageKeyRef = GroupNameRef.child(messageKey);

           HashMap<String, Object> messageInfoMap = new HashMap<>();
               messageInfoMap.put("name", currentUserName);
               messageInfoMap.put("message", message);
               messageInfoMap.put("date", currentDate);
               messageInfoMap.put("time", currentTime);
            GroupMessageKeyRef.updateChildren(messageInfoMap);
       }
    }

    private void DisplayMessages(DataSnapshot dataSnapshot)
    {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            displayTextMessages.append(chatName + " :\n" + chatMessage + "\n" + chatTime + "    " + chatDate + "\n\n\n");

            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu, menu);

        menu.findItem(R.id.main_settings_option).setVisible(false);
        menu.findItem(R.id.main_playlist_option).setVisible(false);
        menu.findItem(R.id.main_find_friends_option).setVisible(false);
        menu.findItem(R.id.main_logout_option).setVisible(false);

        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.add_contact)
        {
            mAuth.signOut();
            SendUserToContactsActivity();

        }
        return true;
    }


    private void SendUserToContactsActivity()
    {
        Intent ContactsIntent = new Intent(GroupPlaylistActivity.this,AddContactsActivity.class);
        ContactsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ContactsIntent);
    }




}
