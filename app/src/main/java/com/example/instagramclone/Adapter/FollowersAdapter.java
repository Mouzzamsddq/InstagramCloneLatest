package com.example.instagramclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.MyHolder> {
    /* access modifiers changed from: private */
    public Context context;
    private List<String> followersList;

    public FollowersAdapter(Context context2, List<String> followersList2) {
        this.context = context2;
        this.followersList = followersList2;
    }

    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(this.context).inflate(R.layout.like_item, parent, false));
    }

    public void onBindViewHolder(final MyHolder holder, int position) {
        final String uid = this.followersList.get(position);
        getUserInfo(uid, holder.profileImageView, holder.usernameTextView, holder.nameTextView);
        checkFollow(uid, holder.followButton);
        holder.followButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (holder.followButton.getText().toString().equals("follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").child(uid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                    holder.followButton.setText("following");
                } else if (holder.followButton.getText().toString().equals("following")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").child(uid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    holder.followButton.setText("follow");
                }
            }
        });
    }

    public int getItemCount() {
        return this.followersList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public Button followButton;
        public TextView nameTextView;
        public ImageView profileImageView;
        public TextView usernameTextView;

        public MyHolder(View itemView) {
            super(itemView);
            this.profileImageView = (ImageView) itemView.findViewById(R.id.profileImageView);
            this.usernameTextView = (TextView) itemView.findViewById(R.id.usernameTextView);
            this.nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            this.followButton = (Button) itemView.findViewById(R.id.followButton);
        }
    }

    private void getUserInfo(String userUid, final ImageView profileImageView, final TextView usernameTextView, final TextView nameTextView) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userUid).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                User user = (User) snapshot.getValue(User.class);
                Glide.with(FollowersAdapter.this.context).load(user.getImageUri()).into(profileImageView);
                usernameTextView.setText(user.getUsername());
                nameTextView.setText(user.getFullName());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void checkFollow(final String userId, final Button followButton) {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(userId).exists()) {
                    followButton.setText("following");
                } else {
                    followButton.setText("follow");
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
