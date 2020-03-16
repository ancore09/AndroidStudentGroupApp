package com.example.studentappmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.MessageEntity;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private final MutableLiveData<List<MessageEntity>> mMessagesList;

    public ChatViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandler = savedStateHandle;
        mRepository = DataRepository.getInstance();

        mMessagesList = new MutableLiveData<>();
        mMessagesList.setValue(mRepository.getMessages().getValue());
        mRepository.getMessages().observeForever(messageEntities -> {
            mMessagesList.setValue(messageEntities);
        });
    }

    public LiveData<List<MessageEntity>> getMessages() {
        return mMessagesList;
    }

    public void sendMessage(MessageEntity messageEntity) {
        mRepository.sendMessage(messageEntity);
    }
}
