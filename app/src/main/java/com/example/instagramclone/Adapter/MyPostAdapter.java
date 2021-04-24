package com.example.instagramclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.instagramclone.Model.ModelPost;
import com.example.instagramclone.MyPostActivity;
import com.example.instagramclone.R;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public boolean isPost;
    private List<ModelPost> myPostList;

    public MyPostAdapter(Context context2, List<ModelPost> myPostList2, boolean isPost2) {
        this.context = context2;
        this.myPostList = myPostList2;
        this.isPost = isPost2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.photos_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(this.context).load(this.myPostList.get(position).getPostImage()).into(holder.postImageView);
        holder.postImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(MyPostAdapter.this.context, MyPostActivity.class);
                intent1.putExtra("isPost", MyPostAdapter.this.isPost);
                MyPostAdapter.this.context.startActivity(intent1);
            }
        });
    }

    public int getItemCount() {
        return this.myPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView postImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.postImageView = (ImageView) itemView.findViewById(R.id.postImages);
        }
    }
}
