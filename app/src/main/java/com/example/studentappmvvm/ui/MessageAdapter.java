package com.example.studentappmvvm.ui;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.MessageBinding;
import com.example.studentappmvvm.model.Message;
import com.example.studentappmvvm.model.MessageEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    List<? extends Message> mMessagesList;
    ChatFragment fragment;

    public MessageAdapter(ChatFragment fragment) {
        this.fragment = fragment;
    }

    public void setMessagesList(final List<? extends Message> messagesList) {
        if (mMessagesList == null) {
            mMessagesList = messagesList;
            notifyItemRangeInserted(0, messagesList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mMessagesList.size();
                }

                @Override
                public int getNewListSize() {
                    return messagesList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mMessagesList.get(oldItemPosition).getId() ==
                            messagesList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Message newProduct = messagesList.get(newItemPosition);
                    Message oldProduct = mMessagesList.get(oldItemPosition);
                    return newProduct.getId() == oldProduct.getId();
                }
            });
            mMessagesList = messagesList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.message, parent, false);
        return new MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.binding.setMessage(mMessagesList.get(position));
        if (mMessagesList.get(position).hasImage()) {
            String url = "http://" + DataRepository.SERVER_IP + ":3001/" + mMessagesList.get(position).getFileHash();
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new RoundedCorners(50));
            Glide.with(fragment).load(url).error(R.drawable.circle).apply(requestOptions).into(holder.binding.imgViewt);
            Glide.with(fragment).load(url).error(R.drawable.circle).apply(requestOptions).into(holder.binding.imgView);
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mMessagesList == null ? 0 : mMessagesList.size();
    }

    @Override
    public long getItemId(int position) {
        return mMessagesList.get(position).getId();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        final MessageBinding binding;

        public MessageViewHolder(MessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
