package com.example.studentappmvvm.model;

public class LessonEntity implements Lesson {
    String datedmy;
    String theme;
    String homework;
    String mark;
    String profcomment;
    String times;
    int ID;
    int group_id;
    String group;

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
    public String getTime() {
        return times;
    }

    @Override
    public String getMark() {
        if (mark != null) {
            return mark;
        } else {
            return "Нет оценки";
        }
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public int getGroupID() {
        return group_id;
    }

    @Override
    public String getGroupName() {
        return group;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
