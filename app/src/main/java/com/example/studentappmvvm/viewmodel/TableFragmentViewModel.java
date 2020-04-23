package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;

import com.evrencoskun.tableview.sort.SortState;
import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.GroupEntity;
import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;
import com.example.studentappmvvm.model.UserEntity;

import java.util.List;
import java.util.function.Function;

public class TableFragmentViewModel extends AndroidViewModel {
    private final DataRepository mRepository;
    private final MediatorLiveData<List<UserEntity>> mUsers;
    public int column = 0;
    public int row = 0;
    public SortState type = SortState.UNSORTED;

    public TableFragmentViewModel(@NonNull Application application) {
        super(application);
        mRepository = DataRepository.getInstance();
        //mRepository.postLoadUsers(1);
        mRepository.postLoadTable();

        mUsers = new MediatorLiveData<>();
        mUsers.addSource(mRepository.getUsers(), userEntities -> {
            mUsers.setValue(userEntities);
        });

        updateTable(1);
    }

    public void updateTable(int groupId) {
        mRepository.updateTable(groupId);
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
    public MutableLiveData<List<GroupEntity>> getGroups() {
        return mRepository.getGroups();
    }
}
