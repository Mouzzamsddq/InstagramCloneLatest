package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.instagramclone.Adapter.LikeAdapter;
import com.example.instagramclone.Model.User;
import com.facebook.share.internal.ShareConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class LikesActivity extends AppCompatActivity {
    String commentId;
    List<String> followingList;
    String id;
    /* access modifiers changed from: private */
    public LikeAdapter likeAdapter;
    private RecyclerView likeRecyclerView;
    /* access modifiers changed from: private */
    public List<String> likeUserList;
    String postId;
    /* access modifiers changed from: private */
    public RelativeLayout relativeLayout;
    /* access modifiers changed from: private */
    public EditText searchEditText;
    private ImageView searchImageView;
    String storyId;
    private Toolbar toolbar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_likes);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setTitle((CharSequence) "Likes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LikesActivity.this.finish();
            }
        });
        this.likeRecyclerView = (RecyclerView) findViewById(R.id.likeRecyclerView);
        this.searchImageView = (ImageView) findViewById(R.id.searchImageView);
        this.searchEditText = (EditText) findViewById(R.id.searchEditText);
        this.relativeLayout = (RelativeLayout) findViewById(R.id.rel1);
        this.likeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.likeUserList = new ArrayList();
        this.followingList = new ArrayList();
        checkFollowing();
        LikeAdapter likeAdapter2 = new LikeAdapter(this, this.likeUserList, this.followingList);
        this.likeAdapter = likeAdapter2;
        this.likeRecyclerView.setAdapter(likeAdapter2);
        this.searchImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LikesActivity.this.searchEditText.requestFocus();
                ((InputMethodManager) LikesActivity.this.getApplicationContext().getSystemService("input_method")).toggleSoftInputFromWindow(LikesActivity.this.relativeLayout.getApplicationWindowToken(), 2, 0);
            }
        });
        Intent intent1 = getIntent();
        boolean isPost = intent1.getBooleanExtra("isPost", false);
        if (intent1.getBooleanExtra("isStory", false)) {
            getSupportActionBar().setTitle((CharSequence) "Views");
            this.id = intent1.getStringExtra("id");
            this.storyId = intent1.getStringExtra("storyId");
            getViews();
            return;
        }
        if (isPost) {
            String stringExtra = intent1.getStringExtra(ShareConstants.RESULT_POST_ID);
            this.postId = stringExtra;
            readLikeUser(stringExtra);
        } else {
            String stringExtra2 = intent1.getStringExtra("commentId");
            this.commentId = stringExtra2;
            readCommentLike(stringExtra2);
        }
        this.searchEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    LikesActivity likesActivity = LikesActivity.this;
                    likesActivity.readLikeUser(likesActivity.postId);
                    return;
                }
                LikesActivity.this.searchUser(s.toString());
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void searchUser(String s) {
        FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s).endAt(s + "").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                LikesActivity.this.likeUserList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LikesActivity.this.likeUserList.add(((User) dataSnapshot.getValue(User.class)).getUID());
                }
                LikesActivity.this.likeAdapter.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void readLikeUser(String postUid) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postUid).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    LikesActivity.this.likeUserList.add(snapshot1.getKey());
                }
                LikesActivity.this.likeAdapter.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void readCommentLike(String commentId2) {
        FirebaseDatabase.getInstance().getReference().child("CommentLikes").child(commentId2).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    LikesActivity.this.likeUserList.add(snapshot1.getKey());
                }
                LikesActivity.this.likeAdapter.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void getViews() {
        FirebaseDatabase.getInstance().getReference().child("Story").child(this.id).child(this.storyId).child("views").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                LikesActivity.this.likeUserList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    LikesActivity.this.likeUserList.add(snapshot1.getKey());
                }
                LikesActivity.this.likeAdapter.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void checkFollowing() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                LikesActivity.this.followingList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    LikesActivity.this.followingList.add(snapshot1.getKey());
                }
                Log.d("kkk", LikesActivity.this.followingList.toString());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
