package com.example.studentappmvvm.model;

public class LessonEntity implements Lesson {
    String datedmy;
    String theme;
    String homework;
    String mark;
    String profcomment;
    int ID;

    public void setDate(String date) {
        this.datedmy = date;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setId(int id) {
        this.ID = id;
    }

    @Override
    public String getDate() {
        return datedmy;
    }

    @Override
    public String getTheme() {
        return theme;
    }

    @Override
    public String getHomework() {
        return homework;
    }

    @Override
    public String getComment() {
        return profcomment;
    }

    @Override
    public String getMark() {
        return mark;
    }

    @Override
    public int getId() {
        return ID;
    }
}
