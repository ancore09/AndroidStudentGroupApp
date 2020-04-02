package com.example.studentappmvvm.model;

public class GroupingEntity implements Grouping {
    int ID;
    int Group_ID;
    int User_ID;
    @Override
    public int getID() {
        return ID;
    }

    @Override
    public int getUserID() {
        return User_ID;
    }

    @Override
    public int getGroupID() {
        return Group_ID;
    }
}
