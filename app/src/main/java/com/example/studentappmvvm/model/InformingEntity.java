package com.example.studentappmvvm.model;

public class InformingEntity implements Informing {
    int ID;
    int New_ID;
    int Group_ID;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public int getNewID() {
        return New_ID;
    }

    @Override
    public int getGroupID() {
        return Group_ID;
    }
}
