package com.example.studentappmvvm.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.FragmentNewsBinding;
import com.example.studentappmvvm.databinding.TeacherFragmentNewsBinding;
import com.example.studentappmvvm.model.New;
import com.example.studentappmvvm.model.NewEntity;
import com.example.studentappmvvm.viewmodel.NewsListViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class NewsFragment extends Fragment {

    public static final String TAG = "NewsListFragment";

    private NewsAdapter mNewsAdapter;

    private FragmentNewsBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflating binding with layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        // setting views
        mNewsAdapter = new NewsAdapter(this);
        mBinding.newsList.setAdapter(mNewsAdapter);
        mBinding.setIsLoading(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NewsListViewModel viewModel = new ViewModelProvider(requireActivity()).get(NewsListViewModel.class);
        subscribeUI(viewModel.getNews());

        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            viewModel.updateNews();
            refreshLayout.finishRefresh(1000);
        }); // listener of pulling down the recyclerview
    }

    private void subscribeUI(MediatorLiveData<List<NewEntity>> liveData) {
        liveData.observe(getViewLifecycleOwner(), mNews -> {
            // updating news list and list view if live data has changed
            if (mNews != null) {
                mBinding.setIsLoading(false);
                mNewsAdapter.setNews(mNews);
                mNewsAdapter.notifyDataSetChanged();
            } else {
                mBinding.setIsLoading(true);
            }
            mBinding.executePendingBindings();
        }); //observing live data from view model
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mNewsAdapter = null;
        super.onDestroyView();
    }
}
