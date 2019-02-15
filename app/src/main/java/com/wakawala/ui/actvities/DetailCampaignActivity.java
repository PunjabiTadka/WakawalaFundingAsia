package com.wakawala.ui.actvities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.wakawala.R;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivityDetailCampaignBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.model.DataMapping;
import com.wakawala.model.PopularCampaignModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCampaignActivity extends BaseActivity {

    private ActivityDetailCampaignBinding mBinding;
    static PopularCampaignModel campaignModel;

    public static void newInstance(Context context, PopularCampaignModel campaignModel) {
        DetailCampaignActivity.campaignModel = campaignModel;
        context.startActivity(new Intent(context, DetailCampaignActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_detail_campaign);
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.app_name), true);
    }

    @Override
    protected void loadData() {
        mBinding.rlComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentsActivity.newInstance(DetailCampaignActivity.this);
            }
        });
        mBinding.btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentActivity.newInstance(DetailCampaignActivity.this);
            }
        });

        TextView title = findViewById(R.id.tvCampaignTitle);

        TextView category = findViewById(R.id.tvCategory);
        ImageView ivCampaignImage = findViewById(R.id.ivCampaignImage);
        ImageView ivProfileImage = findViewById(R.id.ivProfileImage);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvType = findViewById(R.id.tvType);
        TextView tvPlace = findViewById(R.id.tvPlace);
        TextView tvLikeCount = findViewById(R.id.tvLikeCount);
        TextView tvShareCount = findViewById(R.id.tvShareCount);
        ProgressBar money = findViewById(R.id.money);
        TextView t1 = findViewById(R.id.t1);
        TextView t2 = findViewById(R.id.t2);
        CheckBox ivLike = findViewById(R.id.ivLike);
        ImageView ivShare = findViewById(R.id.ivShare);
        ImageView ivMore = findViewById(R.id.ivMore);
        final ImageView ivStar = findViewById(R.id.ivStar);
        if (campaignModel != null) {
            ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(DetailCampaignActivity.this, ivMore);
                    popup.inflate(R.menu.options_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.report:
                                    if (!LoginHelper.getInstance().getUserData().getId().equals("000"))
                                        showReportDialog(DetailCampaignActivity.this, campaignModel.getCampaignId());
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();
                }
            });

            t1.setText(" " + campaignModel.getCurrentAmount() + " of " + DataMapping.moneyAmount(campaignModel.getTargetAmount()));
            t2.setText(DataMapping.getDateAgo(campaignModel.getStartDate(), campaignModel.getEndDate()));

            mBinding.description.setText(campaignModel.getDescription());
            title.setText(campaignModel.getTitle());
            category.setText(getCategoryFromId(campaignModel.getCategory()) + " - " + campaignModel.getCategory());
            tvName.setText(campaignModel.getUserName());

            if (campaignModel.getUserType() == 2)
                tvType.setText("Organization");
            else
                tvType.setText("Individual");


            tvLikeCount.setText(campaignModel.getLikeCount());
            tvShareCount.setText(campaignModel.getShareCount());

            money.setProgress(campaignModel.getCurrentAmount());
            money.setMax(campaignModel.getTargetAmount());
            tvPlace.setText(campaignModel.getCity() + " , " + campaignModel.getCountry());

            if (campaignModel.getPhotoLink() != null && campaignModel.getPhotoLink().trim().length() != 0)
                Glide.with(this).load(campaignModel.getPhotoLink())
                        .apply(new RequestOptions().placeholder(R.drawable.place_holder).error(R.drawable.place_holder).transform(new CircleCrop()))
                        .into(ivCampaignImage);
//            Picasso.with(this)
//                    .load(campaignModel.getPhotoLink())
//                    .into(ivCampaignImage);

            if (campaignModel.getUserProfilePic() != null && campaignModel.getUserProfilePic().trim().length() != 0)
                Glide.with(this).load(campaignModel.getUserProfilePic())
                        .apply(new RequestOptions().placeholder(R.drawable.place_holder).error(R.drawable.place_holder).transform(new CircleCrop()))
                        .into(ivProfileImage);
//                Picasso.with(this)
//                        .load(campaignModel.getUserProfilePic())
//                        .into(ivProfileImage);

            if (campaignModel.isFavourite())
                ivStar.setImageResource(R.drawable.ic_star_selected);
            else
                ivStar.setImageResource(R.drawable.ic_star);

