package com.example.studentappmvvm.model;

public class EvaluationEntity implements Evaluation {
    int ID;
    int lesson_id;
    int mark_id;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public int getLessonID() {
        return lesson_id;
    }

    @Override
    public int getMarkID() {
        return mark_id;
    }
}
