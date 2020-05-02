package com.example.studentappmvvm.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.AnswerAdapterItemBinding;
import com.example.studentappmvvm.model.AnswerEntity;

import java.util.ArrayList;
import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    List<AnswerEntity> mAnswers;
    List<Integer> selectedAnswers = new ArrayList<>();

    public void setAnswers(List<AnswerEntity> answers) {
        if (mAnswers == null) {
            mAnswers = answers;
            notifyItemRangeInserted(0, mAnswers.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mAnswers.size();
                }

                @Override
                public int getNewListSize() {
                    return answers.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;
                }
            });
            mAnswers = answers;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AnswerAdapterItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.answer_adapter_item, parent, false);
        return new AnswerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        holder.binding.setAnswer(mAnswers.get(position));
        holder.binding.checkbox.setChecked(false);
        holder.binding.checkbox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedAnswers.add(mAnswers.get(position).getID());
            } else {
                if (selectedAnswers.contains(mAnswers.get(position).getID())) {
                    selectedAnswers.remove(position);
                }
            }
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    public List<Integer> getSelectedAnswers() {
        return selectedAnswers;
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {

        final AnswerAdapterItemBinding binding;

        public AnswerViewHolder(AnswerAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
