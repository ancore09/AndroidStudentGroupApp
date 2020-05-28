package com.example.studentappmvvm.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.NewAdapterItemBinding;
import com.example.studentappmvvm.databinding.NewCardAdapterItemBinding;
import com.example.studentappmvvm.model.New;
import com.google.android.material.transition.MaterialContainerTransform;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewViewHolder> {

    List<? extends New> mNewsList;

    public List<? extends New> getNewsList(){
        return mNewsList;
    }

    private Fragment fragment;

    public NewsAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

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
        NewCardAdapterItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.new_card_adapter_item, parent, false);
        return new NewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {
        holder.binding.setNewitem(mNewsList.get(position));
        if (mNewsList.get(position).hasImage()) {
            String url = "http://" + DataRepository.SERVER_IP + ":3001/" + mNewsList.get(position).getFileHash();
            Glide.with(fragment).load(url).error(R.drawable.bob).into(holder.binding.imgView);
        }
        holder.binding.cardView.setOnClickListener(view -> {
            if (holder.binding.expandableLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(holder.binding.cardView, new AutoTransition());
                holder.binding.expandableLayout.setVisibility(View.VISIBLE);
                //holder.binding.expandBtn.setText("Collapse");
            } else {
                TransitionManager.beginDelayedTransition(holder.binding.cardView, new AutoTransition());
                holder.binding.expandableLayout.setVisibility(View.GONE);

                //holder.binding.expandBtn.setText("Expand");
            }
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mNewsList == null ? 0 : mNewsList.size();
    }

    static class NewViewHolder extends RecyclerView.ViewHolder {

        final NewCardAdapterItemBinding binding;

        public NewViewHolder(NewCardAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
