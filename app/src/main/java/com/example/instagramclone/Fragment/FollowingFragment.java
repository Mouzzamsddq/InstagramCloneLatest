package com.example.instagramclone.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.instagramclone.Adapter.FollowersAdapter;
import com.example.instagramclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment {
    private RecyclerView followersRecyclerView;
    FollowersAdapter followingAdapter;
    List<String> followingList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        this.followersRecyclerView = (RecyclerView) view.findViewById(R.id.followersRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.followingList = new ArrayList();
        this.followingAdapter = new FollowersAdapter(getContext(), this.followingList);
        this.followersRecyclerView.setLayoutManager(linearLayoutManager);
        this.followersRecyclerView.setAdapter(this.followingAdapter);
        getFollowingList();
        return view;
    }

    private void getFollowingList() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(getActivity().getSharedPreferences("F", 0).getString("uid", (String) null)).child("following").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                FollowingFragment.this.followingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FollowingFragment.this.followingList.add(dataSnapshot.getKey());
                }
                FollowingFragment.this.followingAdapter.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
