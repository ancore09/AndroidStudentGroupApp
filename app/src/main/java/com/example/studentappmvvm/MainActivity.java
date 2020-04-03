package com.example.studentappmvvm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentappmvvm.model.MemberDataEntity;
import com.example.studentappmvvm.model.UserEntity;
import com.example.studentappmvvm.ui.AppActivity;
import com.example.studentappmvvm.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private final DataRepository mRepository = DataRepository.getInstance();
    private final UserEntity mUser = UserEntity.getInstance();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("StudentApp", MODE_PRIVATE);

//        if (sharedPreferences.getInt("id", -1) != -1) { //if user has entered the app once, he doesn't have to log in manually again
//            mUser.setID(sharedPreferences.getInt("id", -1));
//            mUser.setMemberdata_ID(sharedPreferences.getInt("memberDataId", -1));
//            mUser.setMemberData(new MemberDataEntity(sharedPreferences.getString("nickname", "null"), sharedPreferences.getString("color", "null")));
//            mUser.setLogin(sharedPreferences.getString("login", "null"));
//            mUser.setFirstName(sharedPreferences.getString("firstname", "null"));
//            mUser.setLastName(sharedPreferences.getString("lastname", "null"));
//            mUser.setNickName(sharedPreferences.getString("nickname", "null"));
//
//            mRepository.getGroups(integer -> {
//                Intent intent = new Intent(this, AppActivity.class);
//                startActivity(intent);
//                return 0;
//            });
//        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        //}
    }
}
