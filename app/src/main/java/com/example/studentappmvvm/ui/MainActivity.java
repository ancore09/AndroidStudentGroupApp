package com.example.studentappmvvm.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.model.Lesson;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public BottomNavigationView navView;
    NewsFragment news = new NewsFragment();
    JournalFragment journal = new JournalFragment();
    ChatFragment chat = new ChatFragment();
    ProfileFragment profile = new ProfileFragment();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    mTextMessage.setText(R.string.title_news);
                    changeFragment(news);
                    return true;
                case R.id.navigation_journal:
                    mTextMessage.setText(R.string.title_journal);
                    changeFragment(journal);
                    return true;
                case R.id.navigation_chat:
                    mTextMessage.setText(R.string.title_chat);
                    changeFragment(chat);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    changeFragment(profile);
                    return true;
            }
            return false;
        }
    };

    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener
            = menuItem -> {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            navView = findViewById(R.id.nav_view);
            mTextMessage = findViewById(R.id.message);
            navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navView.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);

            KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
                if (isOpen) {
                    navView.setVisibility(View.GONE);
                } else {
                    navView.setVisibility(View.VISIBLE);
                }
            });

            getSupportFragmentManager().beginTransaction().add(R.id.place_holder, news, NewsFragment.TAG).commit();
        }
    }



    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.place_holder, fragment).commit();
    }

    public void showLesson(Lesson lesson) {
        LessonFragment fragment = LessonFragment.forLesson(lesson);
        getSupportFragmentManager().beginTransaction().addToBackStack("lesson").replace(R.id.place_holder, fragment, null).commit();
    }

    public void showPrefs(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().addToBackStack("prefs").replace(R.id.place_holder, fragment).commit();
    }

    static String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
