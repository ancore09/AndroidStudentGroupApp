package com.example.studentappmvvm.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.NewAdapterItemBinding;
import com.example.studentappmvvm.model.New;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewViewHolder> {

    List<? extends New> mNewsList;

    public void setNews(final List<? extends New> newsList) {
        if (mNewsList == null) {
            mNewsList = newsList;
            notifyItemRangeInserted(0, mNewsList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mNewsList.size();
                }

                @Override
                public int getNewListSize() {
                    return newsList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mNewsList.get(oldItemPosition).getId() ==
                            newsList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    New newNew = newsList.get(newItemPosition);
                    New oldNew = mNewsList.get(oldItemPosition);
                    return newNew.getId() == oldNew.getId()
                            && TextUtils.equals(newNew.getTitle(), oldNew.getTitle())
                            && TextUtils.equals(newNew.getBody(), oldNew.getBody())
                            && TextUtils.equals(newNew.getEpilogue(), oldNew.getEpilogue());
                }
            });
            mNewsList = newsList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewAdapterItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.new_adapter_item, parent, false);
        return new NewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {
        holder.binding.setNewitem(mNewsList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mNewsList == null ? 0 : mNewsList.size();
    }

    static class NewViewHolder extends RecyclerView.ViewHolder {

        final NewAdapterItemBinding binding;

        public NewViewHolder(NewAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
