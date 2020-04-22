package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;
import com.example.studentappmvvm.model.UserEntity;

import java.util.List;
import java.util.function.Function;

public class TableFragmentViewModel extends AndroidViewModel {
    private final DataRepository mRepository;
    private final MediatorLiveData<List<UserEntity>> mUsers;

    public TableFragmentViewModel(@NonNull Application application, Function<Void, Void> func) {
        super(application);
        mRepository = DataRepository.getInstance();
        //mRepository.postLoadUsers(1);
        mRepository.postLoadTable();

        mUsers = new MediatorLiveData<>();
        mUsers.addSource(mRepository.getUsers(), userEntities -> {
            mUsers.setValue(userEntities);
        });

        updateTable();
    }

    public void updateTable() {
        mRepository.updateTable();
    }

    public void setMark(int col, int row, String data) {
        mRepository.setMark(col, row, data);
    }

    public MutableLiveData<List<UserEntity>> getUsers() {
        return mUsers;
    }
    public MutableLiveData<List<LessonEntity>> getLessons() {
        return mRepository.getLessons();
    }
}
