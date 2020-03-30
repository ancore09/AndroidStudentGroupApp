package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.NewEntity;

import java.util.List;

public class NewsListViewModel extends AndroidViewModel {
    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private final LiveData<List<NewEntity>> mNews;

    public NewsListViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandler = savedStateHandle;
        mRepository = DataRepository.getInstance();
        //mRepository.loadNews();

        mNews = mRepository.loadNews();
    }

    public LiveData<List<NewEntity>> getNews() {
        return mNews;
    }
}
