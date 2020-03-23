package com.example.studentappmvvm.model;

public class NewEntity implements New{
    String datedmy;
    String title;
    String body;
    String epilogue;
    int ID;

    public int getId() {
        return ID;
    }

    public String getDateDMY() {
        return datedmy;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getEpilogue() {
        return epilogue;
    }
}
