package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.FragmentLessonBinding;
import com.example.studentappmvvm.model.Lesson;
import com.example.studentappmvvm.model.LessonEntity;
import com.example.studentappmvvm.viewmodel.LessonViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.function.Function;

public class LessonFragment extends Fragment implements View.OnClickListener{

    private FragmentLessonBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lesson, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] args = getArguments().getString("info").split("#&");
        LessonEntity les = new LessonEntity();
        les.setId(Integer.parseInt(args[0]));
        les.setHomework(args[1]);
        les.setMark(args[2]);
        les.setDate(args[3]);
        les.setTheme(args[4]);

        mBinding.setLesson(les);

        final LessonViewModel viewModel = new ViewModelProvider(requireActivity()).get(LessonViewModel.class);

        if (!viewModel.getUser().isStudent()) {
            mBinding.homeworkLes.setOnClickListener(this);
            mBinding.themeLes.setOnClickListener(this);
        }
    }

    public static LessonFragment forLesson(Lesson lesson) {
        LessonFragment fr = new LessonFragment();
        Bundle args = new Bundle();
        args.putString("info", lesson.getId() + "#&" + lesson.getHomework() + "#&" + lesson.getMark() +"#&" + lesson.getDate() + "#&" + lesson.getTheme() );
        fr.setArguments(args);
        return fr;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeworkLes:
                new LessonEditDialogFragment("Enter new hometask", "Hometask", mBinding.getLesson().getId(), s -> {
                    mBinding.homeworkLes.setText(s);
                    showSnackbar("Homework changed");
                    return null;
                }).show(getChildFragmentManager(), "dialog");
                break;
            case R.id.themeLes:
                new LessonEditDialogFragment("Enter new theme", "Theme", mBinding.getLesson().getId(), s -> {
                    mBinding.themeLes.setText(s);
                    //View contextView = getView().findViewById(R.id.containerL);
                    showSnackbar("Theme changed");
                    return null;
                }).show(getChildFragmentManager(), "dialog");
            default:
                break;
        }
    }

    private void showSnackbar(String message) {
        View contextView = ((TeacherAppActivity) requireActivity()).findViewById(R.id.place_holder);
        Snackbar.make(contextView, message, Snackbar.LENGTH_SHORT).setAnchorView(((TeacherAppActivity) requireActivity()).navView).show();
    }
}
