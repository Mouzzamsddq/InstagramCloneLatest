package com.example.instagramclone.Model;

public class ModelPost {
    private String postDescription;
    private String postId;
    private String postImage;
    private String postType;
    private String postedBy;
    private String timestamp;

    public ModelPost() {
    }

    public ModelPost(String postType2, String timestamp2, String postImage2, String postedBy2, String postDescription2, String postId2) {
        this.postType = postType2;
        this.timestamp = timestamp2;
        this.postImage = postImage2;
        this.postedBy = postedBy2;
        this.postDescription = postDescription2;
        this.postId = postId2;
    }

    public String getPostType() {
        return this.postType;
    }

    public void setPostType(String postType2) {
        this.postType = postType2;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp2) {
        this.timestamp = timestamp2;
    }

    public String getPostImage() {
        return this.postImage;
    }

    public void setPostImage(String postImage2) {
        this.postImage = postImage2;
    }

    public String getPostedBy() {
        return this.postedBy;
    }

    public void setPostedBy(String postedBy2) {
        this.postedBy = postedBy2;
    }

    public String getPostDescription() {
        return this.postDescription;
    }

    public void setPostDescription(String postDescription2) {
        this.postDescription = postDescription2;
    }

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String postId2) {
        this.postId = postId2;
    }
}
