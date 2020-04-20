package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.FileResponse;
import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.NewEntity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class LessonAdditionViewModel extends AndroidViewModel {
    private final DataRepository mRepository;

    public LessonAdditionViewModel(@NonNull Application application) {
        super(application);
        mRepository = DataRepository.getInstance();
    }

    public void postLesson(String groupName, String date, String time, String theme, String homework, String comment, Function<LessonEntity, Void> func) {
        AtomicInteger id = new AtomicInteger();
        id.set(-1);
        mRepository.getGroups().getValue().forEach(groupEntity -> {
            if (groupEntity.getName().equals(groupName)) {
                id.set(groupEntity.getID());
            }
        });
        if (id.get() != -1) {
            mRepository.postLesson(id.get(), date, time, theme, homework, comment, func);
        } else {
            func.apply(null);
        }
    }
}
