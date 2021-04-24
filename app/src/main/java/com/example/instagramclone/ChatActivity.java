package com.example.instagramclone;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.instagramclone.Adapter.ChatAdapter;
import com.example.instagramclone.Model.ModelChat;
import com.example.instagramclone.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    ChatAdapter chatAdapter;
    List<ModelChat> chatList;
    /* access modifiers changed from: private */
    public RecyclerView chatRecyclerView;
    private TextView fullNameTextView;
    /* access modifiers changed from: private */
    public String hisUid;
    /* access modifiers changed from: private */
    public EditText messageEditText;
    /* access modifiers changed from: private */
    public String myUid;
    private ImageView profileImageView;
    /* access modifiers changed from: private */
    public TextView sendTextView;
    private TextView usernameTextView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_chat);
        this.hisUid = getIntent().getStringExtra("userId");
        this.myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        readMessage();
        this.usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        this.fullNameTextView = (TextView) findViewById(R.id.fullNameTextView);
        this.profileImageView = (ImageView) findViewById(R.id.profileImageView);
        this.sendTextView = (TextView) findViewById(R.id.sendTextView);
        this.messageEditText = (EditText) findViewById(R.id.messageEditText);
        this.chatRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        this.chatList = new ArrayList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        this.chatRecyclerView.setHasFixedSize(true);
        this.chatRecyclerView.setLayoutManager(linearLayoutManager);
        updateToolbar(this.hisUid, this.profileImageView, this.usernameTextView, this.fullNameTextView);
        this.sendTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChatActivity chatActivity = ChatActivity.this;
                chatActivity.sendMessage(chatActivity.messageEditText, ChatActivity.this.myUid, ChatActivity.this.hisUid);
                ChatActivity.this.readMessage();
            }
        });
        this.messageEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    ChatActivity.this.sendTextView.setVisibility(4);
                } else {
                    ChatActivity.this.sendTextView.setVisibility(0);
                }
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendMessage(EditText editText, String myUid2, String hisUid2) {
        String message = editText.getText().toString();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "You cann't send the empty message..!", 0).show();
        }
        FirebaseDatabase.getInstance().getReference().child("chats").push().setValue(new ModelChat(myUid2, hisUid2, message, String.valueOf(System.currentTimeMillis())));
        this.messageEditText.setText("");
    }

    private void updateToolbar(String hisUid2, final ImageView profileImageView2, final TextView usernameTextView2, final TextView fullNameTextView2) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(hisUid2).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                User user = (User) snapshot.getValue(User.class);
                Glide.with(ChatActivity.this.getApplicationContext()).load(user.getImageUri()).into(profileImageView2);
                usernameTextView2.setText(user.getUsername());
                fullNameTextView2.setText(user.getFullName());
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void readMessage() {
        FirebaseDatabase.getInstance().getReference().child("chats").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                ChatActivity.this.chatList.clear();
                for (DataSnapshot ds1 : snapshot.getChildren()) {
                    ModelChat modelCHat = (ModelChat) ds1.getValue(ModelChat.class);
                    if (!(modelCHat.getSender() == null || modelCHat.getReceiver() == null)) {
                        if ((modelCHat.getSender().equals(ChatActivity.this.hisUid) && modelCHat.getReceiver().equals(ChatActivity.this.myUid)) || (modelCHat.getSender().equals(ChatActivity.this.myUid) && modelCHat.getReceiver().equals(ChatActivity.this.hisUid))) {
                            ChatActivity.this.chatList.add(modelCHat);
                            ChatActivity.this.chatAdapter = new ChatAdapter(ChatActivity.this.getApplicationContext(), ChatActivity.this.chatList);
                            ChatActivity.this.chatRecyclerView.setAdapter(ChatActivity.this.chatAdapter);
                        }
                    }
                }
            }

            public void onCancelled(DatabaseError error) {
                Toast.makeText(ChatActivity.this, "" + error.getMessage(), 0).show();
            }
        });
    }
}
