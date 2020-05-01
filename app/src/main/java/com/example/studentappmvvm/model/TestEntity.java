package com.example.studentappmvvm.model;

import java.util.List;

public class TestEntity implements Test {
    int ID;
    String description;
    List<QuestionEntity> questions;

    public TestEntity(int id, String desc) {
        this.ID = id;
        this.description = desc;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public List<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
