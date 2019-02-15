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
import com.wakawala.databinding.RowFragmentProfileCampaignBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.interfaces.RecyclerItemClickInterface;
import com.wakawala.model.PopularCampaignModel;
import com.wakawala.ui.actvities.ChartActivity;
import com.wakawala.ui.actvities.DetailCampaignActivity;
import com.wakawala.viewholder.PaginationViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileCampaignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;

    private Context context;
    private RecyclerItemClickInterface<Object> clickInterface;
    public ArrayList<PopularCampaignModel> profileCampaignModels;
    private boolean isProfile;

    public ProfileCampaignAdapter(Context context, RecyclerItemClickInterface<Object> clickInterface,
                                  ArrayList<PopularCampaignModel> profileCampaignModels, boolean isProfile) {
        this.isProfile = isProfile;
        this.context = context;
        this.clickInterface = clickInterface;
        this.profileCampaignModels = profileCampaignModels;
    }

    @Override
    public int getItemCount() {
        return profileCampaignModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == profileCampaignModels.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case ITEM:
                RowFragmentProfileCampaignBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_fragment_profile_campaign, parent, false);
                return new ProfileCampaignHolder(binding);
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
                ProfileCampaignHolder recentCampaignHolder = (ProfileCampaignHolder) holder;
                PopularCampaignModel recentCampaignModel = profileCampaignModels.get(pos);
                if (recentCampaignHolder.binding != null) {
                    recentCampaignHolder.binding.setRecentCampaignModel(profileCampaignModels.get(pos));

                    Glide.with(context).load(profileCampaignModels.get(pos).getUserProfilePic())
                            .apply(new RequestOptions().placeholder(R.drawable.place_holder)
                                    .error(R.drawable.place_holder).circleCrop().transform(new CircleCrop()))
                            .into(recentCampaignHolder.binding.ivProfileImage);

                    if (isProfile)
                        recentCampaignHolder.binding.ivStar.setVisibility(View.VISIBLE);
                    else
                        recentCampaignHolder.binding.ivStar.setVisibility(View.INVISIBLE);

                    recentCampaignHolder.binding.tvCategory.setText(getCategoryFromId(recentCampaignModel.getCategory()) + " - " + recentCampaignModel.getCategory());

                    if (recentCampaignModel.getUserType() == 2)
                        recentCampaignHolder.binding.tvType.setText("Organization");
                    else
                        recentCampaignHolder.binding.tvType.setText("Individual");

                    recentCampaignHolder.binding.ivChart.setVisibility(View.INVISIBLE);

                    recentCampaignHolder.binding.ivMore.setOnClickListener(v -> {
                        PopupMenu popup = new PopupMenu(recentCampaignHolder.itemView.getContext(), recentCampaignHolder.binding.ivMore);
                        popup.inflate(R.menu.options_menu);
                        popup.setOnMenuItemClickListener(item -> {
                            switch (item.getItemId()) {
                                case R.id.report:
                                    if (!LoginHelper.getInstance().getUserData().getId().equals("000"))
                                        DetailCampaignActivity.showReportDialog(context, recentCampaignModel.getCampaignId());
                                    return true;
                                default:
                                    return false;
                            }
                        });
                        popup.show();
                    });
                    recentCampaignHolder.binding.ivChart.setOnClickListener(v -> {
                        ChartActivity.newInstance(recentCampaignHolder.binding.ivChart.getContext());
                    });

                    recentCampaignHolder.binding.ivCampaignImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clickInterface != null)
                                clickInterface.onRecyclerItemClick(null, pos);
                        }
                    });

                    recentCampaignHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DetailCampaignActivity.newInstance(recentCampaignHolder.itemView.getContext(), profileCampaignModels.get(pos));
                        }
                    });
                    recentCampaignHolder.binding.ivCampaignImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DetailCampaignActivity.newInstance(recentCampaignHolder.itemView.getContext(), profileCampaignModels.get(pos));
                        }
                    });

                    Log.v("liketest", "onBindCalled");

                    if (recentCampaignModel.isFavourite())
                        recentCampaignHolder.binding.ivStar.setImageResource(R.drawable.ic_star_selected);
                    else
                        recentCampaignHolder.binding.ivStar.setImageResource(R.drawable.ic_star);

                    recentCampaignHolder.binding.ivStar.setOnClickListener(view -> {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(WebFields.CAMPAIGNID, recentCampaignModel.getCampaignId());

                        if (!LoginHelper.getInstance().getUserData().getId().equals("000")) {
                            LoadingHelper.getInstance().show(context);
                            if (recentCampaignModel.isFavourite()) {
                                ApiClient.getClient().unfavourite("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(),
                                        jsonObject).enqueue(
                                        new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {
                                                LoadingHelper.getInstance().dismiss();
                                                try {
                                                    if (response.isSuccessful()) {
                                                        recentCampaignHolder.binding.ivStar.setImageResource(R.drawable.ic_star);
                                                        recentCampaignModel.setFavourite(false);
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
                                                        recentCampaignHolder.binding.ivStar.setImageResource(R.drawable.ic_star_selected);
                                                        recentCampaignModel.setFavourite(true);
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

                    recentCampaignHolder.binding.ivLike.setOnClickListener(v -> {
                        Log.v("liketest", "onBindCalled " + recentCampaignModel.isLiked());

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(WebFields.CAMPAIGNID, recentCampaignModel.getCampaignId());

                        if (!LoginHelper.getInstance().getUserData().getId().equals("000")) {
                            LoadingHelper.getInstance().show(context);
                            if (recentCampaignModel.isLiked()) {
                                ApiClient.getClient().unlike("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject).enqueue(
                                        new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {
                                                LoadingHelper.getInstance().dismiss();
                                                try {
                                                    if (response.isSuccessful()) {
                                                        recentCampaignHolder.binding.ivLike.setChecked(false);
                                                        recentCampaignModel.setLiked(false);

                                                        recentCampaignModel.setLikeCount(String.valueOf(Integer.parseInt(recentCampaignModel.getLikeCount()) - 1));
                                                        recentCampaignHolder.binding.tvLikeCount.setText(recentCampaignModel.getLikeCount());
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
                                                        recentCampaignHolder.binding.ivLike.setChecked(true);
                                                        recentCampaignModel.setLiked(true);
                                                        recentCampaignModel.setLikeCount(String.valueOf(Integer.parseInt(recentCampaignModel.getLikeCount()) + 1));
                                                        recentCampaignHolder.binding.tvLikeCount.setText(recentCampaignModel.getLikeCount());
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

                    recentCampaignHolder.binding.ivShare.setOnClickListener(view -> {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "I found a nice campaign that you shall contribute to, download Wakawala now at onelink.to/wakawala and search for \"" + recentCampaignModel.getTitle() + "\".");
                        context.startActivity(Intent.createChooser(sharingIntent, "Share using"));

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(WebFields.CAMPAIGNID, recentCampaignModel.getCampaignId());

                        ApiClient.getClient().share("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject).enqueue(
                                new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        LoadingHelper.getInstance().dismiss();
                                        try {
                                            if (response.isSuccessful()) {
                                                recentCampaignModel.setShareCount(String.valueOf(Integer.parseInt(recentCampaignModel.getShareCount()) + 1));
                                                recentCampaignHolder.binding.tvShareCount.setText(recentCampaignModel.getShareCount());
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
        profileCampaignModels.add(mc);
        notifyItemInserted(profileCampaignModels.size() - 1);
    }

    public void addAll(List<PopularCampaignModel> mcList) {
        for (PopularCampaignModel mc : mcList) {
            add(mc);
        }
    }

    public void remove(PopularCampaignModel city) {
        int position = profileCampaignModels.indexOf(city);
        if (position > -1) {
            profileCampaignModels.remove(position);
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

            if (profileCampaignModels.size() > 0) {
                int position = profileCampaignModels.size() - 1;
                PopularCampaignModel item = getItem(position);

                if (item != null) {
                    profileCampaignModels.remove(position);
                    notifyItemRemoved(position);
                }
            }
        }
    }

    public PopularCampaignModel getItem(int position) {
        return profileCampaignModels.get(position);
    }

    class ProfileCampaignHolder extends RecyclerView.ViewHolder {
        private final RowFragmentProfileCampaignBinding binding;

        ProfileCampaignHolder(RowFragmentProfileCampaignBinding binding) {
            super(binding.getRoot());
            this.binding = DataBindingUtil.bind(binding.getRoot());
        }
    }
}
