package com.example.studentappmvvm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentappmvvm.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private final DataRepository mRepository = DataRepository.getInstance();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("StudentApp", MODE_PRIVATE);

        if (sharedPreferences.getInt("id", -1) != -1) {

        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
