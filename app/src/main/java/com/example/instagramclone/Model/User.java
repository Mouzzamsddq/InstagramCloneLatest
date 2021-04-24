package com.example.instagramclone.Model;

public class User {
    private String UID;
    private String bio;
    private String dateOfBirth;
    private String email;
    private String fullName;
    private String gender;
    private String imageUri;
    private String username;

    public User() {
    }

    public User(String UID2, String email2, String fullName2, String imageUri2, String bio2, String username2, String dateOfBirth2, String gender2) {
        this.UID = UID2;
        this.email = email2;
        this.fullName = fullName2;
        this.imageUri = imageUri2;
        this.bio = bio2;
        this.username = username2;
        this.dateOfBirth = dateOfBirth2;
        this.gender = gender2;
    }

    public String getUID() {
        return this.UID;
    }

    public void setUID(String UID2) {
        this.UID = UID2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName2) {
        this.fullName = fullName2;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(String imageUri2) {
        this.imageUri = imageUri2;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio2) {
        this.bio = bio2;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username2) {
        this.username = username2;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth2) {
        this.dateOfBirth = dateOfBirth2;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender2) {
        this.gender = gender2;
    }
}
