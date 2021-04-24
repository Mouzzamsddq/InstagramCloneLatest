package com.example.instagramclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.instagramclone.Model.ModelChat;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    List<ModelChat> chatMessageList;
    Context context;
    private int type;

    public ChatAdapter(Context context2, List<ModelChat> chatMessageList2) {
        this.context = context2;
        this.chatMessageList = chatMessageList2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.type = viewType;
        if (viewType == 1) {
            return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.chat_right, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.chat_left, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.messageTextView.setText(this.chatMessageList.get(position).getMessage());
        updateUserInfo(this.chatMessageList.get(position).getSender(), holder.profileImageView);
    }

    public int getItemCount() {
        return this.chatMessageList.size();
    }

    public int getItemViewType(int position) {
        if (this.chatMessageList.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return 1;
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView messageTextView;
        /* access modifiers changed from: private */
        public ImageView profileImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            this.profileImageView = (ImageView) itemView.findViewById(R.id.profileImageView);
        }
    }

    private void updateUserInfo(String userId, final ImageView profileImageView) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Glide.with(ChatAdapter.this.context).load(((User) snapshot.getValue(User.class)).getImageUri()).into(profileImageView);
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
