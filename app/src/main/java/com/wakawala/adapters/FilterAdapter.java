package com.wakawala.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wakawala.R;
import com.wakawala.databinding.RowFilterBinding;
import com.wakawala.model.FilterModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<FilterModel> categoryModels;

    public FilterAdapter(Context context, ArrayList<FilterModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowFilterBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_filter, parent, false);
        return new FilterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        holder.mBinding.tvName.setText(categoryModels.get(position).getName());
        holder.mBinding.cbCheck.setChecked(categoryModels.get(position).isChecked());

        holder.mBinding.llParent.setOnClickListener(view -> {
            categoryModels.get(position).setChecked(!categoryModels.get(position).isChecked());
            notifyItemChanged(position);
            Log.e("size", getSelectedSize() + "");
        });
    }

    public int getSelectedSize() {
        int count = 0;
        for (int i = 0; i < categoryModels.size(); i++) {
            if (categoryModels.get(i).isChecked())
                count++;
        }
        return count;
    }

    public String getSelectedArray() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < categoryModels.size(); i++) {
            if (categoryModels.get(i).isChecked()) {
                if (!stringBuilder.toString().isEmpty())
                    stringBuilder.append(",,");
                stringBuilder.append(categoryModels.get(i).getName());
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {

        private RowFilterBinding mBinding;

        public FilterViewHolder(@NonNull RowFilterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