//            FirebaseDatabase
//                    .getInstance().getReference("users")
//                    .child(FirebaseAuth.getInstance().getUid() != null ? FirebaseAuth.getInstance().getUid() : "nouser")
//                    .child("campaignFavourited")
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            HashSet<String> userFavorited = new HashSet<>();
//                            try {
//                                for (DataSnapshot d1 : dataSnapshot.getChildren()) {
//                                    if (d1 != null) {
//                                        userFavorited.add(d1.getKey());
//                                    }
//                                }
//                                Log.v("liketest", new Gson().toJson(userFavorited));
//
//                                if (userFavorited != null && userFavorited.contains(campaignModel.getCampaignId()))
//                                    ivStar.setImageDrawable(ContextCompat.getDrawable(DetailCampaignActivity.this, R.drawable.ic_star_selected));
//                                else
//                                    ivStar.setImageDrawable(ContextCompat.getDrawable(DetailCampaignActivity.this, R.drawable.ic_star));
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });

            ivStar.setOnClickListener(v -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(WebFields.CAMPAIGNID, campaignModel.getCampaignId());

                if (!LoginHelper.getInstance().getUserData().getId().equals("000")) {
                    LoadingHelper.getInstance().show(this);
                    if (campaignModel.isFavourite()) {
                        ApiClient.getClient().unfavourite("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(),
                                jsonObject).enqueue(
                                new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        LoadingHelper.getInstance().dismiss();
                                        try {
                                            if (response.isSuccessful()) {
                                                ivStar.setImageResource(R.drawable.ic_star);
                                                campaignModel.setFavourite(false);
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
                                                ivStar.setImageResource(R.drawable.ic_star_selected);
                                                campaignModel.setFavourite(true);
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
//                    if (FirebaseAuth.getInstance().getUid() != null) {
//                        final DatabaseReference ref = FirebaseDatabase.getInstance()
//                                .getReference("users")
//                                .child(FirebaseAuth.getInstance().getUid()).child("campaignFavourited");
//
//                        FirebaseDatabase
//                                .getInstance().getReference("users")
//                                .child(FirebaseAuth.getInstance().getUid())
//                                .child("campaignFavourited")
//                                .addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                        HashSet<String> userFavorited = new HashSet<>();
//                                        try {
//                                            for (DataSnapshot d1 : dataSnapshot.getChildren()) {
//                                                if (d1 != null) {
//                                                    userFavorited.add(d1.getKey());
//                                                }
//                                            }
//                                            Log.v("liketest", new Gson().toJson(userFavorited));
//
//                                            if (userFavorited != null && userFavorited.contains(campaignModel.getCampaign().getId())) {
//
//                                                Log.v("usertest", "found");
//                                                ref.child(campaignModel.getCampaign().getId()).child("timestamp").removeValue();
//                                                ivStar.setImageDrawable(ContextCompat.getDrawable(DetailCampaignActivity.this, R.drawable.ic_star));
//
//                                            } else {
//
//                                                Log.v("usertest", "not found");
//                                                ivStar.setImageDrawable(ContextCompat.getDrawable(DetailCampaignActivity.this, R.drawable.ic_star_selected));
//
//                                                ref.child(campaignModel.getCampaign().getId()).child("timestamp").setValue(new Date().getTime());
//                                            }
////                                        notifyDataSetChanged();
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//
//                    } else
//                        Toast.makeText(DetailCampaignActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
            });

            ivLike.setChecked(campaignModel.isLiked());

            ivLike.setOnClickListener(v -> {
                Log.v("liketest", "onBindCalled " + campaignModel.isLiked());

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(WebFields.CAMPAIGNID, campaignModel.getCampaignId());

                if (!LoginHelper.getInstance().getUserData().getId().equals("000")) {
                    LoadingHelper.getInstance().show(this);
                    if (campaignModel.isLiked()) {
                        ApiClient.getClient().unlike("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject).enqueue(
                                new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        LoadingHelper.getInstance().dismiss();
                                        try {
                                            if (response.isSuccessful()) {
                                                ivLike.setChecked(false);
                                                campaignModel.setLiked(false);
                                                campaignModel.setLikeCount(String.valueOf(Integer.parseInt(campaignModel.getLikeCount()) - 1));
                                                tvLikeCount.setText(campaignModel.getLikeCount());
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
                                                ivLike.setChecked(true);
                                                campaignModel.setLiked(true);
                                                campaignModel.setLikeCount(String.valueOf(Integer.parseInt(campaignModel.getLikeCount()) + 1));
                                                tvLikeCount.setText(campaignModel.getLikeCount());
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

            ivShare.setOnClickListener(view -> {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "I found a nice campaign that you shall contribute to, download Wakawala now at onelink.to/wakawala and search for \"" + campaignModel.getTitle() + "\".");
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(WebFields.CAMPAIGNID, campaignModel.getCampaignId());

                ApiClient.getClient().share("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject).enqueue(
                        new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                try {
                                    if (response.isSuccessful()) {
                                        campaignModel.setShareCount(String.valueOf(Integer.parseInt(campaignModel.getShareCount()) + 1));
                                        tvShareCount.setText(campaignModel.getShareCount());
                                    }
                                } catch (Exception e) {
                                    Log.e("onloginex", e.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                            }
                        }
                );
            });
        }
    }

    public static void showReportDialog(Context context, int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(dialogView);
        View dialogTitle = inflater.inflate(R.layout.dialog_title, null);
        builder.setCustomTitle(dialogTitle);
        ((TextView) dialogTitle.findViewById(R.id.tvTitle)).setText("Enter Reporting Text");

        final EditText editText = (EditText) dialogView.findViewById(R.id.etText);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editText.setHint("Enter Report Text");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (editText != null) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty(WebFields.CAMPAIGNID, id);
                    jsonObject.addProperty(WebFields.REPORT, editText.getText().toString());

                    ApiClient.getClient().report("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken()
                            , jsonObject).enqueue(
                            new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    LoadingHelper.getInstance().dismiss();
                                    try {
                                        if (response.isSuccessful()) {
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
        builder.setTitle("Enter Reporting Text");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
}
