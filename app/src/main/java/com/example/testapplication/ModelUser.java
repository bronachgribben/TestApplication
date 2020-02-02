package com.example.testapplication;

public class ModelUser {

    String image, name, uid, status, sendTo;
    boolean isBlocked = false;

    public ModelUser() {
    }

    public ModelUser(String image, String name, String uid, String status, String sendTo, boolean isBlocked) {
        this.image = image;
        this.name = name;
        this.uid = uid;
        this.status = status;
        this.sendTo = sendTo;
        this.isBlocked = isBlocked;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
