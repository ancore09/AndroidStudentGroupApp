package com.example.studentappmvvm.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studentappmvvm.R;
import com.google.android.material.textfield.TextInputEditText;

public class MarkEditDialogFragment extends DialogFragment {
    private MarkEditCallback callback;

    public MarkEditDialogFragment(MarkEditCallback callback) {
        this.callback = callback;
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
        textView.setText("New mark");

        TextInputEditText editText = getView().findViewById(R.id.input_info);
        editText.setHint("5/4/3/2/1");

        getView().findViewById(R.id.btn_post).setOnClickListener(v -> {
           callback.onEdit(editText.getText().toString());
           dismiss();
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
