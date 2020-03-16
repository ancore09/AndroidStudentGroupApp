package com.example.studentappmvvm.model;

public class LessonEntity implements Lesson {
    String date;
    String theme;
    String homework;
    String mark;
    int id;

    public void setDate(String date) {
        this.date = date;
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
        this.id = id;
    }

    @Override
    public String getDate() {
        return date;
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
    public String getMark() {
        return mark;
    }

    @Override
    public int getId() {
        return id;
    }
}
