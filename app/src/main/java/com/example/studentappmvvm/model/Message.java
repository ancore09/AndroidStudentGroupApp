package com.example.studentappmvvm.model;

public interface Message {
    int getId();
    String getBody();
    MemberDataEntity getMemberData();
    boolean isBelongsToCurrentUser();
}
