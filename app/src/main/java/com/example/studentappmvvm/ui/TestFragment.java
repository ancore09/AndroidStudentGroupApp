package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.FragmentTestBinding;
import com.example.studentappmvvm.model.Test;
import com.example.studentappmvvm.viewmodel.TestViewModel;

public class TestFragment extends Fragment {

    TestViewModel viewModel;
    private FragmentTestBinding mBinding;
    private AnswerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new AnswerAdapter();
        mBinding.answerList.setAdapter(mAdapter);
        //mBinding.answerList.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.line));

        viewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);

        mBinding.nextbtn.setOnClickListener(view1 -> {
            viewModel.nextQuestion();
        });

        subscribeUItest(viewModel.getTest());
        subscribeUIquestion(viewModel.getCurrentQuestion());
    }

    private void subscribeUItest(MediatorLiveData<Test> liveData) {
        liveData.observe(getViewLifecycleOwner(), test -> {
            mBinding.setTest(test);
            mBinding.setCurrentquestion(test.getQuestions().get(0));
            mAdapter.setAnswers(test.getQuestions().get(0).getAnswers());
        });
    }

    private void subscribeUIquestion(MutableLiveData<Integer> liveData) {
        liveData.observe(getViewLifecycleOwner(), integer -> {
            if (integer != -1) {
                mBinding.setCurrentquestion(viewModel.getTest().getValue().getQuestions().get(integer));
                mAdapter.setAnswers(viewModel.getTest().getValue().getQuestions().get(integer).getAnswers());
            } else {
                Toast.makeText(getContext(), mAdapter.getSelectedAnswers().toString(), Toast.LENGTH_LONG).show();
                mAdapter.getSelectedAnswers().clear();
            }
        });
    }
}
