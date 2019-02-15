package com.wakawala.ui.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wakawala.R;
import com.wakawala.adapters.SearchHistoryAdapter;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseFragment;
import com.wakawala.databinding.FragmentSearchCampaignListBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.interfaces.RecyclerItemClickInterface;
import com.wakawala.model.SearchCampaignModel;
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
 * Use the {@link SearchCampaignListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchCampaignListFragment extends BaseFragment implements RecyclerItemClickInterface<Object> {

    private FragmentSearchCampaignListBinding mBinding;
    private ArrayList<SearchCampaignModel> searchCampaignModels;
    private SearchHistoryAdapter searchHistoryAdapter;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL = 0;
    private int skipRecord = PAGE_START;
    private int currentRecord = 0;
    private final int LIMIT = 5;

    public SearchCampaignListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static SearchCampaignListFragment newInstance(int tag) {
        Bundle args = new Bundle();
        args.putInt("tag", tag);
        SearchCampaignListFragment fragment = new SearchCampaignListFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_campaign_list, container, false);
        initUI();
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
        searchCampaignModels.clear();
        searchHistoryAdapter.notifyDataSetChanged();
        if (getArguments() != null)
            this.tag = getArguments().getInt("tag");
    }

    int tag = 0;

    @Override
    protected void initUI() {
        searchCampaignModels = new ArrayList<>();
        searchHistoryAdapter = new SearchHistoryAdapter(mContext, this, searchCampaignModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mBinding.rcvSearch.setLayoutManager(linearLayoutManager);
        mBinding.rcvSearch.setAdapter(searchHistoryAdapter);

        mBinding.rcvSearch.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                isLastPage = false;
                skipRecord += LIMIT;

                new Handler().postDelayed(() -> loadData(), 1000);
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

    String query = "";

    public void setQueryString(String query) {
        this.query = query;
        searchCampaignModels.clear();
        searchHistoryAdapter.notifyDataSetChanged();
        loadData();
    }

    @Override
    protected void loadData() {
        String url = "search/searchid?";
        String id = "id";
        String bookmark = "bookmark";
        switch (tag) {
            case 0:
                url = "search/searchid?";
                id = "id";
                bookmark = "bookmark";
                break;
            case 1:
                url = "search/searchtitle?";
                id = "title";
                bookmark = "bookmark";
                break;
            case 2:
                url = "search/searchuser?";
                id = "partialname";
                bookmark = "skip";
                break;
        }
//url + id + "=" + this.query + "&" + bookmark + "=" + skipRecord + "&take=" + LIMIT).enqueue(
        ApiClient.getClient().CampaignSearchId("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(),
                url + id + "=" + this.query + "&take=" + LIMIT).enqueue(
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

                            if (jsonObject.has(WebFields.ERROR_CODE)) {
                                Toast.makeText(mContext, jsonObject.getString(WebFields.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();

                                if (searchCampaignModels.size() == 0) {
                                    mBinding.rcvSearch.setVisibility(View.GONE);
                                    mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                                } else {
                                    mBinding.rcvSearch.setVisibility(View.VISIBLE);
                                    mBinding.tvNoDataFound.setVisibility(View.GONE);
                                }
                                return;
                            }

                            JSONArray jsonArrayResult = jsonObject.getJSONArray(WebFields.RESULTS);
                            Log.e("size", jsonArrayResult.length() + "");
                            searchHistoryAdapter.removeLoadingFooter();

                            for (int i = 0; i < jsonArrayResult.length(); i++) {
                                JSONObject jsonData = jsonArrayResult.getJSONObject(i).getJSONObject(WebFields.RECORD);

                                SearchCampaignModel searchCampaignModel = new SearchCampaignModel(
                                        jsonData.getString(WebFields.USERNAME),
                                        jsonData.getString(WebFields.USER_PROFILE_PIC),
                                        jsonData.getString(WebFields.DESCRIPTION),
                                        jsonData.getString(WebFields.CAMPAIGN_CODE)
                                );

                                if (jsonData.has(WebFields.PHOTO_LINK)) {
                                    JSONArray jsonObjectPhoto = jsonData.getJSONArray(WebFields.PHOTO_LINK);
                                    searchCampaignModel.setProfilePic(jsonObjectPhoto.getString(0));
                                }

                                searchCampaignModels.add(searchCampaignModel);
                            }

                            if (currentRecord == 0) {
                                //TOTAL = jsonObject.getInt(WebFields.TOTAL);
                                if (jsonObject.has(WebFields.PAGINATION_INFO)) {
                                    JSONObject jsonObjectPaginationInfo = jsonObject.getJSONObject(WebFields.PAGINATION_INFO);
                                    TOTAL = jsonObjectPaginationInfo.getInt(WebFields.RECORDS_COUNT);
                                }
                            }

                            currentRecord += jsonArrayResult.length();

                            if (TOTAL > searchCampaignModels.size()) {
                                if (currentRecord <= TOTAL) {
                                    if (searchCampaignModels.size() != 0)
                                        searchHistoryAdapter.addLoadingFooter();
                                } else {
                                    isLastPage = true;
                                    searchHistoryAdapter.removeLoadingFooter();
                                }
                            } else {
                                isLastPage = true;
                                searchHistoryAdapter.removeLoadingFooter();
                            }

                            searchHistoryAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("popularex", e.getMessage());
                        }

                        if (searchCampaignModels.size() == 0) {
                            mBinding.rcvSearch.setVisibility(View.GONE);
                            mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.rcvSearch.setVisibility(View.VISIBLE);
                            mBinding.tvNoDataFound.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LoadingHelper.getInstance().dismiss();
                    }
                });
    }

    @Override
    public void onRecyclerItemClick(Object object, int pos) {
        //if (getContext() != null)
    }
}
