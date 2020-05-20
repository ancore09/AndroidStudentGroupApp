package com.example.studentappmvvm.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.viewmodel.LessonAdditionViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class LessonAdditionDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_App_MaterialAlertDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lesson_addition_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LessonAdditionViewModel viewModel = new ViewModelProvider(requireActivity()).get(LessonAdditionViewModel.class);

        TextInputEditText group = getView().findViewById(R.id.input_group);
        TextInputEditText date = getView().findViewById(R.id.input_date);
        TextInputEditText time = getView().findViewById(R.id.input_time);
        TextInputEditText theme = getView().findViewById(R.id.input_theme);
        TextInputEditText homework = getView().findViewById(R.id.input_homework);
        TextInputEditText comment = getView().findViewById(R.id.input_comment);
        getView().findViewById(R.id.btn_post).setOnClickListener(v -> {
            viewModel.postLesson(group.getText().toString(), date.getText().toString(), time.getText().toString(), theme.getText().toString(), homework.getText().toString(), comment.getText().toString(), lessonEntity -> {
                if (lessonEntity != null) {
                    //showSnackbar("Lesson posted", getView().findViewById(R.id.container), null);
                    dismiss();
                } else {
                    showSnackbar("Post failed", getView().findViewById(R.id.container), null);
                }
                return null;
            });
        });
    }

    private void showSnackbar(String message,View contextView, View anchor) {
        Snackbar.make(contextView, message, Snackbar.LENGTH_SHORT).setAnchorView(anchor).show();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
