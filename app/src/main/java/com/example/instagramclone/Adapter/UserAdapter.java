package com.example.instagramclone.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.instagramclone.Fragment.profileFragment;
import com.example.instagramclone.HomeActivity;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    private FirebaseUser firebaseUser;
    private List<User> userList;

    public UserAdapter(Context context2, List<User> userList2) {
        this.context = context2;
        this.userList = userList2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.user_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = this.userList.get(position);
        holder.usernameTextView.setText(user.getUsername());
        holder.fullNameTextView.setText(user.getFullName());
        Glide.with(this.context).load(user.getImageUri()).into((ImageView) holder.profileImageView);
        isFollowing(user.getUID(), holder.followingTextView);
        if (user.getUID().equals(this.firebaseUser.getUid())) {
            holder.followingTextView.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = UserAdapter.this.context.getSharedPreferences("PREFS", 0).edit();
                editor.putString("profileUID", user.getUID());
                editor.apply();
                ((HomeActivity) UserAdapter.this.context).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainert, new profileFragment()).commit();
            }
        });
    }

    public int getItemCount() {
        return this.userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView followingTextView;
        public TextView fullNameTextView;
        public CircularImageView profileImageView;
        public TextView usernameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.usernameTextView = (TextView) itemView.findViewById(R.id.username);
            this.fullNameTextView = (TextView) itemView.findViewById(R.id.fullName);
            this.profileImageView = (CircularImageView) itemView.findViewById(R.id.profileImageView);
            this.followingTextView = (TextView) itemView.findViewById(R.id.followingTextView);
        }
    }

    private void isFollowing(final String userId, final TextView textView) {
        FirebaseDatabase.getInstance().getReference().child("follow").child(this.firebaseUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(userId).exists()) {
                    textView.setVisibility(0);
                } else {
                    textView.setVisibility(8);
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
