package com.wakawala.ui.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.wakawala.ApplicationLoader;
import com.wakawala.R;
import com.wakawala.adapters.PopularCampaignAdapter;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseFragment;
import com.wakawala.databinding.FragmentPopularBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.interfaces.RecyclerItemClickInterface;
import com.wakawala.model.PopularCampaignModel;
import com.wakawala.ui.actvities.FilterActivity;
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
 * Use the {@link PopularFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularFragment extends BaseFragment implements RecyclerItemClickInterface<Object> {

    //private FirebaseDatabase mDatabase;
    //private DatabaseReference mDatabaseReference, mUserReference;
    //private ArrayList<Campaign> mCampaignList;
    //private boolean isCampaignSet = false, isUsersSet = false;
    //private HashMap<String, UserInfo> userInfoHashMap;

    private FragmentPopularBinding mBinding;
    private ArrayList<PopularCampaignModel> popularCampaignModels;
    private PopularCampaignAdapter popularCampaignAdapter;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL = 0;
    private int skipRecord = PAGE_START;
    private int currentRecord = 0;
    private final int LIMIT = 5;

    public PopularFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static PopularFragment newInstance() {
        PopularFragment fragment = new PopularFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_popular, container, false);
        initUI();
        loadData();
        return mBinding.getRoot();
    }

    @Override
    protected void initUI() {
        popularCampaignModels = new ArrayList<>();
        popularCampaignAdapter = new PopularCampaignAdapter(mContext, this, popularCampaignModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setAdapter(popularCampaignAdapter);

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

//        mDatabase = FirebaseDatabase.getInstance();
//        mDatabaseReference = mDatabase.getReference("campaigns");
//        mUserReference = mDatabase.getReference("users");
//        mCampaignList = new ArrayList<>();
        //userInfoHashMap = new HashMap<>();
//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Campaign campaign = snapshot.getValue(Campaign.class);
//                    mCampaignList.add(campaign);
//                }
//
//                isCampaignSet = true;
//                if (isUsersSet && isCampaignSet)
//                    setUpdata();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//            }
//        });
    }

    String url = "";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isLoading = false;
        isLastPage = false;
        TOTAL = 0;
        skipRecord = PAGE_START;
        currentRecord = 0;
        popularCampaignModels.clear();
        popularCampaignAdapter.notifyDataSetChanged();
        url = "campaign/popular?";
        loadData("skip=" + skipRecord + "&take=" + LIMIT);
    }

    //    private void setUpdata() {
//        for (Campaign campaign : mCampaignList) {
//            if (userInfoHashMap.containsKey(campaign.getUserId())) {
//                UserInfo userInfo = userInfoHashMap.get(campaign.getUserId());
//                campaignModels.add(new CampaignModel(campaign,
//                        userInfo.getUserName(),
//                        userInfo.getUserType(),
//                        userInfo.getProfileImageURL()));
//            }
//        }
//
//        loadAdapter();
//    }

//    private void loadAdapter() {
//        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
//        mBinding.recyclerView.setAdapter(new CampaignAdapter(this, campaignModels));
//    }

    @Override
    public void onResume() {
        super.onResume();
        popularCampaignAdapter.notifyDataSetChanged();
    }

    public void filterData() {
        isLoading = false;
        isLastPage = false;
        TOTAL = 0;
        skipRecord = PAGE_START;
        currentRecord = 0;
        popularCampaignModels.clear();
        popularCampaignAdapter.notifyDataSetChanged();

        String url = "skip=" + skipRecord + "&take=" + LIMIT;

        String[] country = ApplicationLoader.getInstance().getCountry().split(",,");
        String[] category = ApplicationLoader.getInstance().getCategory().split(",,");

        if (country.length > 0)
            for (int i = 0; i < country.length; i++) {
                if (!country[i].isEmpty())
                    url += "&" + WebFields.COUNTRY + "=" + country[i];
            }

        if (category.length > 0)
            for (int i = 0; i < category.length; i++) {
                if (!category[i].isEmpty())
                    url += "&" + WebFields.CATEGORY + "=" + FilterActivity.getShortFormCategory(category[i]);
            }

        loadData(url);
    }

    private void loadData(String param) {
        String token = LoginHelper.getInstance().getUserData().getAuthToken();
        if (!token.isEmpty())
            token = "Bearer " + token;
        ApiClient.getClient().getPopularCampaign(token,
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
                            if (jsonObject.has(WebFields.RESULT)) {
                                JSONArray jsonArrayResult = jsonObject.getJSONArray(WebFields.RESULT);
                                Log.e("size", jsonArrayResult.length() + "");
                                popularCampaignAdapter.removeLoadingFooter();

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
                                            "",
                                            jsonData.getString(WebFields.DESCRIPTION)
                                    );

                                    if(jsonData.has(WebFields.PHOTO_LINK)){
                                        JSONArray jsonArrayPhoto = jsonData.getJSONArray(WebFields.PHOTO_LINK);
                                        if(jsonArrayPhoto.length()>0){
                                            popularCampaignModel.setPhotoLink(jsonArrayPhoto.getString(0));
                                        }
                                    }

                                    popularCampaignModels.add(popularCampaignModel);
                                }

                                if (currentRecord == 0)
                                    TOTAL = jsonObject.getInt(WebFields.TOTAL);

                                currentRecord += jsonArrayResult.length();
                                if (TOTAL > popularCampaignModels.size()) {

                                    if (currentRecord <= TOTAL) {
                                        if (popularCampaignModels.size() != 0)
                                            popularCampaignAdapter.addLoadingFooter();
                                    } else {
                                        isLastPage = true;
                                        popularCampaignAdapter.removeLoadingFooter();
                                    }
                                } else {
                                    isLastPage = true;
                                    popularCampaignAdapter.removeLoadingFooter();
                                }
                            }


                            popularCampaignAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("popularex", e.getMessage());
                        }

                        if (popularCampaignModels.size() == 0) {
                            mBinding.recyclerView.setVisibility(View.GONE);
                            mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.recyclerView.setVisibility(View.VISIBLE);
                            mBinding.tvNoDataFound.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LoadingHelper.getInstance().dismiss();
                    }
                });

//        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    UserInfo userInfo = snapshot.child("userInfo").getValue(UserInfo.class);
//                    userInfoHashMap.put(snapshot.getKey(), userInfo);
//                }
//
//                isUsersSet = true;
//                if (isUsersSet && isCampaignSet)
//                    setUpdata();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onRecyclerItemClick(Object object, int pos) {
//        CampaignModel campaign = campaignModels.get(pos);
//        if (campaign != null) {
//            Log.v("detailstest", campaign.getCampaign().getTitle());
//            if (getContext() != null)
//                DetailCampaignActivity.newInstance(getContext(), campaign);
//        }
    }
}
