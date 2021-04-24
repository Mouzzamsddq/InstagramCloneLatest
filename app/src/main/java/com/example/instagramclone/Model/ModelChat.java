package com.example.instagramclone.Model;

public class ModelChat {
    private String message;
    private String receiver;
    private String sender;
    private String timestamp;

    public ModelChat() {
    }

    public ModelChat(String sender2, String receiver2, String message2, String timestamp2) {
        this.sender = sender2;
        this.receiver = receiver2;
        this.message = message2;
        this.timestamp = timestamp2;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender2) {
        this.sender = sender2;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setReceiver(String receiver2) {
        this.receiver = receiver2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp2) {
        this.timestamp = timestamp2;
    }
}
