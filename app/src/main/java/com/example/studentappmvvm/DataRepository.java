package com.example.studentappmvvm;

import android.database.Cursor;
import android.net.Uri;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;
import com.example.studentappmvvm.model.MemberDataEntity;
import com.example.studentappmvvm.model.MessageEntity;
import com.example.studentappmvvm.model.NewEntity;
import com.example.studentappmvvm.model.User;
import com.example.studentappmvvm.model.UserEntity;
import com.example.studentappmvvm.ui.ChatFragment;
import com.example.studentappmvvm.ui.MainActivity;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.ResponseBody;


import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private MutableLiveData<List<MessageEntity>> mObservableMessages;
    private LiveData<List<Mark>> mObservableMarks;

    private UserEntity mUser;

    private DataRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.129:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ws = retrofit.create(Webservice.class);
        firstLoad();
        //mObservableNews = loadNews();
        //mObservableLessons = loadJournal();
        //mObservableMessages = loadMessages();
        //mObservableMarks = loadMarks();
        //mUser = UserEntity.getInstance();

        mSocket.on("message", args -> {
            Gson gson = new Gson();
            MessageEntity msg;
            try {
                msg = gson.fromJson(args[0].toString(), MessageEntity.class);
                mObservableMessages.getValue().add(msg);
                mObservableMessages.postValue(mObservableMessages.getValue());
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

    public void firstLoad() {
        mUser = UserEntity.getInstance();
        mObservableNews = loadNews();
        mObservableLessons = loadJournal();
        mObservableMessages = loadMessages();
        mObservableMarks = loadMarks();
        mSocket.connect();
    }

    public void sendMessage(MessageEntity messageEntity) {
//        mObservableMessages.getValue().add(messageEntity);
//        mObservableMessages.setValue(mObservableMessages.getValue());
        Gson gson = new Gson();
        String json = gson.toJson(messageEntity);
        //mObservableMessages.getValue().add(messageEntity);
        mSocket.emit("message", json);
    }

    public LiveData<List<LessonEntity>> searchLessons(String query) {
        List<LessonEntity> filteredList = mObservableLessons.getValue().stream().filter(lessonEntity -> lessonEntity.getDate().contains(query)).collect(Collectors.toList());
        MutableLiveData<List<LessonEntity>> data = new MutableLiveData<>();
        data.setValue(filteredList);
        return data;
    }

    public void authUser(String login, String hash, Function<UserEntity, Integer> func) {
        ws.authUser(login, hash).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                mUser.setID(response.body().getID());
                mUser.setFirstName(response.body().getFirstName());
                mUser.setLastName(response.body().getLastName());
                mUser.setNickName(response.body().getNickName());
                mUser.setLogin(response.body().getLogin());
                mUser.setMemberdata_ID(response.body().getMemberdata_ID());
                func.apply(mUser);

                ws.getMemberData(mUser.getMemberdata_ID()).enqueue(new Callback<MemberDataEntity>() {
                    @Override
                    public void onResponse(Call<MemberDataEntity> call, Response<MemberDataEntity> response) {
                        response.body().setColor(ChatFragment.getRandomColor());
                        mUser.setMemberData(response.body());
                    }

                    @Override
                    public void onFailure(Call<MemberDataEntity> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                mUser = null;
                func.apply(null);
            }
        });

//        ws.getMemberData(mUser.getMemberdata_ID()).enqueue(new Callback<MemberDataEntity>() {
//            @Override
//            public void onResponse(Call<MemberDataEntity> call, Response<MemberDataEntity> response) {
//                mUser.setMemberData(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<MemberDataEntity> call, Throwable t) {
//
//            }
//        });
    }

    public void uploadFile(String path) {
        File file = new File(path);

        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fbody);

        ws.uploadFile(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<List<Mark>> loadMarks() {
        MutableLiveData<List<Mark>> data = new MutableLiveData<>();

        return data;
    }

    public LiveData<List<NewEntity>> loadNews() {
        MutableLiveData<List<NewEntity>> data = new MutableLiveData<>();
        ws.getNews(1).enqueue(new Callback<List<NewEntity>>() {
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

    public MutableLiveData<List<MessageEntity>> loadMessages() {
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

    public LiveData<List<LessonEntity>> loadJournal() {
        MutableLiveData<List<LessonEntity>> data = new MutableLiveData<>();
        ws.getLessons(1, mUser.getID()).enqueue(new Callback<List<LessonEntity>>() {
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

    public LiveData<List<NewEntity>> getNews() {
        return mObservableNews;
    }
    public LiveData<List<LessonEntity>> getLessons() {
        return mObservableLessons;
    }
    public MutableLiveData<List<MessageEntity>> getMessages() {
        return mObservableMessages;
    }
    public LiveData<List<Mark>> getMarks() {
        return mObservableMarks;
    }
    public UserEntity getUser() {
        return mUser;
    }
}
