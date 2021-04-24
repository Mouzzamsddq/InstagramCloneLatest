package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagramclone.Adapter.CommentAdapter;
import com.example.instagramclone.Model.ModelComment;
import com.example.instagramclone.Model.ModelPost;
import com.example.instagramclone.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActvity extends AppCompatActivity {



    private EditText commentEditText;
    private ImageView profileImageView;
    private TextView postCommentTextView;


    private CircularImageView postProfileImageView;
    private TextView usernameTextView;
    private TextView postDescriptionTextView;

    String postId,publisherUid;


    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private List<ModelComment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_actvity);


        commentEditText = findViewById(R.id.commentEditText);
        profileImageView = findViewById(R.id.profileImageView);
        postCommentTextView = findViewById(R.id.postCommentButton);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        postProfileImageView = findViewById(R.id.postProfileImageView);
        postDescriptionTextView = findViewById(R.id.commentTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        commentList = new ArrayList<>();
        commentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentActvity.this);
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(CommentActvity.this,commentList);
        commentRecyclerView.setAdapter(commentAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        publisherUid = intent.getStringExtra("publisherUid");


        getImage();
        readComments();
        setPostDescription();
        setPostPublisherInfo();

        postCommentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentEditText.getText().toString().equals(""))
                {
                    Toast.makeText(CommentActvity.this, "You can't send empty comment", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addComment();
                }
            }
        });


    }

    private void addComment() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(postId);


        String timestamp = String.valueOf(System.currentTimeMillis());
        String commentId = databaseReference.push().getKey();


        HashMap<String,Object>  hashMap = new HashMap<>();
        hashMap.put("comment",commentEditText.getText().toString());
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("timestamp",timestamp);
        hashMap.put("commentId",commentId);
        hashMap.put("postId",postId);

        databaseReference.child(commentId).setValue(hashMap);

        commentEditText.setText("");



    }

    private void getImage()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user  = snapshot.getValue(User.class);

                Glide.with(CommentActvity.this).load(user.getImageUri()).into(profileImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readComments()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();


                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    ModelComment comment = snapshot1.getValue(ModelComment.class);
                    Log.d("kkk","comment:"+comment.getComment());
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setPostDescription()
    {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelPost modelPost = snapshot.getValue(ModelPost.class);
                Log.d("kkk","onCreate:"+modelPost.getPostDescription());
                postDescriptionTextView.setText(modelPost.getPostDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void setPostPublisherInfo(){

        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisherUid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                Glide.with(CommentActvity.this).load(user.getImageUri()).into(postProfileImageView);
                usernameTextView.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}