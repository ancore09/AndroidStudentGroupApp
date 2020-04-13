package com.example.studentappmvvm.model;

import android.graphics.Color;

import java.util.List;

public class UserEntity implements User {
    int ID;
    String Login;
    String FirstName;
    String LastName;
    String NickName;
    int memberdata_ID;
    MemberDataEntity data;
    List<Mark> marks;
    boolean isStudent = false;

    public static UserEntity sInstance;

    public static UserEntity getInstance() {
        if (sInstance == null) {
            synchronized (UserEntity.class) {
                if (sInstance == null) {
                    sInstance = new UserEntity();
                }
            }
        }
        return sInstance;
    }

    public int getID() {
        return ID;
    }

    @Override
    public boolean isStudent() {
        return isStudent;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getMemberdata_ID() {
        return memberdata_ID;
    }

    public void setMemberdata_ID(int memberdata_ID) {
        this.memberdata_ID = memberdata_ID;
    }

    @Override
    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public MemberDataEntity getMemberData() {
        return data;
    }

    public void setMemberData(MemberDataEntity data) {
        this.data = data;
    }

    @Override
    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }
}
