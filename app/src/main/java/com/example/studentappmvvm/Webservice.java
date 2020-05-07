package com.example.studentappmvvm;

import com.example.studentappmvvm.model.Evaluation;
import com.example.studentappmvvm.model.EvaluationEntity;
import com.example.studentappmvvm.model.FileResponse;
import com.example.studentappmvvm.model.GroupEntity;
import com.example.studentappmvvm.model.GroupingEntity;
import com.example.studentappmvvm.model.InformingEntity;
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
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Webservice {

    @GET("/auth")
    Call<UserEntity> authUser(@Query("login") String login, @Query("passwordhash") String passHash);

    @GET("/getMemberData")
    Call<MemberDataEntity> getMemberData(@Query("id") int id);

    @GET("/getMessages")
    Call<List<MessageEntity>> getMessages(@Query("room") String room);

    @GET("/getUsers")
    Call<List<UserEntity>> getUsers(@Query("groupid") int groupId);

    @GET("/getUserMarks")
    Call<List<Mark>> getMarks(@Query("login") String login, @Query("lessonsids") int[] les_ids);

    @GET("/getMarks")
    Call<List<Mark>> getMarksForTable(@Query("login") String login, @Query("groupid") int groupId);

    @GET("/getEvaluation")
    Call<List<EvaluationEntity>> getEvaluation(@Query("login") String login);

    @GET("/getInforming")
    Call<List<InformingEntity>> getInforming(@Query("groupid") int[] group_ids);

    @GET("/getNews")
    Call<List<NewEntity>> getNews(@Query("groupid") int[] group_id);

    @GET("/getLessons")
    Call<List<LessonEntity>> getLessons(@Query("groupid") int[] group_ids, @Query("loginid") int user_id);

    @GET("/getGroup")
    Call<List<GroupEntity>> getGroup(@Query("id") int[] group_ids);

    @GET("/getGrouping")
    Call<List<GroupingEntity>> getGrouping(@Query("userid") int user_id);

    @Multipart
    @POST("/uploadFile")
    Call<FileResponse> uploadFile(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("/postNew")
    Call<NewEntity> postNew(@Query("groupid") int groupId, @Field("datedmy") String date, @Field("title") String title, @Field("body") String body, @Field("epilogue") String epil, @Field("filehash") String filehash);

    @FormUrlEncoded
    @PUT("/editLesson")
    Call<LessonEntity> editLesson(@Query("type") int type, @Field("body") String body, @Field("lessonId") int lessonId);

    @FormUrlEncoded
    @POST("/postLesson")
    Call<LessonEntity> postLesson(@Field("groupId") int groupId, @Field("datedmy") String date, @Field("theme") String theme, @Field("homework") String homework, @Field("comment") String comment, @Field("times") String time);
}
