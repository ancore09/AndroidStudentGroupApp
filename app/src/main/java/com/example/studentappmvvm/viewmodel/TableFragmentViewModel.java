package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;

import java.util.List;

public class TableFragmentViewModel extends AndroidViewModel {
    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private final MediatorLiveData<List<Mark>> mMarks;

    public TableFragmentViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mRepository = DataRepository.getInstance();
        mRepository.postLoadtable();
        mSavedStateHandler = savedStateHandle;

        mMarks = new MediatorLiveData<>();
        mMarks.addSource(mRepository.loadTable(), marks -> {
            mMarks.setValue(marks);
        });
    }

    public MutableLiveData<List<Mark>> getMarks() {
        return mMarks;
    }
    public MutableLiveData<List<LessonEntity>> getLessons() {
        return mRepository.getLessons();
    }
}
