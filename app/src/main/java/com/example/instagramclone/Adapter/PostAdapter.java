package com.example.instagramclone.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.instagramclone.CommentActvity;
import com.example.instagramclone.Fragment.profileFragment;
import com.example.instagramclone.HomeActivity;
import com.example.instagramclone.LikesActivity;
import com.example.instagramclone.Model.ModelPost;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends  RecyclerView.Adapter<PostAdapter.MyHolder> {

    private Context context;
    private List<ModelPost> postList;
    Integer Likes;

    public PostAdapter(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {


        final ModelPost modelPost = postList.get(position);

        Glide.with(context).load(postList.get(position).getPostImage())
                .apply(new RequestOptions().placeholder(R.drawable.ic_baseline_image_24)).into(holder.postImageView);


        final String postUID = postList.get(position).getPostedBy();

        final String postId  = postList.get(position).getPostId();


        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.likeImageView.getTag().equals("Like"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postId)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                }
                else
                {

                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postId)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                }

            }
        });




        holder.commentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context , CommentActvity.class);
                intent1.putExtra("postId",postId);
                intent1.putExtra("publisherUid",postUID);
                context.startActivity(intent1);
            }
        });

        holder.commentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context , CommentActvity.class);
                intent1.putExtra("postId",postId);
                intent1.putExtra("publisherUid",postUID);
                context.startActivity(intent1);
            }
        });

        holder.postTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.commentEditText.getText().toString().equals(""))
                {
                    Toast.makeText(context, "You can't send empty comment", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addComment(postId,holder.commentEditText);
                }
            }
        });

        holder.saveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.saveImageView.getTag().equals("save"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(postId).setValue(true);

                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(postId).removeValue();
                }
            }
        });


        holder.likeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, LikesActivity.class);
                intent1.putExtra("postId",postId);
                intent1.putExtra("isPost",true);
                context.startActivity(intent1);
            }
        });


        holder.optionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context , v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId())
                        {
                            case R.id.edit:
                                editPost(postId);
                                return true;

                            case R.id.delete:
                                FirebaseDatabase.getInstance().getReference("Posts")
                                        .child(postId).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                return true;

                            case R.id.Report:
                                Toast.makeText(context, "Report clicked", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.inflate(R.menu.post_menu);
                if(!modelPost.getPostedBy().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                }

                popupMenu.show();

            }
        });



        if(postUID != null) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Users").child(postUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    holder.usernameText1.setText(user.getUsername());
                    holder.usernameText2.setText(user.getUsername());
                    Glide.with(context).load(user.getImageUri()).into(holder.profileImageView);
                    Glide.with(context).load(user.getImageUri()).into(holder.profileCommentImageView);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        String postDescription = postList.get(position).getPostDescription();
        if ( postDescription.equals(""))
        {
            holder.captionTextView.setVisibility(View.GONE);
        }
        else
        {
            holder.captionTextView.setText(postDescription);
            holder.captionTextView.setVisibility(View.VISIBLE);

        }
        final String timestamp = postList.get(position).getTimestamp();

        //convert timestamp into date object;
        //convert timestamp to dd/mm/yyyy mm:hh am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String postedDate= DateFormat.format("dd-MM-yyyy HH:mm:ss",calendar).toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date d1 = simpleDateFormat.parse(postedDate);


            String currentTimeStamp = String.valueOf(System.currentTimeMillis());
            Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
            calendar1.setTimeInMillis(Long.parseLong(currentTimeStamp));
            String currentTime= DateFormat.format("dd-MM-yyyy HH:mm:ss",calendar1).toString();
            Date d2 = simpleDateFormat.parse(currentTime);
            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);
            long difference_In_Days
                    = (diff
                    / (1000 * 60 * 60 * 24))
                    % 365;

            Log.d("kkk","diff in days:"+difference_In_Days);
            Log.d("kkk","diff in seconds:"+diffSeconds);
            Log.d("kkk","diff in minutes:"+diffMinutes);
            Log.d("kkk","diff in hour:"+diffHours);


            if(difference_In_Days == 0)
            {
                if(diffHours == 0)
                {
                    if(diffMinutes == 0)
                    {
                        if(diffSeconds==0)
                        {
                            holder.timeTextView.setVisibility(View.GONE);
                        }
                        else
                        {
                            holder.timeTextView.setText(diffSeconds+" seconds ago");
                        }
                    }
                    else
                    {
                        holder.timeTextView.setText(diffMinutes+" minutes ago");
                    }
                }
                else
                {
                    holder.timeTextView.setText(diffHours +" hours ago");
                }
            }
            else
            {
                if(difference_In_Days > 3)
                {
                    holder.timeTextView.setVisibility(View.GONE);
                }
                else
                {
                    holder.timeTextView.setVisibility(View.VISIBLE);
                    holder.timeTextView.setText(difference_In_Days+" days ago");
                }
            }







        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("kkk","Hello:");
        }


        isLiked(postId,holder.likeImageView);
        countLikes(holder.likeTextView , postId);
        getComment(postId,holder.commentTextView);
        isSaved(postId,holder.saveImageView);



        holder.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile(postUID);
            }
        });


        holder.usernameText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile(postUID);
            }
        });


        holder.usernameText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile(postUID);
            }
        });







    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {

        private ImageView profileImageView,postImageView,profileCommentImageView;
        private TextView usernameText1,usernameText2;
        private ImageView likeImageView,commentImageView,saveImageView,moreImageView,optionImageView;
        private TextView captionTextView,likeTextView,timeTextView,commentTextView,postTextView;
        private EditText commentEditText;
        public MyHolder(@NonNull View itemView) {
            super(itemView);


            profileImageView = itemView.findViewById(R.id.profileImageView);
            usernameText1 = itemView.findViewById(R.id.usernameText1);
            usernameText2 = itemView.findViewById(R.id.usernameText2);
            likeImageView = itemView.findViewById(R.id.likeImageView);
            commentImageView = itemView.findViewById(R.id.commentImageView);
            saveImageView = itemView.findViewById(R.id.saveImageView);
            moreImageView = itemView.findViewById(R.id.optionImageView);
            captionTextView = itemView.findViewById(R.id.descriptionTextView);
            likeTextView = itemView.findViewById(R.id.likeTextView);
            postImageView = itemView.findViewById(R.id.postImageView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            profileCommentImageView =  itemView.findViewById(R.id.profileCommentView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            commentEditText = itemView.findViewById(R.id.commentEditText);
            postTextView = itemView.findViewById(R.id.postButton);
            optionImageView = itemView.findViewById(R.id.optionImageView);


        }
    }


    private void isLiked(String postId, final ImageView likeImageView)
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists())
                {
                    likeImageView.setImageResource(R.drawable.ic_action_like_red);
                    likeImageView.setTag("Liked");
                }
                else
                {
                    likeImageView.setImageResource(R.drawable.ic_action_like);
                    likeImageView.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void countLikes(final TextView likeTextView , String postId)
    {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0) {
                    likeTextView.setVisibility(View.VISIBLE);
                    likeTextView.setText(snapshot.getChildrenCount() + " likes");
                }
                else
                {
                    likeTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getComment(String postId, final TextView commentTextView)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0) {
                    commentTextView.setVisibility(View.VISIBLE);
                    commentTextView.setText("View All " + snapshot.getChildrenCount() + " Comments");
                }
                else
                {
                    commentTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void addComment(String postId,EditText commentEditText) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(postId);

        String commentId = databaseReference.push().getKey();

        String timestamp = String.valueOf(System.currentTimeMillis());
        HashMap<String,Object>  hashMap = new HashMap<>();
        hashMap.put("comment",commentEditText.getText().toString());
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("timestamp",timestamp);
        hashMap.put("commentId",commentId);
        hashMap.put("postId",postId);

        databaseReference.child(commentId).setValue(hashMap);

        commentEditText.setText("");



    }


    private void openProfile(String profileId)
    {

        SharedPreferences.Editor editor =  context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
        editor.putString("profileUID",profileId);
        editor.apply();

        ((HomeActivity)context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainert,new profileFragment())
                .commit();



    }




    private void isSaved(final String postId, final ImageView imageView)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Saves").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postId).exists())
                {
                    imageView.setImageResource(R.drawable.ic_save_black);
                    imageView.setTag("saved");
                }

                else
                {
                    imageView.setImageResource(R.drawable.ic_action_save);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void editPost(final String postId)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Edit Post");

        final EditText editText = new EditText(context);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT ,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        getText(postId , editText);


        alertDialog.setPositiveButton("Edit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("postDescription", editText.getText().toString());

                        FirebaseDatabase.getInstance().getReference("Posts")
                                .child(postId).updateChildren(hashMap);


                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }


    private void getText(String postId , final EditText editText)
    {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts")
                .child(postId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editText.setText(snapshot.getValue(ModelPost.class).getPostDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}