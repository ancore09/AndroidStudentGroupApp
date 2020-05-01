package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.FragmentTestBinding;
import com.example.studentappmvvm.model.Test;
import com.example.studentappmvvm.viewmodel.TestViewModel;

public class TestFragment extends Fragment {

    private FragmentTestBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TestViewModel viewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);
        subscribeUI(viewModel.getTest());
    }

    private void subscribeUI(MediatorLiveData<Test> liveDate) {
        liveDate.observe(getViewLifecycleOwner(), test -> {
            mBinding.setTest(test);
            mBinding.setCurrentquestion(test.getQuestions().get(0));
        });
    }
}
