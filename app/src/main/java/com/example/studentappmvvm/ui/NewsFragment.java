package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.FragmentNewsBinding;
import com.example.studentappmvvm.model.NewEntity;
import com.example.studentappmvvm.viewmodel.NewsListViewModel;

import java.util.List;

public class NewsFragment extends Fragment {

    public static final String TAG = "NewsListFragment";

    private NewsAdapter mNewsAdapter;

    private FragmentNewsBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);

        mNewsAdapter = new NewsAdapter();
        mBinding.newsList.setAdapter(mNewsAdapter);
        mBinding.setIsLoading(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NewsListViewModel viewModel = new ViewModelProvider(requireActivity()).get(NewsListViewModel.class);
        subscribeUI(viewModel.getNews());
    }

    private void subscribeUI(LiveData<List<NewEntity>> liveData) {
        liveData.observe(getViewLifecycleOwner(), mNews -> {
            if (mNews != null) {
                mBinding.setIsLoading(false);
                mNewsAdapter.setNews(mNews);
            } else {
                mBinding.setIsLoading(true);
            }
            mBinding.executePendingBindings();
        });
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mNewsAdapter = null;
        super.onDestroyView();
    }
}
