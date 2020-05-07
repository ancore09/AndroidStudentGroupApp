package com.example.studentappmvvm;

import androidx.lifecycle.MutableLiveData;

import com.example.studentappmvvm.model.AnswerEntity;
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
import com.example.studentappmvvm.model.QuestionEntity;
import com.example.studentappmvvm.model.Test;
import com.example.studentappmvvm.model.TestEntity;
import com.example.studentappmvvm.model.UserEntity;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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
//    {
//        try {
//            mSocket = IO.socket("http://192.168.1.129:3000?room=");
//        } catch (URISyntaxException e) {}
//    }

    private static DataRepository sInstance;
    private MutableLiveData<List<NewEntity>> mObservableNews;
    private MutableLiveData<List<LessonEntity>> mObservableLessons;
    private MutableLiveData<List<UserEntity>> mObservableUsers;
    private MutableLiveData<List<MessageEntity>> mObservableMessages;
    private MutableLiveData<List<GroupEntity>> mGroups = new MutableLiveData<>();
    private MutableLiveData<Test> mObservableTest;
    private UserEntity mUser;

    private DataRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://194.67.92.182:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ws = retrofit.create(Webservice.class);
        firstLoad();
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
        mObservableNews = loadNews(listMutableLiveData -> null, getGroupIds());
    }

    public void postLoadJournal() {
        mObservableLessons = loadJournal(getGroupIds());
    }

    public void postLoadTable() {
        mObservableUsers = loadTable(1, listMutableLiveData -> null);
    }

    public void postLoadUsers(int groupId) {
        mObservableUsers = loadUsers(groupId);
    }

    public void postLoadTest() {
        mObservableTest = loadTest();
    }

    public void postLoadMessages(String room) {
        String roomNirm = room.replace(" ", "");
        mObservableMessages = loadMessages(roomNirm, listMutableLiveData -> null);
        try {
            //roomNirm = "c";
            mSocket = IO.socket("http://194.67.92.182:3000?room=" + roomNirm);
        } catch (URISyntaxException e) {}

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

        mSocket.connect();
    }

    public void changeRoom(String prev_id, String new_id) {
        String roomNirm = new_id.replace(" ", "");
        loadMessages(roomNirm, listMutableLiveData -> {
            mObservableMessages.setValue(listMutableLiveData.getValue());
            return null;
        });
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("prev_id", prev_id.replace(" ", ""));
            jsonObject.put("new_id", new_id.replace(" ", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("changeRoom", jsonObject);
    }


    private int[] getGroupIds() {
        int[] groupIds = new int[mGroups.getValue().size()];
        for (int i = 0; i < groupIds.length; i++) {
            groupIds[i] = mGroups.getValue().get(i).getID();
        }
        return groupIds;
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

                ws.getMemberData(mUser.getMemberdata_ID()).enqueue(new Callback<MemberDataEntity>() {
                    @Override
                    public void onResponse(Call<MemberDataEntity> call, Response<MemberDataEntity> response) {
                        //response.body().setColor(ChatFragment.getRandomColor());
                        mUser.setMemberData(response.body());

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
                    public void onFailure(Call<MemberDataEntity> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                func.apply(null);
            }
        });
    } //trying to get user and its memberdata and groups from server

    public void getGroups(Function<Integer, Integer> func) {
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
                        func.apply(1);
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

    public MutableLiveData<List<UserEntity>> loadUsers(int groupId) {
        MutableLiveData<List<UserEntity>> data = new MutableLiveData<>();

        ws.getUsers(groupId).enqueue(new Callback<List<UserEntity>>() {
            @Override
            public void onResponse(Call<List<UserEntity>> call, Response<List<UserEntity>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<UserEntity>> call, Throwable t) {

            }
        });

        return data;
    }


    public void updateNews() {
        loadNews(listMutableLiveData -> {
            mObservableNews.setValue(listMutableLiveData.getValue());
            return null;
        }, getGroupIds());
    } //updating news using callback

    public void updateTable(int groupId) {
        loadTable(groupId, listMutableLiveData -> {
            mObservableUsers.setValue(listMutableLiveData.getValue());
            return null;
        });
    }

    public MutableLiveData<List<NewEntity>> loadNews(Function<MutableLiveData<List<NewEntity>>, Void> func, int[] groupIds) {
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

    public NewEntity postNew(String groupName, String date, String title, String body, String epil, String filehash, Function<NewEntity, Integer> func) {
        NewEntity postItem = new NewEntity(date, title, body, epil, filehash, groupName);
        int id = 0;
        for (int i = 0; i < mGroups.getValue().size(); i++) {
            if (mGroups.getValue().get(i).getName().equals(groupName)) {
                id = mGroups.getValue().get(i).getID();
            }
        }
        ws.postNew(id, date, title, body, epil, filehash).enqueue(new Callback<NewEntity>() {
            @Override
            public void onResponse(Call<NewEntity> call, Response<NewEntity> response) {
                func.apply(postItem);
            }

            @Override
            public void onFailure(Call<NewEntity> call, Throwable t) {
                func.apply(null);
            }
        });
        return postItem;
    }


    public void sendMessage(MessageEntity messageEntity, String room) throws JSONException {
        Gson gson = new Gson();
        String json = gson.toJson(messageEntity);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mes", json);
        //room = "c";
        jsonObject.put("room", room.replace(" ", ""));
        mSocket.emit("message", jsonObject);
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

    public MutableLiveData<List<MessageEntity>> loadMessages(String room, Function<MutableLiveData<List<MessageEntity>>, Void> func) {
        MutableLiveData<List<MessageEntity>> data = new MutableLiveData<>();
        List<MessageEntity> failData = new ArrayList<>();
        ws.getMessages(room).enqueue(new Callback<List<MessageEntity>>() {
            @Override
            public void onResponse(Call<List<MessageEntity>> call, Response<List<MessageEntity>> response) {
                ArrayList<MessageEntity> messageEntities = new ArrayList<>();
                response.body().forEach(messageEntity -> {
                    if (messageEntity.getMemberData().getName().equals(mUser.getMemberData().getName())) {
                        messageEntity.setBelongsToCurrentUser(true);
                        messageEntities.add(messageEntity);
                    } else {
                        messageEntity.setBelongsToCurrentUser(false);
                        messageEntities.add(messageEntity);
                    }
                });
                data.setValue(messageEntities);
                func.apply(data);
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

    public MutableLiveData<List<UserEntity>> loadTable(int groupId, Function<MutableLiveData<List<UserEntity>>, Void> func) {
        MutableLiveData<List<UserEntity>> data = new MutableLiveData<>();

        ws.getUsers(groupId).enqueue(new Callback<List<UserEntity>>() {
            @Override
            public void onResponse(Call<List<UserEntity>> call, Response<List<UserEntity>> response) {
                data.setValue(response.body());

                data.getValue().forEach(userEntity -> {
                    ws.getMarksForTable(userEntity.getLogin(), groupId).enqueue(new Callback<List<Mark>>() {
                        @Override
                        public void onResponse(Call<List<Mark>> call, Response<List<Mark>> responseMarks) {
                            userEntity.setMarks(responseMarks.body());
                            /*if (data.getValue().indexOf(userEntity) == data.getValue().size()-1) {
                                func.apply(null);
                            }*/
                            AtomicBoolean loaded = new AtomicBoolean(false);
                            data.getValue().forEach(userEntity1 -> {
                                if (userEntity1.getMarks() == null) {
                                    loaded.set(false);
                                } else {
                                    loaded.set(true);
                                }
                            });
                            if (loaded.get()) {
                                func.apply(data);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Mark>> call, Throwable t) {

                        }
                    });
                });
            }

            @Override
            public void onFailure(Call<List<UserEntity>> call, Throwable t) {

            }
        });

        return data;
    }

    public LessonEntity postLesson(int groupId, String date, String time, String theme, String homework, String comment, Function<LessonEntity, Void> func) {
        LessonEntity postItem = new LessonEntity();
        func.apply(postItem);
        ws.postLesson(groupId, date, theme, homework, comment, time).enqueue(new Callback<LessonEntity>() {
            @Override
            public void onResponse(Call<LessonEntity> call, Response<LessonEntity> response) {
                func.apply(postItem);
            }

            @Override
            public void onFailure(Call<LessonEntity> call, Throwable t) {
                func.apply(null);
            }
        });

        return postItem;
    }

    public LessonEntity editLesson(int type, String body, int lessonId, Function<LessonEntity, Void> func) {
        LessonEntity editItem = new LessonEntity();

        ws.editLesson(type, body, lessonId).enqueue(new Callback<LessonEntity>() {
            @Override
            public void onResponse(Call<LessonEntity> call, Response<LessonEntity> response) {
                func.apply(editItem);
            }

            @Override
            public void onFailure(Call<LessonEntity> call, Throwable t) {
                func.apply(null);
            }
        });
        return editItem;
    }

    public void setMark(int col, int row, String data) {
        mObservableUsers.getValue().get(row).getMarks().get(col).setMark(data);
        mObservableUsers.setValue(mObservableUsers.getValue());
    }

    public MutableLiveData<Test> loadTest() {
        MutableLiveData<Test> data = new MutableLiveData<>();
        TestEntity test = new TestEntity(1, "Тест");
        ArrayList<QuestionEntity> questionEntities = new ArrayList<>();
        questionEntities.add(new QuestionEntity(1, 1, "Сколько программистов надо, чтобы закрутить лампочку?"));
        ArrayList<AnswerEntity> answerEntities1 = new ArrayList<>();
        answerEntities1.add(new AnswerEntity(1, "Один", false));
        answerEntities1.add(new AnswerEntity(2, "Два", false));
        answerEntities1.add(new AnswerEntity(3, "Четыре", false));
        answerEntities1.add(new AnswerEntity(4, "Хоть сколько не возьми, без документации не вкрутят", true));
        questionEntities.get(0).setAnswers(answerEntities1);

        questionEntities.add(new QuestionEntity(2, 2, "Сколько будет 1 + 1?"));
        ArrayList<AnswerEntity> answerEntities2 = new ArrayList<>();
        answerEntities2.add(new AnswerEntity(5, "Два", false));
        answerEntities2.add(new AnswerEntity(6, "Одиннадцать", true));
        answerEntities2.add(new AnswerEntity(7, "11", false));
        answerEntities2.add(new AnswerEntity(8, "null", false));
        questionEntities.get(1).setAnswers(answerEntities2);

        questionEntities.add(new QuestionEntity(3, 3, "А 10 - 1?"));
        ArrayList<AnswerEntity> answerEntities3 = new ArrayList<>();
        answerEntities3.add(new AnswerEntity(9, "101", false));
        answerEntities3.add(new AnswerEntity(10, "Девять", false));
        answerEntities3.add(new AnswerEntity(11, "9", true));
        answerEntities3.add(new AnswerEntity(12, "undefined", false));
        questionEntities.get(2).setAnswers(answerEntities3);

        test.setQuestions(questionEntities);
        data.setValue(test);
        return data;
    }

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
    public MutableLiveData<List<UserEntity>> getUsers() {
        return mObservableUsers;
    }
    public MutableLiveData<List<GroupEntity>> getGroups() {
        return mGroups;
    }
    public MutableLiveData<Test> getTest() { return mObservableTest; }
}
