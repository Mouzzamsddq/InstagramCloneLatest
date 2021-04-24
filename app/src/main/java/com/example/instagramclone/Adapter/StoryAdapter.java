package com.example.instagramclone.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.instagramclone.AddStoryActivity;
import com.example.instagramclone.Model.Story;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.example.instagramclone.StoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public Context mContext;
    private List<Story> storyList;

    public StoryAdapter(Context mContext2, List<Story> storyList2) {
        this.mContext = mContext2;
        this.storyList = storyList2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.add_story_item, parent, false);
        } else {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.story_item, parent, false);
        }
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Story story = this.storyList.get(position);
        userInfo(holder, story.getUserId(), position);
        if (holder.getAdapterPosition() != 0) {
            seenStory(holder, story.getUserId());
        }
        if (holder.getAdapterPosition() == 0) {
            myStory(holder.addStoryText, holder.storyPlus, false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (holder.getAdapterPosition() == 0) {
                    StoryAdapter.this.myStory(holder.addStoryText, holder.storyPlus, true);
                    return;
                }
                Intent intent = new Intent(StoryAdapter.this.mContext, StoryActivity.class);
                intent.putExtra("userId", story.getUserId());
                StoryAdapter.this.mContext.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.storyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView addStoryText;
        public ImageView storyPhoto;
        public ImageView storyPhotoSeen;
        public ImageView storyPlus;
        public TextView storyUserName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.storyPhoto = (ImageView) itemView.findViewById(R.id.storyPhoto);
            this.storyPlus = (ImageView) itemView.findViewById(R.id.storyPlus);
            this.storyPhotoSeen = (ImageView) itemView.findViewById(R.id.storyPhotoSeen);
            this.storyUserName = (TextView) itemView.findViewById(R.id.storyUsername);
            this.addStoryText = (TextView) itemView.findViewById(R.id.addStoryText);
        }
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    private void userInfo(final ViewHolder viewHolder, String userId, final int pos) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                User user = (User) snapshot.getValue(User.class);
                Glide.with(StoryAdapter.this.mContext).load(user.getImageUri()).into(viewHolder.storyPhoto);
                if (pos != 0) {
                    Glide.with(StoryAdapter.this.mContext).load(user.getImageUri()).into(viewHolder.storyPhotoSeen);
                    viewHolder.storyUserName.setText(user.getUsername());
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void myStory(final TextView textView, final ImageView imageView, final boolean click) {
        FirebaseDatabase.getInstance().getReference().child("Story").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                int count = 0;
                long timeCurrent = System.currentTimeMillis();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Story story = (Story) dataSnapshot.getValue(Story.class);
                    if (timeCurrent > story.getTimestart() && timeCurrent < story.getTimesEnd()) {
                        count++;
                    }
                }
                if (click) {
                    if (count > 0) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(StoryAdapter.this.mContext).create();
                        alertDialog.setButton(-2, "View story", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(StoryAdapter.this.mContext, StoryActivity.class);
                                intent.putExtra("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                StoryAdapter.this.mContext.startActivity(intent);
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.setButton(-1, "Add story", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                StoryAdapter.this.mContext.startActivity(new Intent(StoryAdapter.this.mContext, AddStoryActivity.class));
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                        return;
                    }
                    StoryAdapter.this.mContext.startActivity(new Intent(StoryAdapter.this.mContext, AddStoryActivity.class));
                } else if (count > 0) {
                    textView.setText("My Story");
                    imageView.setVisibility(8);
                } else {
                    textView.setText("Add story");
                    imageView.setVisibility(0);
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void seenStory(final ViewHolder viewHolder, String userId) {
        FirebaseDatabase.getInstance().getReference().child("Story").child(userId).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (!snapshot1.child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists() && System.currentTimeMillis() < ((Story) snapshot1.getValue(Story.class)).getTimesEnd()) {
                        i++;
                    }
                }
                if (i > 0) {
                    viewHolder.storyPhoto.setVisibility(0);
                    viewHolder.storyPhotoSeen.setVisibility(8);
                    return;
                }
                viewHolder.storyPhotoSeen.setVisibility(0);
                viewHolder.storyPhoto.setVisibility(8);
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
