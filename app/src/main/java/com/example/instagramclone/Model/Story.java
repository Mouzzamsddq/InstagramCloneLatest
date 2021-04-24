package com.example.instagramclone.Model;

public class Story {
    private String imageUri;
    private String storyId;
    private long timesEnd;
    private long timestart;
    private String userId;

    public Story(String imageUri2, long timestart2, long timesEnd2, String storyId2, String userId2) {
        this.imageUri = imageUri2;
        this.timestart = timestart2;
        this.timesEnd = timesEnd2;
        this.storyId = storyId2;
        this.userId = userId2;
    }

    public Story() {
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(String imageUri2) {
        this.imageUri = imageUri2;
    }

    public long getTimestart() {
        return this.timestart;
    }

    public void setTimestart(long timestart2) {
        this.timestart = timestart2;
    }

    public long getTimesEnd() {
        return this.timesEnd;
    }

    public void setTimesEnd(long timesEnd2) {
        this.timesEnd = timesEnd2;
    }

    public String getStoryId() {
        return this.storyId;
    }

    public void setStoryId(String storyId2) {
        this.storyId = storyId2;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }
}
