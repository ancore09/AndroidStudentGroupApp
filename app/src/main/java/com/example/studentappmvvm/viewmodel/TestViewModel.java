package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.Test;

public class TestViewModel extends AndroidViewModel {
    private final DataRepository mRepository;
    private final MediatorLiveData<Test> mTest;

    public TestViewModel(@NonNull Application application) {
        super(application);
        mRepository = DataRepository.getInstance();
        mRepository.postLoadTest();

        mTest = new MediatorLiveData<>();
        mTest.addSource(mRepository.getTest(), test -> {
            mTest.setValue(test);
        });
    }

    public MediatorLiveData<Test> getTest() {
        return mTest;
    }
}
