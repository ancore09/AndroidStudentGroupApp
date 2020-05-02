package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.Test;
import com.example.studentappmvvm.ui.AnswerAdapter;

public class TestViewModel extends AndroidViewModel {
    private final DataRepository mRepository;
    private final MediatorLiveData<Test> mTest;
    private final MutableLiveData<Integer> currentQuestion;

    public TestViewModel(@NonNull Application application) {
        super(application);
        mRepository = DataRepository.getInstance();
        mRepository.postLoadTest();

        currentQuestion = new MutableLiveData<>();
        currentQuestion.setValue(0);

        mTest = new MediatorLiveData<>();
        mTest.addSource(mRepository.getTest(), test -> {
            mTest.setValue(test);
        });
    }

    public void nextQuestion() {
        if (currentQuestion.getValue() + 1 < mTest.getValue().getQuestions().size()) {
            currentQuestion.setValue(currentQuestion.getValue() + 1);
        } else {
            currentQuestion.setValue(-1);
        }
    }

    public MutableLiveData<Integer> getCurrentQuestion() {
        return currentQuestion;
    }

    public MediatorLiveData<Test> getTest() {
        return mTest;
    }
}
