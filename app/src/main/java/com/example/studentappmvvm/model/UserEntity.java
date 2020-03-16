package com.example.studentappmvvm.model;

import java.util.List;

public class UserEntity implements User {
    String login;
    MemberDataEntity data;
    List<Mark> marks;

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public MemberData getMemberData() {
        return data;
    }

    @Override
    public List<Mark> getMarks() {
        return marks;
    }

}
