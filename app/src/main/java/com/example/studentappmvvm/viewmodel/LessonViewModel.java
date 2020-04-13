package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.UserEntity;

public class LessonViewModel extends AndroidViewModel {
    private final DataRepository mRepository;

    public LessonViewModel(@NonNull Application application) {
        super(application);
        mRepository = DataRepository.getInstance();
    }

    public void editLesson() {

    }

    public UserEntity getUser() {
        return mRepository.getUser();
    }
}
