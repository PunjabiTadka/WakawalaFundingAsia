package com.wakawala.ui.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wakawala.R;
import com.wakawala.adapters.CampaignAdapter;
import com.wakawala.baseclasses.BaseFragment;
import com.wakawala.databinding.FragmentFavoriteBinding;
import com.wakawala.interfaces.RecyclerItemClickInterface;
import com.wakawala.model.Campaign;
import com.wakawala.model.CampaignModel;
import com.wakawala.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends BaseFragment implements RecyclerItemClickInterface<Object> {
    private FragmentFavoriteBinding mBinding;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference, mUserReference;
    private ArrayList<Campaign> mCampaignList;
    private HashMap<String, UserInfo> userInfoHashMap;
    private ArrayList<CampaignModel> campaignModels;
    private boolean isCampaignSet = false, isUsersSet = false;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        initUI();
        loadData();
        return mBinding.getRoot();
    }

    @Override
    protected void initUI() {
        campaignAdapter = new CampaignAdapter();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("campaigns");

        mUserReference = mDatabase.getReference("users").child(FirebaseAuth.getInstance().getUid() != null ? FirebaseAuth.getInstance().getUid() : "nouser").child("campaignFavourited");
        mCampaignList = new ArrayList<>();
        userInfoHashMap = new HashMap<>();
        campaignModels = new ArrayList<>();

//
//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Campaign campaign = snapshot.getValue(Campaign.class);
//                    mCampaignList.add(campaign);
//                }
//
//                isCampaignSet = true;
//                if(isUsersSet && isCampaignSet)
//                    setUpdata();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//            }
//        });
//
    }

//    private void setUpdata() {
//        loadAdapter();
//    }

    private CampaignAdapter campaignAdapter;

    private void populateAdapter(final List<String> campaignIds) {
        mBinding.rcvFavorite.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        mBinding.rcvFavorite.setAdapter(campaignAdapter);

        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserInfo userInfo = snapshot.child("userInfo").getValue(UserInfo.class);
                    userInfoHashMap.put(snapshot.getKey(), userInfo);
                }

                for (final String campaignId : campaignIds) {
                    FirebaseDatabase.getInstance().getReference("campaigns").child(campaignId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try {
                                        Campaign campaign = dataSnapshot.getValue(Campaign.class);
                                        UserInfo userInfo = userInfoHashMap.get(campaign.getUserId());

                                        CampaignModel campaignModel;
                                        if (userInfo != null) {
                                            campaignModel = (new CampaignModel(campaign,
                                                    userInfo.getUserName() != null ? userInfo.getUserName() : "",
                                                    userInfo.getUserType() != null ? userInfo.getUserType() : "",
                                                    userInfo.getProfileImageURL() != null ? userInfo.getProfileImageURL() : ""));

                                        } else {
                                            campaignModel = (new CampaignModel(campaign,
                                                    "",
                                                    "",
                                                    ""));
                                        }
                                        campaignModels.add(campaignModel);

                                        if (campaignModel != null) {
                                            Log.v("favtest", campaignModel.getCampaign().getTitle() + " added");
                                            campaignAdapter.mCampaignList.add(campaignModel);

                                            campaignAdapter.notifyDataSetChanged();
                                        } else {
                                            Log.v("favtest", "null found");
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void loadData() {
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> campaignIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println(snapshot.getKey());
                    campaignIds.add(snapshot.getKey());

                    Log.v("favtest", snapshot.getKey());
                }
                populateAdapter(campaignIds);
//                isUsersSet = true;
//                if(isUsersSet && isCampaignSet)
//                    setUpdata();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
