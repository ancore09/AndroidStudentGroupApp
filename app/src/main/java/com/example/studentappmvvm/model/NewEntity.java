package com.example.studentappmvvm.model;

public class NewEntity implements New{
    String title;
    String body;
    String epilogue;
    int id;

    public int getId() {
        return id;
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
