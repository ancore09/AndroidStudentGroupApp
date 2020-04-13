package com.example.studentappmvvm.ui;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.studentappmvvm.R;
import com.example.studentappmvvm.viewmodel.NewAdditionViewModel;

import static android.app.Activity.RESULT_OK;

public class NewAdditionDialogFragment extends DialogFragment {

    private static int RESULT_LOAD_IMAGE = 1;

    private NewAdditionViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_App_MaterialAlertDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_addition_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText groupName = getView().findViewById(R.id.input_group);
        EditText date = getView().findViewById(R.id.input_date);
        EditText title = getView().findViewById(R.id.input_title);
        EditText body = getView().findViewById(R.id.input_body);
        EditText epil = getView().findViewById(R.id.input_epil);

        viewModel = new ViewModelProvider(requireActivity()).get(NewAdditionViewModel.class);

        getView().findViewById(R.id.imgView).setOnClickListener(v -> {
            attachPhoto();
        });

        getView().findViewById(R.id.btn_post).setOnClickListener(v -> {
            viewModel.postNew(groupName.getText().toString(),
                    date.getText().toString(),
                    title.getText().toString(),
                    body.getText().toString(),
                    epil.getText().toString(), newEntity -> {
                if (newEntity != null) {
                    this.dismiss(); return 0;
                } else {
                    return 1;
                }
                    });
        });
    }

    void attachPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    } //starting gallery activity to choose photo

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            viewModel.uploadFile(filePath, fileResponse -> {
                String url = "http://192.168.1.129:3000/" + fileResponse.getName();
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new RoundedCorners(30));
                Glide.with(this).load(url).error(R.drawable.circle).apply(requestOptions).into((ImageView) getView().findViewById(R.id.imgView));
                return 0;
            });
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
