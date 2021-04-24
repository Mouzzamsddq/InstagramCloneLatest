package com.example.instagramclone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.instagramclone.Fragment.FollowingFragment;
import com.example.instagramclone.Fragment.followersFragment;
import com.example.instagramclone.Model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FollowersActivity extends AppCompatActivity {
    ImageView backImageView;
    TabLayout tabLayout;
    TextView titleTextView;
    ViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_followers);
        this.tabLayout = (TabLayout) findViewById(R.id.followersTabLayout);
        ViewPager viewPager2 = (ViewPager) findViewById(R.id.viewPager);
        this.viewPager = viewPager2;
        this.tabLayout.setupWithViewPager(viewPager2);
        this.titleTextView = (TextView) findViewById(R.id.usernameTitleTextView);
        ImageView imageView = (ImageView) findViewById(R.id.backButton);
        this.backImageView = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FollowersActivity.this.finish();
            }
        });
        getCountFollowers();
        getCountFollowing();
        setupViewPager(this.viewPager);
        this.viewPager.setCurrentItem(getIntent().getIntExtra("position", 2));
        getActivityTitle();
    }

    private void setupViewPager(ViewPager viewPager2) {
        SharedPreferences sharedPreferences = getSharedPreferences("F", 0);
        String followers = sharedPreferences.getString("followers", (String) null);
        String following = sharedPreferences.getString("following", (String) null);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new followersFragment(), followers + " followers");
        viewPagerAdapter.addFragment(new FollowingFragment(), following + " following");
        viewPager2.setAdapter(viewPagerAdapter);
    }

    private void getActivityTitle() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                FollowersActivity.this.titleTextView.setText(((User) snapshot.getValue(User.class)).getUsername());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void getCountFollowers() {
        String uid = getSharedPreferences("F", 0).getString("uid", (String) null);
        Log.d("kkk", "uid:" + uid);
        FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                SharedPreferences.Editor editor = FollowersActivity.this.getSharedPreferences("F", 0).edit();
                editor.putString("followers", String.valueOf(snapshot.getChildrenCount()));
                editor.apply();
                Log.d("kkk", "followers:" + snapshot.getChildrenCount());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void getCountFollowing() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(getSharedPreferences("F", 0).getString("uid", (String) null)).child("following").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                SharedPreferences.Editor editor = FollowersActivity.this.getSharedPreferences("F", 0).edit();
                editor.putString("following", String.valueOf(snapshot.getChildrenCount()));
                editor.apply();
                Log.d("kkk", "following:" + snapshot.getChildrenCount());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
