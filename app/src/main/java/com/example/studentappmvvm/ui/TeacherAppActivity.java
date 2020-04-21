package com.example.studentappmvvm.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Fade;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionSet;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TeacherAppActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = { //required permissions for chat
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final short MOVE_DEFAULT_TIME = 50;
    private static final short FADE_DEFAULT_TIME = 100;


    public TextView mTextMessage;
    public View mesBackground;
    public BottomNavigationView navView;
    DataRepository mRepository;

    TeacherNewsFragment news = new TeacherNewsFragment();
    TeacherJournalFragment journal = new TeacherJournalFragment();
    ChatFragment chat = new ChatFragment();
    ProfileFragment profile = new ProfileFragment();

    public Fragment curr = news;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    mTextMessage.setText(R.string.title_news);
                    //changeFragment(news);
                    performTransition(news, curr);
                    return true;
                case R.id.navigation_journal:
                    mTextMessage.setText(R.string.title_journal);
                    //changeFragment(journal);
                    performTransition(journal, curr);
                    return true;
                case R.id.navigation_chat:
                    mTextMessage.setText(R.string.title_chat);
                    //navView.setVisibility(View.GONE);
                    //changeFragment(chat);
                    performTransition(chat, curr);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    //changeFragment(profile);
                    performTransition(profile, curr);
                    return true;
            }
            return false;
        }
    };

    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener
            = menuItem -> {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.Theme_MyApp);
        verifyStoragePermissions(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mRepository = DataRepository.getInstance();

            navView = findViewById(R.id.nav_view);
            mTextMessage = findViewById(R.id.message);
            mesBackground = findViewById(R.id.back);
            navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navView.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);

            KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
                if (isOpen) {
                    if (curr instanceof ChatFragment) {
                        navView.setVisibility(View.GONE);
                    }
                } else {
                    if (curr instanceof ChatFragment) {
                        navView.setVisibility(View.VISIBLE);
                    }
                }
            }); //when keyboard is open, navbar wont interfere

            getSupportFragmentManager().beginTransaction().add(R.id.place_holder, news, NewsFragment.TAG).commit();
            //mRepository.postLoad(); //loading of all data from server
            //mRepository.postLoadUsers(1);
        }
    }

    public void performTransition(Fragment next, Fragment fr) {
        if (isDestroyed()) {
            return;
        }

        curr = next;


        Fragment nextFragment = next;
        Fragment previousFragment = fr;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fade exitFade = new Fade();
        exitFade.setDuration(FADE_DEFAULT_TIME);
        previousFragment.setExitTransition(exitFade);

        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
        nextFragment.setSharedElementEnterTransition(enterTransitionSet);

        Fade enterFade = new Fade();
        enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
        enterFade.setDuration(FADE_DEFAULT_TIME);
        nextFragment.setEnterTransition(enterFade);

        fragmentTransaction.addSharedElement(previousFragment.requireView().findViewById(R.id.containerL), previousFragment.getView().findViewById(R.id.containerL).getTransitionName());
        fragmentTransaction.addToBackStack("transition").replace(R.id.place_holder, nextFragment).commitAllowingStateLoss();
    } //transition with fade in/out animation between two fragments

    public void simpleShowFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().addToBackStack("change").replace(R.id.place_holder, fragment).commit();
    }


    static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    } //sha1 hash function for auth

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    } //checking for permissions
}
