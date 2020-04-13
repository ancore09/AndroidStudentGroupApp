package com.example.studentappmvvm.model;

public class NewEntity implements New{
    String datedmy;
    String title;
    String body;
    String epilogue;
    String filehash;
    String group;
    int ID;

    public NewEntity(String datedmy, String title, String body, String epilogue, String filehash, String groupName) {
        this.datedmy = datedmy;
        this.title = title;
        this.body = body;
        this.epilogue = epilogue;
        this.filehash = filehash;
        this.group = groupName;
    }

    public int getId() {
        return ID;
    }

    @Override
    public String getGroupName() {
        return group;
    }

    public void setGroupName(String groupName) {
        this.group = groupName;
    }

    public String getDateDMY() {
        return datedmy;
    }

    public String getFileHash() {
        return filehash;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getEpilogue() {
        return epilogue;
    }

    public boolean hasImage() {
        return filehash != null;
    }
}
