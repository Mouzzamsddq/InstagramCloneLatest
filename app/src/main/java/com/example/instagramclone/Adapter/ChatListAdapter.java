package com.example.instagramclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.instagramclone.ChatActivity;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<String> chatUserList;
    /* access modifiers changed from: private */
    public Context context;
    private HashMap<String, Object> lastMessageMap = new HashMap<>();

    public ChatListAdapter(Context context2, List<String> chatUserList2) {
        this.context = context2;
        this.chatUserList = chatUserList2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.chat_list_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final String userId = this.chatUserList.get(position);
        setUserDetails(userId, holder.profileImageView, holder.usernameTextView);
        holder.lastMessageTextView.setText((String) this.lastMessageMap.get(userId));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChatListAdapter.this.context, ChatActivity.class);
                intent.putExtra("userId", userId);
                ChatListAdapter.this.context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.chatUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView lastMessageTextView;
        /* access modifiers changed from: private */
        public ImageView profileImageView;
        /* access modifiers changed from: private */
        public TextView usernameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.usernameTextView = (TextView) itemView.findViewById(R.id.usernameTextView);
            this.lastMessageTextView = (TextView) itemView.findViewById(R.id.lastMessageTextView);
            this.profileImageView = (ImageView) itemView.findViewById(R.id.profileImageView);
        }
    }

    public void setLastMessageMap(String userId, String lastMessage) {
        this.lastMessageMap.put(userId, lastMessage);
    }

    private void setUserDetails(String userId, final ImageView profileImageView, final TextView usernameTextView) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                User user = (User) snapshot.getValue(User.class);
                usernameTextView.setText(user.getUsername());
                Glide.with(ChatListAdapter.this.context).load(user.getImageUri()).into(profileImageView);
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
