package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ShareFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.share_fragment, container, false);
        Button btn = (Button) v.findViewById(R.id.find_friends);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                sendUserToFindFriends();
            }
        } );
 return v;
    }

    private void sendUserToFindFriends()
    {
      Intent intent = new Intent(getActivity(), FindFriendsActivity.class);
      startActivity(intent);

    }
}
