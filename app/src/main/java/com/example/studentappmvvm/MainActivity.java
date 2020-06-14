package com.example.studentappmvvm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.studentappmvvm.model.MemberDataEntity;
import com.example.studentappmvvm.model.UserEntity;
import com.example.studentappmvvm.ui.AppActivity;
import com.example.studentappmvvm.ui.LoginActivity;
import com.example.studentappmvvm.ui.StreamActivity;

public class MainActivity extends AppCompatActivity {
    private final DataRepository mRepository = DataRepository.getInstance();
    private final UserEntity mUser = UserEntity.getInstance();
    SharedPreferences sharedPreferences;

    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("StudentApp", MODE_PRIVATE);

        // auto login if there was another success login before
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
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
            Intent intent = new Intent(this, StreamActivity.class);
            startActivity(intent);
        //}
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
