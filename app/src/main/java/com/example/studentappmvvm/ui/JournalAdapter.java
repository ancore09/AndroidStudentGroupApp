package com.example.studentappmvvm.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentappmvvm.R;
import com.example.studentappmvvm.databinding.LessonAdapterItemBinding;
import com.example.studentappmvvm.databinding.LessonNirmAdapterItemBinding;
import com.example.studentappmvvm.model.Lesson;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.LessonViewHolder> {

    List<? extends Lesson> mLessonsList;

    @NonNull
    private final LessonClickCallback mLessonClickCallback;

    public JournalAdapter(@NonNull LessonClickCallback clickCallback) {
        mLessonClickCallback = clickCallback;
    }

    public void setLessonsList(final List<? extends Lesson> lessonsList) {
        if (mLessonsList == null) {
            mLessonsList = lessonsList;
            notifyItemRangeInserted(0, mLessonsList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mLessonsList.size();
                }

                @Override
                public int getNewListSize() {
                    return lessonsList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Lesson newLesson = lessonsList.get(newItemPosition);
                    Lesson oldLesson = mLessonsList.get(oldItemPosition);
                    return newLesson.getId() == oldLesson.getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Lesson newLesson = lessonsList.get(newItemPosition);
                    Lesson oldLesson = mLessonsList.get(oldItemPosition);
                    return newLesson.getId() == oldLesson.getId()
                            && TextUtils.equals(newLesson.getDate(), oldLesson.getDate())
                            && TextUtils.equals(newLesson.getMark(), oldLesson.getMark())
                            && TextUtils.equals(newLesson.getTheme(), oldLesson.getTheme())
                            && TextUtils.equals(newLesson.getHomework(), oldLesson.getHomework());
                }
            });
            mLessonsList = lessonsList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LessonNirmAdapterItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.lesson_nirm_adapter_item, parent, false);
        binding.setCallback(mLessonClickCallback);
        return new LessonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        holder.binding.setLessonItem(mLessonsList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mLessonsList == null ? 0 : mLessonsList.size();
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder{

        final LessonNirmAdapterItemBinding binding;

        public LessonViewHolder(LessonNirmAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
