package com.example.studentappmvvm.model;

public class MessageEntity implements Message {
    private String body;
    private MemberDataEntity memberData;
    private boolean belongsToCurrentUser;

    public MessageEntity(String text, MemberDataEntity data, boolean belongsToCurrentUser) {
        this.body = text;
        this.memberData = data;
        this.belongsToCurrentUser = belongsToCurrentUser;
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
}
