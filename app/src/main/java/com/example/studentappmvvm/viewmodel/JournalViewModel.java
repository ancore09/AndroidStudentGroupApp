package com.example.studentappmvvm.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.LessonEntity;

import java.util.List;
import androidx.arch.core.util.Function;

public class JournalViewModel extends AndroidViewModel {

    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private final LiveData<List<LessonEntity>> mLessons;

    public JournalViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandler = savedStateHandle;
        mRepository = DataRepository.getInstance();

        mLessons = Transformations.switchMap(
                savedStateHandle.getLiveData("QUERY", null),
                (Function<CharSequence, LiveData<List<LessonEntity>>>) query -> {
                    if (TextUtils.isEmpty(query)) {
                        return mRepository.getLessons();
                    }
                    return mRepository.searchLessons(""+query);
                }); //switchmap override livedata value using filtered with query list
    }

    public void setQuery(CharSequence query) {
        mSavedStateHandler.set("QUERY", query);
    }

    public LiveData<List<LessonEntity>> getLessons() {
        return mLessons;
    }
}
