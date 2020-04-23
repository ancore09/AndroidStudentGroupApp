package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentappmvvm.ui.AppActivity;

import java.util.function.Function;

public class TableFragmentViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Function<Void, Void> func;

    public TableFragmentViewModelFactory(Application application, Function<Void, Void> func) {
        mApplication = application;
        this.func = func;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TableFragmentViewModel(mApplication);
    }
}
