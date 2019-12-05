package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FindFriendsActivity extends AppCompatActivity {

    private ImageButton searchbutton;
    private EditText searchinputtext;

    private RecyclerView searchResultList;

    private DatabaseReference allUsersDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        searchResultList = (RecyclerView) findViewById(R.id.search_result);
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));

        searchbutton = (ImageButton) findViewById(R.id.search_people_button);
        searchinputtext = (EditText) findViewById(R.id.search_box_input);

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String searchBoxInput = searchinputtext.getText().toString();
                searchPeopleAndFriends(searchBoxInput);
            }
        });

    }

    private void searchPeopleAndFriends(String searchBoxInput)
    {

        Toast.makeText(this, "searching...", Toast.LENGTH_LONG).show();

        Query searchPeopleandFriendsQuery = allUsersDatabaseRef.orderByChild("Identifer")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");

        FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>
                (
                        FindFriends.class,
                        R.layout.user_display_layout,
                        FindFriendsViewHolder.class,
                        searchPeopleandFriendsQuery


                ) {
            @Override
            protected void populateViewHolder(FindFriendsViewHolder viewHolder, FindFriends model, int i)
            {
                viewHolder(model.getIdentifier());
            }
        };


        searchResultList.setAdapter(firebaseRecyclerAdapter);

    }

    private void viewHolder(String identifier)
    {

    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setIdentifier(String identifier)
        {

            TextView username = (TextView) mView.findViewById(R.id.userid);
            username.setText(identifier);


        }
    }
}
