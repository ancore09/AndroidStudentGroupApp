package com.example.studentappmvvm.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studentappmvvm.R;

public class AccountPrefsFragment extends Fragment {
    SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_prefs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        getView().findViewById(R.id.savebtn).setOnClickListener(v -> {
            EditText mEditLogin = getView().findViewById(R.id.login);
            EditText mEditCourse = getView().findViewById(R.id.course);
            if (mEditLogin.getText().toString() != "") {
                editor.putString("LOGIN_KEY", mEditLogin.getText().toString().trim());
                editor.putString("COURSE_KEY", mEditCourse.getText().toString().trim());
                editor.commit();
            }
        });
    }
}
