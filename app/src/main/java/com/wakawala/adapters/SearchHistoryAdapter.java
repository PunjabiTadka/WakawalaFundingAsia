package com.wakawala.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wakawala.R;
import com.wakawala.databinding.RowSearchBinding;
import com.wakawala.interfaces.RecyclerItemClickInterface;
import com.wakawala.model.SearchCampaignModel;
import com.wakawala.viewholder.PaginationViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;

    private Context context;
    private RecyclerItemClickInterface<Object> clickInterface;
    public ArrayList<SearchCampaignModel> searchCampaignModels;

    public SearchHistoryAdapter(Context context, RecyclerItemClickInterface<Object> clickInterface,
                                ArrayList<SearchCampaignModel> searchCampaignModels) {
        this.context = context;
        this.clickInterface = clickInterface;
        this.searchCampaignModels = searchCampaignModels;
    }

    @Override
    public int getItemCount() {
        return searchCampaignModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == searchCampaignModels.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case ITEM:
                RowSearchBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_search, parent, false);
                return new SearchHistoryViewHolder(binding);
            case LOADING:
            default:
                View v2 = inflater.inflate(R.layout.row_pagination_progress_bar, parent, false);
                viewHolder = new PaginationViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int pos) {
        switch (getItemViewType(pos)) {
            case LOADING:
                break;
            case ITEM:
                SearchHistoryViewHolder searchHistoryViewHolder = (SearchHistoryViewHolder) holder;
                SearchCampaignModel searchCampaignModel = searchCampaignModels.get(pos);
                if (searchHistoryViewHolder.mBinding != null) {
                    searchHistoryViewHolder.mBinding.tvCampaignName.setText(searchCampaignModel.getUsername());
                    searchHistoryViewHolder.mBinding.tvName.setText(searchCampaignModel.getEmail());
                    if (searchCampaignModel.getCampaignCode().isEmpty())
                        searchHistoryViewHolder.mBinding.tvCode.setVisibility(View.GONE);
                    else {
                        searchHistoryViewHolder.mBinding.tvCode.setVisibility(View.VISIBLE);
                        searchHistoryViewHolder.mBinding.tvCode.setText(searchCampaignModel.getCampaignCode());
                    }

                    Glide.with(context).load(searchCampaignModel.getProfilePic())
                            .apply(new RequestOptions().error(R.drawable.place_holder).placeholder(R.drawable.place_holder))
                            .into(searchHistoryViewHolder.mBinding.imgCampaign);
                }
                break;
        }
    }

    public void add(SearchCampaignModel mc) {
        searchCampaignModels.add(mc);
        notifyItemInserted(searchCampaignModels.size() - 1);
    }

    public void addAll(List<SearchCampaignModel> mcList) {
        for (SearchCampaignModel mc : mcList) {
            add(mc);
        }
    }

    public void remove(SearchCampaignModel city) {
        int position = searchCampaignModels.indexOf(city);
        if (position > -1) {
            searchCampaignModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        if (!isLoadingAdded) {
            isLoadingAdded = true;
            add(new SearchCampaignModel());
        }
    }

    public void removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false;

            if (searchCampaignModels.size() > 0) {
                int position = searchCampaignModels.size() - 1;
                SearchCampaignModel item = getItem(position);

                if (item != null) {
                    searchCampaignModels.remove(position);
                    notifyItemRemoved(position);
                }
            }
        }
    }

    public SearchCampaignModel getItem(int position) {
        return searchCampaignModels.get(position);
    }

    public class SearchHistoryViewHolder extends RecyclerView.ViewHolder {

        private RowSearchBinding mBinding;

        public SearchHistoryViewHolder(@NonNull RowSearchBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
