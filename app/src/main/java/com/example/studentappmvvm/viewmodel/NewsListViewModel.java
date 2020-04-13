package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.NewEntity;
import com.example.studentappmvvm.model.UserEntity;

import java.util.List;

public class NewsListViewModel extends AndroidViewModel {
    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private final MediatorLiveData<List<NewEntity>> mNews;

    public NewsListViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandler = savedStateHandle;
        mRepository = DataRepository.getInstance();
        mRepository.postLoadNews();

        mNews = new MediatorLiveData<>();
        mNews.addSource(mRepository.getNews(), newEntities -> {
            mNews.setValue(newEntities);
        }); //observing livedata from repository

        updateNews();
    }

    public MediatorLiveData<List<NewEntity>> getNews() {
        return mNews;
    }

    public void updateNews() {
        mRepository.updateNews(); //updating news in repository
        //mRepository.postLoad();
    }

    public UserEntity getUser() {
        return mRepository.getUser();
    }

}
