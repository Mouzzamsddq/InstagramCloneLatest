package com.example.instagramclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.CommentActvity;
import com.example.instagramclone.HomeActivity;
import com.example.instagramclone.LikesActivity;
import com.example.instagramclone.Model.ModelPost;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SpecificPostAdapter extends RecyclerView.Adapter<SpecificPostAdapter.MyHolder>{



    private Context context;
    private List<ModelPost> myPostList;


    public SpecificPostAdapter(Context context, List<ModelPost> myPostList) {
        this.context = context;
        this.myPostList = myPostList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.mypost_item,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {


        Glide.with(context).load(myPostList.get(position).getPostImage()).into(holder.postImageView);


        final String postUID = myPostList.get(position).getPostedBy();

        final String postId  = myPostList.get(position).getPostId();


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





        holder.commentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context , CommentActvity.class);
                intent1.putExtra("postId",postId);
                intent1.putExtra("publisherUid",postUID);
                context.startActivity(intent1);
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


        holder.commentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context , CommentActvity.class);
                intent1.putExtra("postId",postId);
                intent1.putExtra("publisherUid",postUID);
                context.startActivity(intent1);
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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        String postDescription = myPostList.get(position).getPostDescription();
        if ( postDescription.equals(""))
        {
            holder.captionTextView.setVisibility(View.GONE);
        }
        else
        {
            holder.captionTextView.setText(postDescription);
            holder.captionTextView.setVisibility(View.VISIBLE);

        }
        final String timestamp = myPostList.get(position).getTimestamp();

        //convert timestamp into date object;
        //convert timestamp to dd/mm/yyyy mm:hh am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String postedDate= DateFormat.format("dd-MM-yyyy HH:mm:ss",calendar).toString();
        holder.timeTextView.setText(postedDate);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        try {
//            Date d1 = simpleDateFormat.parse(postedDate);
//            Log.d("kkk","posted date:"+d1.toString());
//            holder.timeTextView.setText(d1.toString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


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
        return myPostList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {


        private ImageView profileImageView,postImageView,profileCommentImageView;
        private TextView usernameText1,usernameText2;
        private ImageView likeImageView,saveImageView,moreImageView,commentImageView;
        private TextView captionTextView,likeTextView,timeTextView,commentTextView;


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
//            commentEditText = itemView.findViewById(R.id.commentEditText);
//            postTextView = itemView.findViewById(R.id.postButton);

        }
    }



    private void openProfile(String profileId)
    {

        SharedPreferences.Editor editor =  context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
        editor.putString("profileUID",profileId);
        editor.apply();

//        ((HomeActivity)context).getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragmentContainert,new profileFragment())
//                .commit();


        Intent intent1 = new Intent(context, HomeActivity.class);
        intent1.putExtra("publisherUid",profileId);
        context.startActivity(intent1);



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

}