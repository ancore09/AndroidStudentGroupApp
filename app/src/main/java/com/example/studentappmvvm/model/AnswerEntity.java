package com.example.studentappmvvm.model;

public class AnswerEntity {
    int ID;
    String answer;
    boolean isTrue;

    public AnswerEntity(int id, String answer, boolean isTrue) {
        this.isTrue = isTrue;
        this.ID = id;
        this.answer = answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }
}
