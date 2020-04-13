package com.example.studentappmvvm.model;

import java.util.List;

public interface User {
    int getID();
    boolean isStudent();
    String getLogin();
    String getFirstName();
    String getLastName();
    String getNickName();
    MemberData getMemberData();
    int getMemberdata_ID();
    List<Mark> getMarks();
}
