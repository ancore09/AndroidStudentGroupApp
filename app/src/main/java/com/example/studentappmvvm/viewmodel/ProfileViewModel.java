package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.SavedStateHandle;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.UserEntity;

public class ProfileViewModel extends AndroidViewModel {
    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private final UserEntity mUser;

    public ProfileViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mRepository = DataRepository.getInstance();
        mSavedStateHandler = savedStateHandle;
        mUser = UserEntity.getInstance();
    }

    public UserEntity getUser() {
        return mUser;
    }
}
