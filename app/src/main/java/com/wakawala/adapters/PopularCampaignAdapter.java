package com.wakawala.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.wakawala.R;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.databinding.RowCampaignPopularBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.interfaces.RecyclerItemClickInterface;
import com.wakawala.model.PopularCampaignModel;
import com.wakawala.ui.actvities.DetailCampaignActivity;
import com.wakawala.ui.actvities.UserProfileActivity;
import com.wakawala.viewholder.PaginationViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularCampaignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;

    private Context context;
    private RecyclerItemClickInterface<Object> clickInterface;
    public ArrayList<PopularCampaignModel> mPopularCampaignList;

    public PopularCampaignAdapter(Context context, RecyclerItemClickInterface<Object> clickInterface,
                                  ArrayList<PopularCampaignModel> mPopularCampaignList) {
        this.context = context;
        this.clickInterface = clickInterface;
        this.mPopularCampaignList = mPopularCampaignList;
    }

    @Override
    public int getItemCount() {
        return mPopularCampaignList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mPopularCampaignList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case ITEM:
                RowCampaignPopularBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_campaign_popular, parent, false);
                return new PopularCampaignHolder(binding);
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
                PopularCampaignHolder popularCampaignHolder = (PopularCampaignHolder) holder;
                PopularCampaignModel popularCampaignModel = mPopularCampaignList.get(pos);
                if (popularCampaignHolder.binding != null) {
                    popularCampaignHolder.binding.setPopularCampaignModel(mPopularCampaignList.get(pos));

                    Glide.with(context).load(mPopularCampaignList.get(pos).getUserProfilePic())
                            .apply(new RequestOptions().placeholder(R.drawable.place_holder)
                                    .error(R.drawable.place_holder).circleCrop().transform(new CircleCrop()))
                            .into(popularCampaignHolder.binding.ivProfileImage);

                    popularCampaignHolder.binding.ivStar.setVisibility(View.VISIBLE);

                    popularCampaignHolder.binding.tvCategory.setText(getCategoryFromId(popularCampaignModel.getCategory()) + " - " + popularCampaignModel.getCategory());

                    if (popularCampaignModel.getUserType() == 2)
                        popularCampaignHolder.binding.tvType.setText("Organization");
                    else
                        popularCampaignHolder.binding.tvType.setText("Individual");

                    popularCampaignHolder.binding.ivProfileImage.setOnClickListener(view -> {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra(WebFields.USER__NAME, popularCampaignModel.getUserName());
                        intent.putExtra(WebFields.USER_PROFILE_PIC, popularCampaignModel.getUserProfilePic());
                        context.startActivity(intent);
                    });

                    popularCampaignHolder.binding.tvName.setOnClickListener(view -> {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra(WebFields.USER__NAME, popularCampaignModel.getUserName());
                        intent.putExtra(WebFields.USER_PROFILE_PIC, popularCampaignModel.getUserProfilePic());
                        context.startActivity(intent);
                    });

                    popularCampaignHolder.binding.tvType.setOnClickListener(view -> {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra(WebFields.USER__NAME, popularCampaignModel.getUserName());
                        intent.putExtra(WebFields.USER_PROFILE_PIC, popularCampaignModel.getUserProfilePic());
                        context.startActivity(intent);
                    });

                    popularCampaignHolder.binding.tvPlace.setOnClickListener(view -> {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra(WebFields.USER__NAME, popularCampaignModel.getUserName());
                        intent.putExtra(WebFields.USER_PROFILE_PIC, popularCampaignModel.getUserProfilePic());
                        context.startActivity(intent);
                    });

                    popularCampaignHolder.binding.ivMore.setOnClickListener(v -> {
                        PopupMenu popup = new PopupMenu(popularCampaignHolder.itemView.getContext(), popularCampaignHolder.binding.ivMore);
                        popup.inflate(R.menu.options_menu);
                        popup.setOnMenuItemClickListener(item -> {
                            switch (item.getItemId()) {
                                case R.id.report:
                                    if (!LoginHelper.getInstance().getUserData().getId().equals("000"))
                                        DetailCampaignActivity.showReportDialog(context, popularCampaignModel.getCampaignId());
                                    return true;
                                default:
                                    return false;
                            }
                        });
                        popup.show();
                    });

                    popularCampaignHolder.binding.ivCampaignImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clickInterface != null)
                                clickInterface.onRecyclerItemClick(null, pos);
                        }
                    });


                    popularCampaignHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DetailCampaignActivity.newInstance(popularCampaignHolder.itemView.getContext(), mPopularCampaignList.get(pos));
                        }
                    });
                    popularCampaignHolder.binding.ivCampaignImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DetailCampaignActivity.newInstance(popularCampaignHolder.itemView.getContext(), mPopularCampaignList.get(pos));
                        }
                    });

                    Log.v("liketest", "onBindCalled");

                    if (popularCampaignModel.isFavourite())
                        popularCampaignHolder.binding.ivStar.setImageResource(R.drawable.ic_star_selected);
                    else
                        popularCampaignHolder.binding.ivStar.setImageResource(R.drawable.ic_star);

                    popularCampaignHolder.binding.ivStar.setOnClickListener(view -> {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(WebFields.CAMPAIGNID, popularCampaignModel.getCampaignId());

                        if (!LoginHelper.getInstance().getUserData().getId().equals("000")) {
                            LoadingHelper.getInstance().show(context);
                            if (popularCampaignModel.isFavourite()) {
                                ApiClient.getClient().unfavourite("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(),
                                        jsonObject).enqueue(
                                        new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {
                                                LoadingHelper.getInstance().dismiss();
                                                try {
                                                    if (response.isSuccessful()) {
                                                        popularCampaignHolder.binding.ivStar.setImageResource(R.drawable.ic_star);
                                                        popularCampaignModel.setFavourite(false);
                                                    }
                                                } catch (Exception e) {
                                                    Log.e("onloginex", e.getMessage());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Object> call, Throwable t) {
                                                LoadingHelper.getInstance().dismiss();
                                            }
                                        }
                                );
                            } else {
                                ApiClient.getClient().favourite("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(),
                                        jsonObject).enqueue(
                                        new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {
                                                LoadingHelper.getInstance().dismiss();
                                                try {
                                                    if (response.isSuccessful()) {
                                                        popularCampaignHolder.binding.ivStar.setImageResource(R.drawable.ic_star_selected);
                                                        popularCampaignModel.setFavourite(true);
                                                    }
                                                } catch (Exception e) {
                                                    Log.e("onloginex", e.getMessage());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Object> call, Throwable t) {
                                                LoadingHelper.getInstance().dismiss();
                                            }
                                        }
                                );
                            }
                        }
                    });

                    popularCampaignHolder.binding.ivLike.setOnClickListener(v -> {
                        Log.v("liketest", "onBindCalled " + popularCampaignModel.isLiked());

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(WebFields.CAMPAIGNID, popularCampaignModel.getCampaignId());

                        if (!LoginHelper.getInstance().getUserData().getId().equals("000")) {
                            LoadingHelper.getInstance().show(context);
                            if (popularCampaignModel.isLiked()) {
                                ApiClient.getClient().unlike("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject).enqueue(
                                        new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {
                                                LoadingHelper.getInstance().dismiss();
                                                try {
                                                    if (response.isSuccessful()) {
                                                        popularCampaignHolder.binding.ivLike.setChecked(false);
                                                        popularCampaignModel.setLiked(false);
                                                        popularCampaignModel.setLikeCount(String.valueOf(Integer.parseInt(popularCampaignModel.getLikeCount()) - 1));
                                                        popularCampaignHolder.binding.tvLikeCount.setText(popularCampaignModel.getLikeCount());
                                                    }
                                                } catch (Exception e) {
                                                    Log.e("onloginex", e.getMessage());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Object> call, Throwable t) {
                                                LoadingHelper.getInstance().dismiss();
                                            }
                                        }
                                );
                            } else {
                                ApiClient.getClient().like("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject).enqueue(
                                        new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {
                                                LoadingHelper.getInstance().dismiss();
                                                try {
                                                    if (response.isSuccessful()) {
                                                        popularCampaignHolder.binding.ivLike.setChecked(true);
                                                        popularCampaignModel.setLiked(true);
                                                        popularCampaignModel.setLikeCount(String.valueOf(Integer.parseInt(popularCampaignModel.getLikeCount()) + 1));
                                                        popularCampaignHolder.binding.tvLikeCount.setText(popularCampaignModel.getLikeCount());
                                                    }
                                                } catch (Exception e) {
                                                    Log.e("onloginex", e.getMessage());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Object> call, Throwable t) {
                                                LoadingHelper.getInstance().dismiss();
                                            }
                                        }
                                );
                            }
                        }
                    });

                    popularCampaignHolder.binding.ivShare.setOnClickListener(view -> {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "I found a nice campaign that you shall contribute to, download Wakawala now at onelink.to/wakawala and search for \"" + popularCampaignModel.getTitle() + "\".");
                        context.startActivity(Intent.createChooser(sharingIntent, "Share using"));

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(WebFields.CAMPAIGNID, popularCampaignModel.getCampaignId());

                        ApiClient.getClient().share("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject).enqueue(
                                new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        LoadingHelper.getInstance().dismiss();
                                        try {
                                            if (response.isSuccessful()) {
                                                popularCampaignModel.setShareCount(String.valueOf(Integer.parseInt(popularCampaignModel.getShareCount()) + 1));
                                                popularCampaignHolder.binding.tvShareCount.setText(popularCampaignModel.getShareCount());
                                            }
                                        } catch (Exception e) {
                                            Log.e("onloginex", e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                        LoadingHelper.getInstance().dismiss();
                                    }
                                }
                        );
                    });
                }
                break;
        }
    }

    private String getCategoryFromId(String form) {
        switch (form) {
            case "HM":
                return "Health & Medical";
            case "AE":
                return "Accidents & Emergencies";
            case "DR":
                return "Disaster Relief";
            case "FD":
                return "Food";
            case "VT":
                return "Veterans";
            case "ED":
                return "Elderly";
            case "KF":
                return "Kids & Family";
            case "SE":
                return "Schools & Education";
            case "NP":
                return "Non-Profit & Charity";
            case "RO":
                return "Religious Organizations";
            case "MF":
                return "Memorials & Funerals";
            case "PO":
                return "Politics & Public Office";
            case "PA":
                return "Pets & Animals";
            case "ST":
                return "Sports & Teams";
            case "TA":
                return "Trips & Adventures";
            case "CC":
                return "Clubs & Community";
            case "CA":
                return "Creative Art, Music & Film";
            case "OT":
                return "Others";
        }
        return "";
    }

    public void add(PopularCampaignModel mc) {
        mPopularCampaignList.add(mc);
        notifyItemInserted(mPopularCampaignList.size() - 1);
    }

    public void addAll(List<PopularCampaignModel> mcList) {
        for (PopularCampaignModel mc : mcList) {
            add(mc);
        }
    }

    public void remove(PopularCampaignModel city) {
        int position = mPopularCampaignList.indexOf(city);
        if (position > -1) {
            mPopularCampaignList.remove(position);
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
            add(new PopularCampaignModel());
        }
    }

    public void removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false;

            if (mPopularCampaignList.size() > 0) {
                int position = mPopularCampaignList.size() - 1;
                PopularCampaignModel item = getItem(position);

                if (item != null) {
                    mPopularCampaignList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        }
    }

    public PopularCampaignModel getItem(int position) {
        return mPopularCampaignList.get(position);
    }

    class PopularCampaignHolder extends RecyclerView.ViewHolder {
        private final RowCampaignPopularBinding binding;

        PopularCampaignHolder(RowCampaignPopularBinding binding) {
            super(binding.getRoot());
            this.binding = DataBindingUtil.bind(binding.getRoot());
        }
    }
}
