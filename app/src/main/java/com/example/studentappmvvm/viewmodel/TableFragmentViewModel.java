package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.LessonEntity;

import java.util.List;

public class TableFragmentViewModel extends AndroidViewModel {
    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private final LiveData<List<LessonEntity>> mLessons;

    public TableFragmentViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mRepository = DataRepository.getInstance();
        //mRepository.postLoadJournal();
        mSavedStateHandler = savedStateHandle;

        mLessons = mRepository.getLessons();
    }

    public LiveData<List<LessonEntity>> getLessons() {
        return mLessons;
    }
}
