package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.TeacherFragmentJournalBinding;

import java.util.ArrayList;

public class TeacherJournalFragment extends Fragment {

    TeacherFragmentJournalBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.teacher_fragment_journal, container, false);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new TeacherLessonsTabFragment());
        fragments.add(new TableFragment());
        mBinding.pager.setAdapter(new PagerAdapter(getChildFragmentManager(), 0, fragments));
        mBinding.tabs.setupWithViewPager(mBinding.pager);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
