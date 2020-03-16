package com.example.studentappmvvm;

import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.model.Mark;
import com.example.studentappmvvm.model.MessageEntity;
import com.example.studentappmvvm.model.NewEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Webservice {
    @GET("/getNews")
    Call<List<NewEntity>> getNews();

    @GET("/getTable")
    Call<List<LessonEntity>> getLessons();

    @GET("/getMessages")
    Call<List<MessageEntity>> getMessages();

    @GET("/getUserMarks")
    Call<List<Mark>> getMarks(/*@Query("course") String course,*/ @Query("login") String login);
}
