package com.example.studentappmvvm.model;

import android.graphics.Color;

public class MemberDataEntity implements MemberData {
    private int ID;
    private String nick;
    private String color;
    private String course;

    public MemberDataEntity(String name, String color) {
        this.nick = name;
        this.color = color;
    }

    public MemberDataEntity(String name, String color, String course) {
        this(name, color);
        this.course = course;
    }

    @Override
    public String getName() {
        return nick;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String getColor() {
        return color;
    }

    public int getActColor() {
        return Color.parseColor(color);
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getCourse() {
        return course;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name='" + nick + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
