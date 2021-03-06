package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.TeacherFragmentTabLessonsBinding;
import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.viewmodel.JournalViewModel;

import java.util.List;

public class TeacherLessonsTabFragment extends Fragment {

    private JournalAdapter mJournalAdapter;

    private TeacherFragmentTabLessonsBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflating binding with layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.teacher_fragment_tab_lessons, container, false);

        // setting views
        mBinding.lessonsList.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.line));
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(3000);
        });

        mJournalAdapter = new JournalAdapter(lesson -> {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                LessonFragment lessonFragment = LessonFragment.forLesson(lesson);
                ((TeacherAppActivity) requireActivity()).performTransition(lessonFragment, this);
            }
        }); // constructor receives callback for items to show corresponding lesson fragment
        mBinding.lessonsList.setAdapter(mJournalAdapter);
        mBinding.setIsLoading(true);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JournalViewModel viewModel = new ViewModelProvider(requireActivity()).get(JournalViewModel.class);

        mBinding.lessonsSearchBtn.setOnClickListener(v -> {
            Editable query = mBinding.lessonsSearchBox.getText();
            viewModel.setQuery(query);
        });

        mBinding.floatingActionButton.setOnClickListener(v -> {
            new LessonAdditionDialogFragment().show(getChildFragmentManager(), "dialog");
        });
        subscribeUI(viewModel.getLessons()); // subscribe view to data changes
    }

    private void subscribeUI(LiveData<List<LessonEntity>> liveData) {
        // updating lessons list and list view if live data has changed
        liveData.observe(getViewLifecycleOwner(), mLessons -> {
            if (mLessons != null) {
                mBinding.setIsLoading(false);
                mJournalAdapter.setLessonsList(mLessons);
            } else {
                mBinding.setIsLoading(true);
            }
            mBinding.executePendingBindings();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherAppActivity) requireActivity()).curr = this;
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mJournalAdapter = null;
        super.onDestroyView();
    }

}
