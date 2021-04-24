package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.instagramclone.Model.Story;
import com.example.instagramclone.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {
    int counter = 0;
    ImageView image;
    List<String> images;
    long limit = 500;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == 0) {
                StoryActivity.this.pressTime = System.currentTimeMillis();
                StoryActivity.this.storiesProgressView.pause();
                return false;
            } else if (action != 1) {
                return false;
            } else {
                long now = System.currentTimeMillis();
                StoryActivity.this.storiesProgressView.resume();
                if (StoryActivity.this.limit < now - StoryActivity.this.pressTime) {
                    return true;
                }
                return false;
            }
        }
    };
    long pressTime = 0;
    LinearLayout r_seen;
    TextView seenNumber;
    StoriesProgressView storiesProgressView;
    ImageView storyDelete;
    List<String> storyIds;
    ImageView storyPhoto;
    TextView storyUsername;
    String userId;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_story);
        this.storiesProgressView = (StoriesProgressView) findViewById(R.id.storiesProgress);
        this.image = (ImageView) findViewById(R.id.storyImage);
        this.storyPhoto = (ImageView) findViewById(R.id.storyPhoto);
        this.storyUsername = (TextView) findViewById(R.id.storyUsername);
        this.r_seen = (LinearLayout) findViewById(R.id.r_seen);
        this.seenNumber = (TextView) findViewById(R.id.seenNumber);
        this.storyDelete = (ImageView) findViewById(R.id.storyDelete);
        this.r_seen.setVisibility(8);
        this.storyDelete.setVisibility(8);
        String stringExtra = getIntent().getStringExtra("userId");
        this.userId = stringExtra;
        if (stringExtra.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            this.r_seen.setVisibility(0);
            this.storyDelete.setVisibility(0);
        }
        getStories(this.userId);
        userInfo(this.userId);
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StoryActivity.this.storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(this.onTouchListener);
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StoryActivity.this.storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(this.onTouchListener);
        this.r_seen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(StoryActivity.this, LikesActivity.class);
                intent.putExtra("id", StoryActivity.this.userId);
                intent.putExtra("storyId", StoryActivity.this.storyIds.get(StoryActivity.this.counter));
                intent.putExtra("title", "views");
                intent.putExtra("isStory", true);
                StoryActivity.this.startActivity(intent);
            }
        });
        this.storyDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Story").child(StoryActivity.this.userId).child(StoryActivity.this.storyIds.get(StoryActivity.this.counter)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(StoryActivity.this, "Deleted", 0).show();
                            StoryActivity.this.finish();
                        }
                    }
                });
            }
        });
    }

    public void onNext() {
        RequestManager with = Glide.with(getApplicationContext());
        List<String> list = this.images;
        int i = this.counter;
        this.counter = i + 1;
        with.load(list.get(i)).into(this.image);
        addView(this.storyIds.get(this.counter));
        seenNumber(this.storyIds.get(this.counter));
    }

    public void onPrev() {
        if (this.counter - 1 >= 0) {
            RequestManager with = Glide.with(getApplicationContext());
            List<String> list = this.images;
            int i = this.counter;
            this.counter = i - 1;
            with.load(list.get(i)).into(this.image);
            seenNumber(this.storyIds.get(this.counter));
        }
    }

    public void onComplete() {
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.storiesProgressView.destroy();
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.storiesProgressView.pause();
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.storiesProgressView.resume();
        super.onResume();
    }

    private void getStories(String userId2) {
        this.images = new ArrayList();
        this.storyIds = new ArrayList();
        FirebaseDatabase.getInstance().getReference().child("Story").child(userId2).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                StoryActivity.this.images.clear();
                StoryActivity.this.storyIds.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Story story = (Story) dataSnapshot.getValue(Story.class);
                    long timeCurrent = System.currentTimeMillis();
                    if (timeCurrent > story.getTimestart() && timeCurrent < story.getTimesEnd()) {
                        StoryActivity.this.images.add(story.getImageUri());
                        StoryActivity.this.storyIds.add(story.getStoryId());
                    }
                }
                StoryActivity.this.storiesProgressView.setStoriesCount(StoryActivity.this.images.size());
                StoryActivity.this.storiesProgressView.setStoryDuration(5000);
                StoryActivity.this.storiesProgressView.setStoriesListener(StoryActivity.this);
                StoryActivity.this.storiesProgressView.startStories(StoryActivity.this.counter);
                Glide.with(StoryActivity.this.getApplicationContext()).load(StoryActivity.this.images.get(StoryActivity.this.counter)).into(StoryActivity.this.image);
                StoryActivity storyActivity = StoryActivity.this;
                storyActivity.addView(storyActivity.storyIds.get(StoryActivity.this.counter));
                StoryActivity storyActivity2 = StoryActivity.this;
                storyActivity2.seenNumber(storyActivity2.storyIds.get(StoryActivity.this.counter));
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void userInfo(String userId2) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId2).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                User user = (User) snapshot.getValue(User.class);
                Glide.with(StoryActivity.this.getApplicationContext()).load(user.getImageUri()).into(StoryActivity.this.storyPhoto);
                StoryActivity.this.storyUsername.setText(user.getUsername());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void addView(String storyId) {
        FirebaseDatabase.getInstance().getReference().child("Story").child(this.userId).child(storyId).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
    }

    /* access modifiers changed from: private */
    public void seenNumber(String storyId) {
        FirebaseDatabase.getInstance().getReference().child("Story").child(this.userId).child(storyId).child("views").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                StoryActivity.this.seenNumber.setText("" + snapshot.getChildrenCount());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
