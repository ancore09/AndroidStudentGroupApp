package com.example.studentappmvvm.model;

import java.util.List;

public class QuestionEntity {
    int ID;
    int number;
    String question;
    List<AnswerEntity> answers;

    public QuestionEntity(int id, int number, String question) {
        this.ID = id;
        this.number = number;
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setAnswers(List<AnswerEntity> answers) {
        this.answers = answers;
    }

    public List<AnswerEntity> getAnswers() {
        return answers;
    }
}
