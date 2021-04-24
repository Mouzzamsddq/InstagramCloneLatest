package com.example.instagramclone.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.HomeActivity;
import com.example.instagramclone.LikesActivity;
import com.example.instagramclone.Model.ModelComment;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter  extends  RecyclerView.Adapter<CommentAdapter.ViewHolder> {


    private Context context;
    private List<ModelComment> commentList;

    public CommentAdapter(Context context, List<ModelComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        final ModelComment modelComment=  commentList.get(position);

        holder.commentTextView.setText(modelComment.getComment());
        final String postId = modelComment.getPostId();
        getUserInfo(holder.profileImageView,holder.usernameTextView,modelComment.getPublisher());


        holder.usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, HomeActivity.class);
                intent1.putExtra("publisherUid",modelComment.getPublisher());
                context.startActivity(intent1);
            }
        });

        final String commentId = commentList.get(position).getCommentId();


        holder.likeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, LikesActivity.class);
                intent1.putExtra("commentId",commentId);
                context.startActivity(intent1);
            }
        });

        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(holder.likeImageView.getTag().equals("Like"))
                {
                    FirebaseDatabase.getInstance().getReference().child("CommentLikes").child(commentId)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                }
                else
                {

                    FirebaseDatabase.getInstance().getReference().child("CommentLikes").child(commentId)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                }
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Are you sure want to delete this comment?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Comments")
                                .child(postId).child(commentId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {

                                    FirebaseDatabase.getInstance().getReference().child("CommentLikes").child(commentId)
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.show();


                return true;
            }
        });


        holder.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, HomeActivity.class);
                intent1.putExtra("publisherUid",modelComment.getPublisher());
                context.startActivity(intent1);
            }
        });

        String timestamp = modelComment.getTimestamp();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String postCommentTime= DateFormat.format("dd-MM-yyyy HH:mm:ss",calendar).toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            Date d1 = simpleDateFormat.parse(postCommentTime);


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
                            holder.timeTextView.setText(diffSeconds+" s");
                        }
                    }
                    else
                    {
                        holder.timeTextView.setText(diffMinutes+" m");
                    }
                }
                else
                {
                    holder.timeTextView.setText(diffHours +" h");
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
                    holder.timeTextView.setText(difference_In_Days+" d");
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


        isLiked(commentId,holder.likeImageView);
        countLikes(holder.likeTextView,commentId);

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {



        public TextView usernameTextView,commentTextView,timeTextView,likeTextView;
        public CircularImageView profileImageView;
        public ImageView likeImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            timeTextView =  itemView.findViewById(R.id.timeTextView);
            likeImageView = itemView.findViewById(R.id.likeImageView);
            likeTextView = itemView.findViewById(R.id.likeTextView);


        }
    }

    private void getUserInfo(final CircularImageView profileImageView, final TextView usernameTextView, String publisherId)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisherId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getImageUri()).into(profileImageView);
                usernameTextView.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void isLiked(String commentId, final ImageView likeImageView)
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference()
                .child("CommentLikes")
                .child(commentId);

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


    private void countLikes(final TextView likeTextView , String commentId)
    {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("CommentLikes")
                .child(commentId);

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


}