package com.example.instagramclone;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.instagramclone.Adapter.SpecificPostAdapter;
import com.example.instagramclone.Model.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MyPostActivity extends AppCompatActivity {
    private ImageView backButton;
    boolean isPost;
    SpecificPostAdapter myPostAdapter;
    SpecificPostAdapter myPostAdapterSave;
    List<ModelPost> myPostList;
    List<ModelPost> myPostListSave;
    private RecyclerView myPostRecyclerView;
    List<String> mySavesList;
    private RecyclerView savePostRecyclerView;
    private TextView titleTextView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_my_post);
        this.backButton = (ImageView) findViewById(R.id.backButton);
        this.myPostRecyclerView = (RecyclerView) findViewById(R.id.myPostRecyclerView);
        this.savePostRecyclerView = (RecyclerView) findViewById(R.id.savePostRecyclerView);
        this.titleTextView = (TextView) findViewById(R.id.titleTextView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        this.myPostList = new ArrayList();
        this.myPostRecyclerView.setLayoutManager(linearLayoutManager);
        this.myPostAdapter = new SpecificPostAdapter(this, this.myPostList);
        this.myPostRecyclerView.setHasFixedSize(true);
        this.myPostRecyclerView.setAdapter(this.myPostAdapter);
        this.myPostListSave = new ArrayList();
        LinearLayoutManager savePostLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        savePostLinearLayoutManager.setStackFromEnd(true);
        savePostLinearLayoutManager.setReverseLayout(true);
        this.savePostRecyclerView.setLayoutManager(savePostLinearLayoutManager);
        this.myPostAdapterSave = new SpecificPostAdapter(this, this.myPostListSave);
        this.savePostRecyclerView.setHasFixedSize(true);
        this.savePostRecyclerView.setAdapter(this.myPostAdapterSave);
        boolean booleanExtra = getIntent().getBooleanExtra("isPost", false);
        this.isPost = booleanExtra;
        if (booleanExtra) {
            this.savePostRecyclerView.setVisibility(8);
            this.myPostRecyclerView.setVisibility(0);
        } else {
            this.savePostRecyclerView.setVisibility(0);
            this.myPostRecyclerView.setVisibility(8);
            this.titleTextView.setText("Saves");
        }
        this.backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyPostActivity.this.finish();
            }
        });
        readPosts();
        mySaves();
    }

    public void readPosts() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                MyPostActivity.this.myPostList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelPost modelPost = (ModelPost) dataSnapshot.getValue(ModelPost.class);
                    if (modelPost.getPostedBy().equals(MyPostActivity.this.getSharedPreferences("PREFS", 0).getString("profileUID", (String) null))) {
                        MyPostActivity.this.myPostList.add(modelPost);
                    }
                }
                MyPostActivity.this.myPostAdapter.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void mySaves() {
        this.mySavesList = new ArrayList();
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                MyPostActivity.this.mySavesList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MyPostActivity.this.mySavesList.add(snapshot1.getKey());
                }
                MyPostActivity.this.readSaves();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void readSaves() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                MyPostActivity.this.myPostListSave.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ModelPost modelPost = (ModelPost) snapshot1.getValue(ModelPost.class);
                    for (String id : MyPostActivity.this.mySavesList) {
                        if (modelPost.getPostId().equals(id)) {
                            MyPostActivity.this.myPostListSave.add(modelPost);
                        }
                    }
                }
                MyPostActivity.this.myPostAdapterSave.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
