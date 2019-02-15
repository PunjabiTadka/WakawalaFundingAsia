package com.wakawala.ui.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.wakawala.R;
import com.wakawala.adapters.ProfileCampaignAdapter;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseFragment;
import com.wakawala.databinding.FragmentProfileCampaignBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.interfaces.RecyclerItemClickInterface;
import com.wakawala.model.PopularCampaignModel;
import com.wakawala.ui.actvities.ChartActivity;
import com.wakawala.ui.actvities.UserProfileActivity;
import com.wakawala.view.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileCampaignListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileCampaignListFragment extends BaseFragment implements RecyclerItemClickInterface<Object> {

    private FragmentProfileCampaignBinding mBinding;
    private ArrayList<PopularCampaignModel> profileCampaignModels;
    private ProfileCampaignAdapter profileCampaignAdapter;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL = 0;
    private int skipRecord = PAGE_START;
    private int currentRecord = 0;
    private final int LIMIT = 5;

    public ProfileCampaignListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ProfileCampaignListFragment newInstance(boolean isProfile, String campaignType, String user) {
        Bundle args = new Bundle();
        args.putBoolean("isProfile", isProfile);
        args.putString("campaignType", campaignType);
        args.putString("user", user);
        ProfileCampaignListFragment fragment = new ProfileCampaignListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_campaign, container, false);
        initUI();
        loadData();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isLoading = false;
        isLastPage = false;
        TOTAL = 0;
        skipRecord = PAGE_START;
        currentRecord = 0;
        profileCampaignModels.clear();
        profileCampaignAdapter.notifyDataSetChanged();

        String campaignType = getArguments().getString("campaignType");
        String user = getArguments().getString("user");
        if (campaignType != null) {

            Log.v("proftest", "campaignType not null " + campaignType);

            if (campaignType.equals("campaignsCreated")) {
                url = "campaign/user?targetUsername=" + user + "&";
            } else {
                url = "donation/donatedlist?targetUsername=" + user + "&";
            }
            loadData("skip=" + skipRecord + "&take=" + LIMIT);
        } else {
            Log.v("proftest", "campaignType null");
        }
    }

    String url = "";

    @Override
    protected void initUI() {
        profileCampaignModels = new ArrayList<>();
        profileCampaignAdapter = new ProfileCampaignAdapter(mContext, this, profileCampaignModels, getArguments().getBoolean("isProfile"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setAdapter(profileCampaignAdapter);

        mBinding.recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                isLastPage = false;
                skipRecord += LIMIT;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData("skip=" + skipRecord + "&take=" + LIMIT);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void loadData(String param) {
        ApiClient.getClient().GetCreatedCampaignByUser("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(),
                url + param).enqueue(
                new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        LoadingHelper.getInstance().dismiss();
                        try {
                            isLoading = false;
                            String strRespResult = "";
                            if (response.isSuccessful()) {
                                strRespResult = new Gson().toJson(response.body());
                            } else {
                                try {
                                    strRespResult = response.errorBody().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            JSONObject jsonObject = new JSONObject(strRespResult);

                            JSONArray jsonArrayResult = jsonObject.getJSONArray(WebFields.RESULT);
                            Log.e("size", jsonArrayResult.length() + "");
                            profileCampaignAdapter.removeLoadingFooter();

                            for (int i = 0; i < jsonArrayResult.length(); i++) {
                                JSONObject jsonData = jsonArrayResult.getJSONObject(i).getJSONObject(WebFields.RECORD);

                                PopularCampaignModel popularCampaignModel = new PopularCampaignModel(
                                        jsonData.getInt(WebFields.CAMPAIGN_ID),
                                        jsonData.getString(WebFields.USERNAME),
                                        jsonData.getString(WebFields.USER_PROFILE_PIC),
                                        jsonData.getInt(WebFields.USERTYPE),
                                        jsonData.getInt(WebFields.LIKE_COUNT) + "",
                                        jsonData.getInt(WebFields.SHARE_COUNT) + "",
                                        jsonData.getInt(WebFields.COMMENT_COUNT) + "",
                                        jsonData.getString(WebFields.CITY),
                                        jsonData.getString(WebFields.COUNTRY),
                                        jsonData.getBoolean(WebFields.IS_LIKED),
                                        jsonData.getBoolean(WebFields.IS_FAVOURITE),
                                        jsonData.getLong(WebFields.START_DATE),
                                        jsonData.getLong(WebFields.END_DATE),
                                        jsonData.getString(WebFields.CATEGORY),
                                        jsonData.getString(WebFields.TITLE),
                                        jsonData.getInt(WebFields.CURRENT_AMOUNT),
                                        jsonData.getInt(WebFields.TARGET_AMOUNT),
                                        "https://wakawalastaging.blob.core.windows.net/wakawala-staging/425ca2b2-6ef4-487b-8b00-2ae92f4c79ac.png",
                                        jsonData.getString(WebFields.DESCRIPTION)
                                );

                                profileCampaignModels.add(popularCampaignModel);
                            }

                            if (currentRecord == 0)
                                TOTAL = jsonObject.getInt(WebFields.TOTAL);

                            currentRecord += jsonArrayResult.length();

                            if (TOTAL > profileCampaignModels.size()) {
                                if (currentRecord <= TOTAL) {
                                    if (profileCampaignModels.size() != 0)
                                        profileCampaignAdapter.addLoadingFooter();
                                } else {
                                    isLastPage = true;
                                    profileCampaignAdapter.removeLoadingFooter();
                                }
                            } else {
                                isLastPage = true;
                                profileCampaignAdapter.removeLoadingFooter();
                            }

                            profileCampaignAdapter.notifyDataSetChanged();

                            if (getArguments().getBoolean("isProfile"))
                                ProfileFragment.profileFragment.setLengthData(getArguments().getString("campaignType"), profileCampaignModels.size());
                            else
                                UserProfileActivity.userProfileActivity.setLengthData(getArguments().getString("campaignType"), profileCampaignModels.size());
                        } catch (Exception e) {
                            Log.e("popularex", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LoadingHelper.getInstance().dismiss();
                    }
                });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onRecyclerItemClick(Object object, int pos) {
        if (getContext() != null)
            ChartActivity.newInstance(getContext());
    }
}
