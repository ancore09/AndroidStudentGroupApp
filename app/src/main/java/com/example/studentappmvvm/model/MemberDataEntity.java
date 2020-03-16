package com.example.studentappmvvm.model;

public class MemberDataEntity implements MemberData {
    private String name;
    private String color;
    private String course;

    public MemberDataEntity(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public MemberDataEntity(String name, String color, String course) {
        this(name, color);
        this.course = course;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getCourse() {
        return course;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
