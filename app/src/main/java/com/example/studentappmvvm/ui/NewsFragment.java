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

//    private ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            final int position = viewHolder.getAdapterPosition();
//            //final New item = mNewsAdapter.mNewsList.get(position);
//
//            if (direction == ItemTouchHelper.LEFT) {
//                mNewsAdapter.notifyItemRemoved(position);
//                mNewsAdapter.mNewsList.remove(position);
//            }
//        }
//    }; //swipe to left on item in recyclerview callback


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);

        //mBinding.newsList.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.line));

        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        //itemTouchHelper.attachToRecyclerView(mBinding.newsList); //attaching callback

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
        }); //listener of pulling down the recyclerview
    }

    private void subscribeUI(MediatorLiveData<List<NewEntity>> liveData) {
        liveData.observe(getViewLifecycleOwner(), mNews -> {
            if (mNews != null) {
                mBinding.setIsLoading(false);
                mNewsAdapter.setNews(mNews);
                mNewsAdapter.notifyDataSetChanged();
            } else {
                mBinding.setIsLoading(true);
            }
            mBinding.executePendingBindings();
        }); //observing livedata from viewmodel
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mNewsAdapter = null;
        super.onDestroyView();
    }
}
