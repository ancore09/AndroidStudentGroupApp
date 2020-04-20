package com.example.studentappmvvm.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.model.Lesson;
import com.example.studentappmvvm.viewmodel.LessonEditViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.function.Function;

public class LessonEditDialogFragment extends DialogFragment {

    private String message;
    private String hint;

    private int lessonID;
    private Function<String, Void> func;

    public LessonEditDialogFragment(String message, String hint, int id, Function<String, Void> func) {
        this.message = message;
        this.hint = hint;
        this.lessonID = id;
        this.func = func;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_App_MaterialAlertDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lesson_edit_from, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = getView().findViewById(R.id.messageEdit);
        textView.setText(message);

        TextInputEditText editText = getView().findViewById(R.id.input_info);
        editText.setHint(hint);

        LessonEditViewModel viewModel = new ViewModelProvider(requireActivity()).get(LessonEditViewModel.class);

        getView().findViewById(R.id.btn_post).setOnClickListener(v -> {
            switch (hint) {
                case "Theme":
                    viewModel.editTheme(editText.getText().toString(), lessonID, lessonEntity -> {
                        if (lessonEntity != null) {
                            func.apply(editText.getText().toString());
                            dismiss();
                        }
                        return null;
                    });
                    break;
                case "Hometask":
                    viewModel.editHomework(editText.getText().toString(), lessonID, lessonEntity -> {
                        if (lessonEntity != null) {
                            func.apply(editText.getText().toString());
                            dismiss();
                        }
                        return null;
                    });
                    break;
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
