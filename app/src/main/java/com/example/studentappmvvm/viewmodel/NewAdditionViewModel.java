package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.FileResponse;
import com.example.studentappmvvm.model.NewEntity;

import java.util.function.Function;

public class NewAdditionViewModel extends AndroidViewModel {
    private final DataRepository mRepository;
    private boolean nextNewHasFile = false;
    private FileResponse nextNewFileHash;

    public NewAdditionViewModel(@NonNull Application application) {
        super(application);
        mRepository = DataRepository.getInstance();
    }

    public void postNew(String groupName, String date, String title, String body, String epil, Function<NewEntity, Integer> func) {
        if (nextNewHasFile) {
            mRepository.postNew(groupName, date, title, body, epil, nextNewFileHash.getName(), func);
        } else {
            mRepository.postNew(groupName, date, title, body, epil, "null", func);
        }
    }
    public void uploadFile(String path, Function<FileResponse, Integer> func) {
        nextNewFileHash = mRepository.uploadFile(path, func);
        nextNewHasFile = true;
    }

}
