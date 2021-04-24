package com.example.instagramclone;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.instagramclone.Adapter.ChatListAdapter;
import com.example.instagramclone.Model.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private ImageView backImageView;
    ChatListAdapter chatListAdapter;
    private RecyclerView chatListRecyclerView;
    List<String> chatUserList;
    List<String> followingList;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_chat_list);
        this.backImageView = (ImageView) findViewById(R.id.backButton);
        this.chatListRecyclerView = (RecyclerView) findViewById(R.id.chatListRecylerView);
        this.followingList = new ArrayList();
        this.chatUserList = new ArrayList();
        this.chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatListAdapter chatListAdapter2 = new ChatListAdapter(this, this.followingList);
        this.chatListAdapter = chatListAdapter2;
        this.chatListRecyclerView.setAdapter(chatListAdapter2);
        checkFollowing();
    }

    private void checkFollowing() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                ChatListActivity.this.followingList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ChatListActivity.this.followingList.add(snapshot1.getKey());
                }
                ChatListActivity.this.chatListAdapter.notifyDataSetChanged();
                Log.d("kkk", ChatListActivity.this.followingList.toString());
                for (int i = 0; i < ChatListActivity.this.followingList.size(); i++) {
                    ChatListActivity chatListActivity = ChatListActivity.this;
                    chatListActivity.lastMessage(chatListActivity.followingList.get(i));
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void lastMessage(final String userId) {
        FirebaseDatabase.getInstance().getReference("chats").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String theLastMessage = "default";
                String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelChat chat = (ModelChat) ds.getValue(ModelChat.class);
                    if (chat != null) {
                        String sender = chat.getSender();
                        String receiver = chat.getReceiver();
                        if (!(sender == null || receiver == null)) {
                            if ((chat.getReceiver().equals(myUid) && chat.getSender().equals(userId)) || (chat.getReceiver().equals(userId) && chat.getSender().equals(myUid))) {
                                theLastMessage = chat.getMessage();
                            }
                        }
                    }
                }
                ChatListActivity.this.chatListAdapter.setLastMessageMap(userId, theLastMessage);
                ChatListActivity.this.chatListAdapter.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
