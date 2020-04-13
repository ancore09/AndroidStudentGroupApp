package com.example.studentappmvvm.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private String[] tabTitles = new String[] { "Schedule", "Table"};

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Fragment> frags) {
        super(fm, behavior);
        this.fragments = (ArrayList<Fragment>) frags.clone();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
