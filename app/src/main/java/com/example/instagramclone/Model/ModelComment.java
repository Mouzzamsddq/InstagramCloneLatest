package com.example.instagramclone.Model;

public class ModelComment {
    private String comment;
    private String commentId;
    private String postId;
    private String publisher;
    private String timestamp;

    public ModelComment() {
    }

    public ModelComment(String comment2, String publisher2, String timestamp2, String commentId2, String postId2) {
        this.comment = comment2;
        this.publisher = publisher2;
        this.timestamp = timestamp2;
        this.commentId = commentId2;
        this.postId = postId2;
    }

    public String getCommentId() {
        return this.commentId;
    }

    public void setCommentId(String commentId2) {
        this.commentId = commentId2;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment2) {
        this.comment = comment2;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher2) {
        this.publisher = publisher2;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp2) {
        this.timestamp = timestamp2;
    }

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String postId2) {
        this.postId = postId2;
    }
}
