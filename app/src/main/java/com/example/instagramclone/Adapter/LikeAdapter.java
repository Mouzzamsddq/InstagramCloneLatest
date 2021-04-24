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
import com.mikhaellopez.circularimageview.CircularImageView;
import java.util.List;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    private List<String> followingList;
    private List<String> likeUserList;

    public LikeAdapter(Context context2, List<String> likeUserList2, List<String> followingList2) {
        this.context = context2;
        this.likeUserList = likeUserList2;
        this.followingList = followingList2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.like_item, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String likeUserUid = this.likeUserList.get(position);
        if (likeUserUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.followButton.setVisibility(4);
        }
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(likeUserUid).exists()) {
                    holder.followButton.setText("Following");
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
        getUserInfo(likeUserUid, holder.profileImageView, holder.usernameTextView, holder.fullNameTextView);
        holder.followButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (holder.followButton.getText().toString().equals("Follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").child(likeUserUid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(likeUserUid).child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                    holder.followButton.setText("Following");
                } else if (holder.followButton.getText().toString().equals("Following")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").child(likeUserUid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(likeUserUid).child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    holder.followButton.setText("Follow");
                }
            }
        });
    }

    public int getItemCount() {
        return this.likeUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button followButton;
        public TextView fullNameTextView;
        public CircularImageView profileImageView;
        public TextView usernameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.profileImageView = (CircularImageView) itemView.findViewById(R.id.profileImageView);
            this.usernameTextView = (TextView) itemView.findViewById(R.id.usernameTextView);
            this.fullNameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            this.followButton = (Button) itemView.findViewById(R.id.followButton);
        }
    }

    private void getUserInfo(String uid, final CircularImageView profileImageView, final TextView usernameTextView, final TextView nameTextView) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                User user = (User) snapshot.getValue(User.class);
                Glide.with(LikeAdapter.this.context).load(user.getImageUri()).into((ImageView) profileImageView);
                usernameTextView.setText(user.getUsername());
                nameTextView.setText(user.getFullName());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void setFollowText(Button followButton, String userId) {
        for (String id : this.followingList) {
            if (userId.equals(id)) {
                followButton.setText("Following");
            } else {
                followButton.setText("Follow");
            }
        }
    }
}
