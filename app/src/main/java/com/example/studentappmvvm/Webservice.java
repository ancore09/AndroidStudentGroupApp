package com.example.studentappmvvm;

import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;
import com.example.studentappmvvm.model.MemberData;
import com.example.studentappmvvm.model.MemberDataEntity;
import com.example.studentappmvvm.model.MessageEntity;
import com.example.studentappmvvm.model.NewEntity;
import com.example.studentappmvvm.model.UserEntity;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Webservice {

    @GET("/auth")
    Call<UserEntity> authUser(@Query("login") String login, @Query("passwordhash") String passHash);

    @GET("/getMemberData")
    Call<MemberDataEntity> getMemberData(@Query("id") int id);

    @GET("/getMessages")
    Call<List<MessageEntity>> getMessages();

    @GET("/getUserMarks")
    Call<List<Mark>> getMarks(/*@Query("course") String course,*/ @Query("login") String login);

    @GET("/getNews")
    Call<List<NewEntity>> getNews(@Query("groupid") int group_id);

    @GET("/getLessons")
    Call<List<LessonEntity>> getLessons(@Query("groupid") int group_id, @Query("loginid") int user_id);

    @Multipart
    @POST("/uploadFile")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);
}
