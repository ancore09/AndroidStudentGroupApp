package com.example.studentappmvvm.model;

public class MessageEntity implements Message {
    private int ID;
    private String body;
    private MemberDataEntity memberData;
    private boolean belongsToCurrentUser;
    private String fileHash;

    public MessageEntity(int id, String text, MemberDataEntity data, boolean belongsToCurrentUser) {
        this.ID = id;
        this.body = text;
        this.memberData = data;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public MemberDataEntity getMemberData() {
        return memberData;
    }

    @Override
    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getFileHash() {
        return fileHash;
    }

    @Override
    public boolean hasImage() {
        if (fileHash != null) {
            if (fileHash.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
}
