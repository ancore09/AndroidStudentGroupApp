package com.example.studentappmvvm.model;

public interface Message {
    String getBody();
    MemberDataEntity getMemberData();
    boolean isBelongsToCurrentUser();
}
