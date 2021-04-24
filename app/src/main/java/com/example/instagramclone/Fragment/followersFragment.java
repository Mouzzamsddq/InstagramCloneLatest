package com.example.instagramclone.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.instagramclone.Adapter.FollowersAdapter;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class followersFragment extends Fragment {
    FollowersAdapter followersAdapter;
    List<String> followersList;
    private RecyclerView followersRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        this.followersRecyclerView = (RecyclerView) view.findViewById(R.id.followersRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.followersList = new ArrayList();
        this.followersAdapter = new FollowersAdapter(getContext(), this.followersList);
        this.followersRecyclerView.setLayoutManager(linearLayoutManager);
        this.followersRecyclerView.setAdapter(this.followersAdapter);
        getFollowersList();
        return view;
    }

    private void getFollowersList() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followers").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                followersFragment.this.followersList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    followersFragment.this.followersList.add(dataSnapshot.getKey());
                }
                followersFragment.this.followersAdapter.notifyDataSetChanged();
                Log.d("kkk", "followerList:" + followersFragment.this.followersList.toString());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
