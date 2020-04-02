package com.example.studentappmvvm.model;

public class GroupEntity implements Group {
    int ID;
    String NameInfo;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getName() {
        return NameInfo;
    }
}
