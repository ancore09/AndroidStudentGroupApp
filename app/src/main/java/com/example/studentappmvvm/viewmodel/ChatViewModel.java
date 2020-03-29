package com.example.studentappmvvm.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.model.MessageEntity;
import com.example.studentappmvvm.model.UserEntity;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private final MediatorLiveData<List<MessageEntity>> mMessagesList;
    private final UserEntity mUser;

    public ChatViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandler = savedStateHandle;
        mRepository = DataRepository.getInstance();
        mUser = UserEntity.getInstance();

        mMessagesList = new MediatorLiveData<>();
        //mMessagesList.setValue(mRepository.getMessages().getValue());
//        mRepository.getMessages().observeForever(messageEntities -> {
//            mMessagesList.setValue(messageEntities);
//        });

        mMessagesList.addSource(mRepository.getMessages(), messageEntities -> {
            mMessagesList.setValue(messageEntities);
            //mMessagesList.setValue(mRepository.getMessages().getValue());
            Log.d("VIEWMODEL", "OBSERVED REPO IN MODEL");
        });
    }

    public MediatorLiveData<List<MessageEntity>> getMessages() {
        return mMessagesList;
    }

    public void sendMessage(MessageEntity messageEntity) {
        mRepository.sendMessage(messageEntity);
    }

    public void uploadFile(String path) {
        mRepository.uploadFile(path);
    }

    public UserEntity getUser() {
        return mUser;
    }
}
