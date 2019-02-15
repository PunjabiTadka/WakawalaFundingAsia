package com.wakawala.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wakawala.R;
import com.wakawala.databinding.RowCommentsBinding;
import com.wakawala.databinding.RowFilterBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowCommentsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_comments, parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private RowCommentsBinding mBinding;

        public CommentViewHolder(@NonNull RowCommentsBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
