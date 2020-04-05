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
import com.example.studentappmvvm.databinding.FragmentChatBinding;
import com.example.studentappmvvm.model.FileResponse;
import com.example.studentappmvvm.model.GroupEntity;
import com.example.studentappmvvm.model.MessageEntity;
import com.example.studentappmvvm.model.UserEntity;

import org.json.JSONException;

import java.util.List;
import java.util.function.Function;

public class ChatViewModel extends AndroidViewModel {
    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private final MediatorLiveData<List<MessageEntity>> mMessagesList;
    private final UserEntity mUser;
    private int room = 1;
    private int prevRoom = 1;

    private boolean nextMessageHasFile = false;
    private FileResponse nextMessageFileHash;

    public ChatViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandler = savedStateHandle;
        mRepository = DataRepository.getInstance();
        mRepository.postLoadMessages();
        mUser = UserEntity.getInstance();

        mMessagesList = new MediatorLiveData<>();

        mMessagesList.addSource(mRepository.getMessages(), messageEntities -> {
            mMessagesList.setValue(messageEntities);
        }); //observing livedata in repository
    }

    public void changeGroupChat(int i) {
        prevRoom = room;
        room = i;
        mRepository.changeRoom(prevRoom, room);
    }

    public MediatorLiveData<List<MessageEntity>> getMessages() {
        return mMessagesList;
    }

    public void sendMessage(MessageEntity messageEntity) throws JSONException {
        if (nextMessageHasFile) {
            messageEntity.setFileHash(nextMessageFileHash.getName());
            nextMessageHasFile = false;
        }
        mRepository.sendMessage(messageEntity, room);
    }

    public void uploadFile(String path, Function<FileResponse, Integer> func) {
        nextMessageFileHash = mRepository.uploadFile(path, func);
        nextMessageHasFile = true;
    }

    public void clearPhoto() {
        nextMessageFileHash = null;
        nextMessageHasFile = false;
    }

    public UserEntity getUser() {
        return mUser;
    }

    public LiveData<List<GroupEntity>> getGroups() {
        return mRepository.getGroups();
    }
}
