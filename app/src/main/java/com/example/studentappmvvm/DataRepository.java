package com.example.studentappmvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentappmvvm.model.EvaluationEntity;
import com.example.studentappmvvm.model.FileResponse;
import com.example.studentappmvvm.model.GroupEntity;
import com.example.studentappmvvm.model.GroupingEntity;
import com.example.studentappmvvm.model.InformingEntity;
import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;
import com.example.studentappmvvm.model.MemberDataEntity;
import com.example.studentappmvvm.model.MessageEntity;
import com.example.studentappmvvm.model.NewEntity;
import com.example.studentappmvvm.model.UserEntity;
import com.example.studentappmvvm.ui.ChatFragment;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.squareup.okhttp.ResponseBody;


import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
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
    private MutableLiveData<List<NewEntity>> mObservableNews;
    private MutableLiveData<List<LessonEntity>> mObservableLessons;
    private MutableLiveData<List<MessageEntity>> mObservableMessages;
    private MutableLiveData<List<GroupEntity>> mGroups;
    private UserEntity mUser;

    private DataRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.129:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ws = retrofit.create(Webservice.class);
        firstLoad();

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
        }); //listener for  "message" event
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
    } //there is only one instance of repository in the app




    public void firstLoad() {
        mUser = UserEntity.getInstance(); //instance of user required for login
    }

    public void postLoadNews() {
        mObservableNews = loadNews(listMutableLiveData -> 0, getGroupIds());
    }

    public void postLoadJournal() {
        mObservableLessons = loadJournal(getGroupIds());
    }

    public void postLoadMessages() {
        mObservableMessages = loadMessages();
        mSocket.connect();
    }


    private int[] getGroupIds() {
        int[] groupids = new int[mGroups.getValue().size()];
        for (int i = 0; i < groupids.length; i++) {
            groupids[i] = mGroups.getValue().get(i).getID();
        }
        return groupids;
    }

    public void authUser(String login, String hash, Function<UserEntity, Integer> func) {
        mGroups = new MutableLiveData<>();
        ws.authUser(login, hash).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                mUser.setID(response.body().getID());
                mUser.setFirstName(response.body().getFirstName());
                mUser.setLastName(response.body().getLastName());
                mUser.setNickName(response.body().getNickName());
                mUser.setLogin(response.body().getLogin());
                mUser.setMemberdata_ID(response.body().getMemberdata_ID());

                ws.getMemberData(mUser.getMemberdata_ID()).enqueue(new Callback<MemberDataEntity>() {
                    @Override
                    public void onResponse(Call<MemberDataEntity> call, Response<MemberDataEntity> response) {
                        //response.body().setColor(ChatFragment.getRandomColor());
                        mUser.setMemberData(response.body());
                    }

                    @Override
                    public void onFailure(Call<MemberDataEntity> call, Throwable t) {

                    }
                });

                ws.getGrouping(mUser.getID()).enqueue(new Callback<List<GroupingEntity>>() {
                    @Override
                    public void onResponse(Call<List<GroupingEntity>> call, Response<List<GroupingEntity>> response) {
                        int[] ids = new int[response.body().size()];
                        for (int i = 0; i < ids.length; i++) {
                            ids[i] = response.body().get(i).getGroupID();
                        }
                        ws.getGroup(ids).enqueue(new Callback<List<GroupEntity>>() {
                            @Override
                            public void onResponse(Call<List<GroupEntity>> call, Response<List<GroupEntity>> response) {
                                mGroups.postValue(response.body());
                                func.apply(mUser);
                            }

                            @Override
                            public void onFailure(Call<List<GroupEntity>> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<GroupingEntity>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                func.apply(null);
            }
        });
    } //trying to get user and its memberdata and groups from server


    public void updateNews() {
        loadNews(listMutableLiveData -> {
            mObservableNews.setValue(listMutableLiveData.getValue());
            return 1;
        }, getGroupIds());
    } //updating news using callback

    public MutableLiveData<List<NewEntity>> loadNews(Function<MutableLiveData<List<NewEntity>>, Integer> func, int[] groupIds) {
        MutableLiveData<List<NewEntity>> data = new MutableLiveData<>();
        ws.getNews(groupIds).enqueue(new Callback<List<NewEntity>>() {
            @Override
            public void onResponse(Call<List<NewEntity>> call, Response<List<NewEntity>> response) {
                data.setValue(response.body());

                ws.getInforming(groupIds).enqueue(new Callback<List<InformingEntity>>() {
                    @Override
                    public void onResponse(Call<List<InformingEntity>> call, Response<List<InformingEntity>> responseInforming) {
//                        responseInforming.body().forEach(informingEntity -> {
//                            data.getValue().forEach(newEntity -> {
//                                mGroups.getValue().forEach(groupEntity -> {
//                                    if (informingEntity.getNewID() == newEntity.getId() && informingEntity.getGroupID() == groupEntity.getID()) {
//                                        newEntity.setGroupName(groupEntity.getName());
//                                    }
//                                });
//                            });
//                        });
                        data.getValue().forEach(newEntity -> {
                            mGroups.getValue().forEach(groupEntity -> {
                                responseInforming.body().forEach(informingEntity -> {
                                    if (informingEntity.getNewID() == newEntity.getId() && informingEntity.getGroupID() == groupEntity.getID()) {
                                        newEntity.setGroupName(groupEntity.getName());
                                    }
                                });
                            });
                        });

                        func.apply(data);
                    }

                    @Override
                    public void onFailure(Call<List<InformingEntity>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<NewEntity>> call, Throwable t) {

            }
        });
        return data;
    } //loading/updating news from server


    public void sendMessage(MessageEntity messageEntity) {
        Gson gson = new Gson();
        String json = gson.toJson(messageEntity);
        mSocket.emit("message", json);
    }

    public FileResponse uploadFile(String path, Function<FileResponse, Integer> func) {
        File file = new File(path);
        FileResponse name = new FileResponse();

        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fbody);

        ws.uploadFile(body).enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                name.setName(response.body().getName());
                func.apply(response.body());
            }

            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return name;
    } //single file/photo uploading

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
    } //loading chat messages from server


    public MutableLiveData<List<LessonEntity>> searchLessons(String query) {
        List<LessonEntity> filteredList = mObservableLessons.getValue().stream().filter(lessonEntity -> lessonEntity.getDate().contains(query)).collect(Collectors.toList());
        MutableLiveData<List<LessonEntity>> data = new MutableLiveData<>();
        data.setValue(filteredList);
        return data;
    } //filtering lessons list with query

    public MutableLiveData<List<LessonEntity>> loadJournal(int[] group_id) {
        MutableLiveData<List<LessonEntity>> data = new MutableLiveData<>();
        ws.getLessons(group_id, mUser.getID()).enqueue(new Callback<List<LessonEntity>>() {
            @Override
            public void onResponse(Call<List<LessonEntity>> call, Response<List<LessonEntity>> response) {
                data.setValue(response.body());

//                ws.getGroup(group_id).enqueue(new Callback<List<GroupEntity>>() {
//                    @Override
//                    public void onResponse(Call<List<GroupEntity>> call, Response<List<GroupEntity>> response) {
//                        data.getValue().forEach(lessonEntity -> {
//                            response.body().forEach(groupEntity -> {
//                                if (lessonEntity.getGroupID() == groupEntity.getID()) {
//                                    lessonEntity.setGroup(groupEntity.getName());
//                                }
//                            });
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<GroupEntity>> call, Throwable t) {
//
//                    }
//                });

                data.getValue().forEach(lessonEntity -> {
                    mGroups.getValue().forEach(groupEntity -> {
                        if (lessonEntity.getGroupID() == groupEntity.getID()) {
                            lessonEntity.setGroup(groupEntity.getName());
                        }
                    });
                });

                ws.getEvaluation(mUser.getLogin()).enqueue(new Callback<List<EvaluationEntity>>() {
                    @Override
                    public void onResponse(Call<List<EvaluationEntity>> call, Response<List<EvaluationEntity>> responseEvaliation) {
                        int[] ids = new int[responseEvaliation.body().size()];
                        for (int i = 0; i < ids.length; i++) {
                            ids[i] = responseEvaliation.body().get(i).getLessonID();
                        }
                        ws.getMarks(mUser.getLogin(), ids).enqueue(new Callback<List<Mark>>() {
                            @Override
                            public void onResponse(Call<List<Mark>> call, Response<List<Mark>> responseMark) {
                                responseEvaliation.body().forEach(evaluationEntity -> {
                                    data.getValue().forEach(lessonEntity -> {
                                        responseMark.body().forEach(mark -> {
                                            if (evaluationEntity.getLessonID() == lessonEntity.getId() && evaluationEntity.getMarkID() == mark.getId()) {
                                                lessonEntity.setMark(mark.getMark());
                                            }
                                        });
                                    });
                                });
                            }

                            @Override
                            public void onFailure(Call<List<Mark>> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<EvaluationEntity>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<LessonEntity>> call, Throwable t) {

            }
        });
        return data;
    } //loading/updating journal(lessons) and marks from server

    public MutableLiveData<List<NewEntity>> getNews() {
        return mObservableNews;
    }
    public MutableLiveData<List<LessonEntity>> getLessons() {
        return mObservableLessons;
    }
    public MutableLiveData<List<MessageEntity>> getMessages() {
        return mObservableMessages;
    }
    public UserEntity getUser() {
        return mUser;
    }
}
