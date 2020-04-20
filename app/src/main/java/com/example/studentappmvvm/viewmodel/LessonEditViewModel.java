package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.LessonEntity;

import java.util.function.Function;

public class LessonEditViewModel extends AndroidViewModel {
    private DataRepository mRepository;

    public LessonEditViewModel(@NonNull Application application) {
        super(application);

        mRepository = DataRepository.getInstance();
    }

    public void editTheme(String data, int id, Function<LessonEntity, Void> func) {
        mRepository.getLessons().getValue().forEach(lessonEntity -> {
            if (lessonEntity.getId() == id) {
                lessonEntity.setTheme(data);
            }
        });

        mRepository.editLesson(1, data, id, func);
    }

    public void editHomework(String data, int id, Function<LessonEntity, Void> func) {
        mRepository.getLessons().getValue().forEach(lessonEntity -> {
            if (lessonEntity.getId() == id) {
                lessonEntity.setHomework(data);
            }
        });

        mRepository.editLesson(2, data, id, func);
    }
}
