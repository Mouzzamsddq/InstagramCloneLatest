
package com.example.instagramclone.Fragment;

import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Sampler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.instagramclone.Adapter.PostAdapter;
import com.example.instagramclone.Adapter.StoryAdapter;
import com.example.instagramclone.ChatListActivity;
import com.example.instagramclone.LoginActivity;
import com.example.instagramclone.Model.ModelPost;
import com.example.instagramclone.Model.Story;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {




    private ImageView chatImageView;
    List<String> followingList;
    PostAdapter postAdapter;
    List<ModelPost> postList;
    private RecyclerView postRecyclerView;
    ProgressBar progressBar;
    /* access modifiers changed from: private */
    public StoryAdapter storyAdapter;
    /* access modifiers changed from: private */
    public List<Story> storyList;
    private RecyclerView storyRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.postRecyclerView = (RecyclerView) view.findViewById(R.id.postRecyclerView);
        this.progressBar = (ProgressBar) view.findViewById(R.id.homeProgressBar);
        this.chatImageView = (ImageView) view.findViewById(R.id.chatImageView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        this.postRecyclerView.setLayoutManager(layoutManager);
        this.postList = new ArrayList();
        this.followingList = new ArrayList();
        PostAdapter postAdapter2 = new PostAdapter(getContext(), this.postList);
        this.postAdapter = postAdapter2;
        this.postRecyclerView.setAdapter(postAdapter2);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.storiesRecyclerView);
        this.storyRecyclerView = recyclerView;
        recyclerView.setHasFixedSize(true);
        this.storyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.storyList = new ArrayList();
        StoryAdapter storyAdapter2 = new StoryAdapter(getContext(), this.storyList);
        this.storyAdapter = storyAdapter2;
        this.storyRecyclerView.setAdapter(storyAdapter2);
        this.chatImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HomeFragment.this.startActivity(new Intent(HomeFragment.this.getContext(), ChatListActivity.class));
            }
        });
        checkFollowing();
        return view;
    }


    private void checkFollowing()
    {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    followingList.add(snapshot1.getKey());
                }


                readPosts();
                readStory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void readPosts() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {

                    ModelPost modelPost = snapshot1.getValue(ModelPost.class);
                    for(String id : followingList)
                    {
                        if(modelPost.getPostedBy().equals(id))
                        {
                            postList.add(modelPost);
                        }

                    }
                    if(modelPost.getPostedBy().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        postList.add(modelPost);
                    }
                }

                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }

    private void readStory()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Story");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long timeCurrent =  System.currentTimeMillis();
                storyList.clear();
                storyList.add(new Story("",0,0,"",FirebaseAuth.getInstance().getCurrentUser().getUid()
                ));

                for( String id : followingList)
                {
                    int countStory  = 0;
                    Story story = null;
                    for(DataSnapshot snapshot1 : snapshot.child(id).getChildren())
                    {
                        story = snapshot1.getValue(Story.class);

                        if(timeCurrent > story.getTimestart() && timeCurrent < story.getTimesEnd())
                        {
                            countStory++;
                        }
                    }
                    if(countStory > 0)
                    {
                        storyList.add(story);
                    }
                }
                storyAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}