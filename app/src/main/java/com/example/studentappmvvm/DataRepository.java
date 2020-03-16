package com.example.studentappmvvm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;
import com.example.studentappmvvm.model.MessageEntity;
import com.example.studentappmvvm.model.NewEntity;
import com.example.studentappmvvm.model.User;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataRepository {

    private Webservice ws;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.129:3000?room=c");
        } catch (URISyntaxException e) {}
    }

    private static DataRepository sInstance;
    private LiveData<List<NewEntity>> mObservableNews;
    private LiveData<List<LessonEntity>> mObservableLessons;
    private LiveData<List<MessageEntity>> mObservableMessages;
    private LiveData<List<Mark>> mObservableMarks;

    private DataRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.129:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ws = retrofit.create(Webservice.class);
        mObservableNews = loadNews();
        mObservableLessons = loadJournal();
        mObservableMessages = loadMessages();
        mObservableMarks = loadMarks();

        mSocket.on("message", args -> {
            Gson gson = new Gson();
            MessageEntity msg;
            try {
                msg = gson.fromJson(args[0].toString(), MessageEntity.class);
                mObservableMessages.getValue().add(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mSocket.connect();
    }

    public static DataRepository getInstance() {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository();
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Mark>> loadMarks() {
        MutableLiveData<List<Mark>> data = new MutableLiveData<>();

        return data;
    }

    public LiveData<List<NewEntity>> loadNews() {
        MutableLiveData<List<NewEntity>> data = new MutableLiveData<>();
        ws.getNews().enqueue(new Callback<List<NewEntity>>() {
            @Override
            public void onResponse(Call<List<NewEntity>> call, Response<List<NewEntity>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<NewEntity>> call, Throwable t) {

            }
        });
        return data;
    }

    public LiveData<List<MessageEntity>> loadMessages() {
        MutableLiveData<List<MessageEntity>> data = new MutableLiveData<>();
        List<MessageEntity> failData = new ArrayList<>();
        ws.getMessages().enqueue(new Callback<List<MessageEntity>>() {
            @Override
            public void onResponse(Call<List<MessageEntity>> call, Response<List<MessageEntity>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<MessageEntity>> call, Throwable t) {
                data.setValue(failData);
            }
        });
        return data;
    }

    public void sendMessage(MessageEntity messageEntity) {
        Gson gson = new Gson();
        String json = gson.toJson(messageEntity);
        //mObservableMessages.getValue().add(messageEntity);
        mSocket.emit("message", json);
    }

    public LiveData<List<LessonEntity>> loadJournal() {
        MutableLiveData<List<LessonEntity>> data = new MutableLiveData<>();
        ws.getLessons().enqueue(new Callback<List<LessonEntity>>() {
            @Override
            public void onResponse(Call<List<LessonEntity>> call, Response<List<LessonEntity>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<LessonEntity>> call, Throwable t) {

            }
        });
        return data;
    }

    public LiveData<List<LessonEntity>> searchLessons(String query) {
        List<LessonEntity> filteredList = mObservableLessons.getValue().stream().filter(lessonEntity -> lessonEntity.getDate().contains(query)).collect(Collectors.toList());
        MutableLiveData<List<LessonEntity>> data = new MutableLiveData<>();
        data.setValue(filteredList);
        return data;
    }

    public LiveData<List<NewEntity>> getNews() {
        return mObservableNews;
    }
    public LiveData<List<LessonEntity>> getLessons() {
        return mObservableLessons;
    }
    public LiveData<List<MessageEntity>> getMessages() {
        return mObservableMessages;
    }
    public LiveData<List<Mark>> getMarks() {
        return mObservableMarks;
    }
}
