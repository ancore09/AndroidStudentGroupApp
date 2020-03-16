package com.example.studentappmvvm.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.MessageBinding;
import com.example.studentappmvvm.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    List<? extends Message> mMessagesList;

    public void setMessagesList(final List<? extends Message> messagesList) {
        if (mMessagesList == null) {
            mMessagesList = messagesList;
            notifyItemRangeInserted(0, messagesList.size());
        } else {

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
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mMessagesList == null ? 0 : mMessagesList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        final MessageBinding binding;

        public MessageViewHolder(MessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
