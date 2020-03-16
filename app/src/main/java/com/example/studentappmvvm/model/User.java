package com.example.studentappmvvm.model;

import java.util.List;

public interface User {
    String getLogin();
    MemberData getMemberData();
    List<Mark> getMarks();
}
