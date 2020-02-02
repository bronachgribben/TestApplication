package com.example.testapplication;

public class ModelGroupPlaylist {

    String groupId, groupTitle, groupIcon, timestamp, createdBy;

    public ModelGroupPlaylist() {
    }

    public ModelGroupPlaylist(String groupId, String groupTitle, String timestamp, String createdBy) {
        this.groupId = groupId;
        this.groupTitle = groupTitle;
        this.groupIcon = groupIcon;
        this.timestamp = timestamp;
        this.createdBy = createdBy;

    }

    public String getGroupId() {

        return groupId;
    }

    public void setGroupId(String groupId) {

        this.groupId = groupId;
    }

    public String getGroupTitle() {

        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {

        this.groupTitle = groupTitle;
    }

    public String getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(String timestamp) {

        this.timestamp = timestamp;
    }

    public String getCreatedBy() {

        return createdBy;
    }

    public void setCreatedBy(String createdBy) {

        this.createdBy = createdBy;
    }

    public String getGroupIcon() {

        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {

        this.groupIcon = groupIcon;
    }
}
