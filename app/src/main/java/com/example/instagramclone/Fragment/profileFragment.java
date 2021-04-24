package com.example.instagramclone.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.instagramclone.Adapter.MyPostAdapter;
import com.example.instagramclone.EditProfileActivity;
import com.example.instagramclone.FollowersActivity;
import com.example.instagramclone.Model.ModelPost;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.MyPostActivity;
import com.example.instagramclone.OptionActivity;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class profileFragment extends Fragment {
    TextView bio;
    Button editProfile;
    FirebaseUser firebaseUser;
    TextView followers;
    LinearLayout followersLinearLayout;
    TextView following;
    LinearLayout followingLinearLayout;
    TextView fullName;
    ImageButton myPhotosButton;
    MyPostAdapter myPostAdapter;
    MyPostAdapter myPostAdapterSave;
    List<ModelPost> myPostList;
    List<ModelPost> myPostList_save;
    /* access modifiers changed from: private */
    public List<String> mySavesList;
    ImageView options;
    TextView posts;
    String profileId;
    ImageView profileImageView;
    RecyclerView recyclerView;
    ImageButton savePhotosButton;
    RecyclerView saveRecyclerView;
    /* access modifiers changed from: private */
    public RelativeLayout unfollowRelativeLayout;
    TextView username;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.profileId = getActivity().getSharedPreferences("PREFS", 0).getString("profileUID", (String) null);
        this.profileImageView = (ImageView) view.findViewById(R.id.profileImageView);
        this.options = (ImageView) view.findViewById(R.id.options);
        this.posts = (TextView) view.findViewById(R.id.posts);
        this.followers = (TextView) view.findViewById(R.id.followers);
        this.following = (TextView) view.findViewById(R.id.following);
        this.fullName = (TextView) view.findViewById(R.id.fullNameTextview);
        this.username = (TextView) view.findViewById(R.id.usernameTextView);
        this.bio = (TextView) view.findViewById(R.id.bioTextView);
        this.editProfile = (Button) view.findViewById(R.id.editProfile);
        this.savePhotosButton = (ImageButton) view.findViewById(R.id.savePhotos);
        this.myPhotosButton = (ImageButton) view.findViewById(R.id.myPhotos);
        this.followersLinearLayout = (LinearLayout) view.findViewById(R.id.followersLinearLayout);
        this.followingLinearLayout = (LinearLayout) view.findViewById(R.id.followingLinearLayout);
        this.unfollowRelativeLayout = (RelativeLayout) view.findViewById(R.id.unfollowRelativeLayout);
        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView2;
        recyclerView2.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        this.myPostList = new ArrayList();
        MyPostAdapter myPostAdapter2 = new MyPostAdapter(getContext(), this.myPostList, true);
        this.myPostAdapter = myPostAdapter2;
        this.recyclerView.setAdapter(myPostAdapter2);
        RecyclerView recyclerView3 = (RecyclerView) view.findViewById(R.id.recyclerViewSave);
        this.saveRecyclerView = recyclerView3;
        recyclerView3.setHasFixedSize(true);
        this.saveRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        this.myPostList_save = new ArrayList();
        MyPostAdapter myPostAdapter3 = new MyPostAdapter(getContext(), this.myPostList_save, false);
        this.myPostAdapterSave = myPostAdapter3;
        this.saveRecyclerView.setAdapter(myPostAdapter3);
        this.recyclerView.setVisibility(0);
        this.saveRecyclerView.setVisibility(8);
        this.options.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                profileFragment.this.startActivity(new Intent(profileFragment.this.getContext(), OptionActivity.class));
            }
        });
        this.followersLinearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (profileFragment.this.editProfile.getText().toString().equals("following") || profileFragment.this.editProfile.getText().toString().equals("Edit Profile")) {
                    SharedPreferences.Editor editor = profileFragment.this.getActivity().getSharedPreferences("F", 0).edit();
                    editor.putString("uid", profileFragment.this.profileId);
                    editor.apply();
                    Intent intent1 = new Intent(profileFragment.this.getContext(), FollowersActivity.class);
                    intent1.putExtra("position", 0);
                    profileFragment.this.startActivity(intent1);
                    return;
                }
                Toast.makeText(profileFragment.this.getContext(), "You can't see the followers", 0).show();
            }
        });
        this.followingLinearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (profileFragment.this.editProfile.getText().toString().equals("following") || profileFragment.this.editProfile.getText().toString().equals("Edit Profile")) {
                    SharedPreferences.Editor editor = profileFragment.this.getActivity().getSharedPreferences("F", 0).edit();
                    editor.putString("uid", profileFragment.this.profileId);
                    editor.apply();
                    Intent intent1 = new Intent(profileFragment.this.getContext(), FollowersActivity.class);
                    intent1.putExtra("position", 1);
                    profileFragment.this.startActivity(intent1);
                    return;
                }
                Toast.makeText(profileFragment.this.getContext(), "You cann't see the following", 0).show();
            }
        });
        this.myPhotosButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                profileFragment.this.recyclerView.setVisibility(0);
                profileFragment.this.saveRecyclerView.setVisibility(8);
            }
        });
        this.savePhotosButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                profileFragment.this.recyclerView.setVisibility(8);
                profileFragment.this.saveRecyclerView.setVisibility(0);
            }
        });
        this.posts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (profileFragment.this.editProfile.getText().toString().equals("following") || profileFragment.this.editProfile.getText().toString().equals("Edit Profile")) {
                    Intent intent1 = new Intent(profileFragment.this.getContext(), MyPostActivity.class);
                    intent1.putExtra("isPost", true);
                    profileFragment.this.startActivity(intent1);
                    return;
                }
                Toast.makeText(profileFragment.this.getContext(), "you can't see the posts", 0).show();
            }
        });
        userInfo();
        getFollowers();
        getNrPosts();
        MyPosts();
        mySaves();
        if (this.profileId.equals(this.firebaseUser.getUid())) {
            this.editProfile.setText("Edit Profile");
        } else {
            checkFollow();
            this.savePhotosButton.setVisibility(8);
        }
        this.editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String btn = profileFragment.this.editProfile.getText().toString();
                if (btn.equals("Edit Profile")) {
                    profileFragment.this.startActivity(new Intent(profileFragment.this.getContext(), EditProfileActivity.class));
                } else if (btn.equals("follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileFragment.this.firebaseUser.getUid()).child("following").child(profileFragment.this.profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileFragment.this.profileId).child("followers").child(profileFragment.this.firebaseUser.getUid()).setValue(true);
                } else if (btn.equals("following")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileFragment.this.firebaseUser.getUid()).child("following").child(profileFragment.this.profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileFragment.this.profileId).child("followers").child(profileFragment.this.firebaseUser.getUid()).removeValue();
                }
            }
        });
        return view;
    }

    private void userInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(this.profileId).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (profileFragment.this.getContext() != null) {
                    User user = (User) snapshot.getValue(User.class);
                    Glide.with(profileFragment.this.getContext()).load(user.getImageUri()).into(profileFragment.this.profileImageView);
                    profileFragment.this.username.setText(user.getUsername());
                    profileFragment.this.fullName.setText(user.getFullName());
                    profileFragment.this.bio.setText(user.getBio());
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private boolean checkFollow() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(this.firebaseUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(profileFragment.this.profileId).exists()) {
                    profileFragment.this.editProfile.setText("following");
                    profileFragment.this.unfollowRelativeLayout.setVisibility(8);
                    profileFragment.this.recyclerView.setVisibility(0);
                } else {
                    profileFragment.this.editProfile.setText("follow");
                    profileFragment.this.unfollowRelativeLayout.setVisibility(0);
                    profileFragment.this.recyclerView.setVisibility(8);
                }
                if (profileFragment.this.profileId.equals(profileFragment.this.firebaseUser.getUid())) {
                    profileFragment.this.unfollowRelativeLayout.setVisibility(8);
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
        if (this.editProfile.getText().equals("following")) {
            return true;
        }
        return false;
    }

    private void getFollowers() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(this.profileId).child("followers").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                profileFragment.this.followers.setText("" + snapshot.getChildrenCount());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Follow").child(this.profileId).child("following").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                profileFragment.this.following.setText("" + snapshot.getChildrenCount());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void getNrPosts() {
        FirebaseDatabase.getInstance().getReference("Posts").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (((ModelPost) snapshot1.getValue(ModelPost.class)).getPostedBy().equals(profileFragment.this.profileId)) {
                        i++;
                    }
                }
                profileFragment.this.posts.setText("" + i);
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void MyPosts() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                profileFragment.this.myPostList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ModelPost modelPost = (ModelPost) snapshot1.getValue(ModelPost.class);
                    if (modelPost.getPostedBy().equals(profileFragment.this.profileId)) {
                        profileFragment.this.myPostList.add(modelPost);
                    }
                    Collections.reverse(profileFragment.this.myPostList);
                    profileFragment.this.myPostAdapter.notifyDataSetChanged();
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void mySaves() {
        this.mySavesList = new ArrayList();
        FirebaseDatabase.getInstance().getReference().child("Saves").child(this.firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                profileFragment.this.mySavesList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    profileFragment.this.mySavesList.add(snapshot1.getKey());
                }
                profileFragment.this.readSaves();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void readSaves() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                profileFragment.this.myPostList_save.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ModelPost modelPost = (ModelPost) snapshot1.getValue(ModelPost.class);
                    for (String id : profileFragment.this.mySavesList) {
                        if (modelPost.getPostId().equals(id)) {
                            profileFragment.this.myPostList_save.add(modelPost);
                        }
                    }
                }
                profileFragment.this.myPostAdapterSave.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
