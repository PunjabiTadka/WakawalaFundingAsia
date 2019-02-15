package com.wakawala.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.wakawala.R;
import com.wakawala.adapters.CampaignViewPagerAdapter;
import com.wakawala.api.ApiClient;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseFragment;
import com.wakawala.databinding.FragmentProfileBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.ui.actvities.EditProfileActivity;
import com.wakawala.ui.actvities.SettingsActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private FragmentProfileBinding mBinding;
    private CampaignViewPagerAdapter mCampaignViewPagerAdapter;

    public static ProfileFragment profileFragment;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        profileFragment = this;
        setHasOptionsMenu(true);
        initUI();
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                SettingsActivity.newInstance(mContext);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUI() {
        mBinding.viewCreated.setSelected(true);
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.viewCreated.setSelected(position == 0);
                mBinding.viewContributed.setSelected(position == 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBinding.llCreated.setOnClickListener(this);
        mBinding.llContributed.setOnClickListener(this);

        if (LoginHelper.getInstance().isLoggedIn()) {
            mBinding.tvEditProfile.setVisibility(View.VISIBLE);
            mBinding.tvEditProfile.setOnClickListener(v -> startActivity(new Intent(getContext(), EditProfileActivity.class)));
        } else {
            mBinding.tvEditProfile.setVisibility(View.GONE);
        }

//        String userId = FirebaseAuth.getInstance().getUid();
//        if (userId != null) {
//            FirebaseDatabase.getInstance()
//                    .getReference("users")
//                    .child(userId)
//                    .child("userInfo")
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            try {
//                                UserInfo currentUserInfo = dataSnapshot.getValue(UserInfo.class);
//                                if (currentUserInfo != null) {
//                                    if (currentUserInfo.getUserName() != null)
//                                        mBinding.tvName.setText(currentUserInfo.getUserName());
//                                    Picasso.with(getContext())
//                                            .load(currentUserInfo.getProfileImageURL())
//                                            .into(mBinding.tvFirstLetter);
//                                }
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
//
//            mBinding.tvEditProfile.setVisibility(View.VISIBLE);
//            mBinding.tvEditProfile.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(getContext(), EditProfileActivity.class));
//                }
//            });
//
//
//        } else {
//            mBinding.tvEditProfile.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void loadData() {
        ApiClient.getClient().getProfile("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken()).enqueue(
                new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        try {
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

                            Glide.with(mContext).load(jsonObject.getString(WebFields.PROFILEPIC))
                                    .apply(new RequestOptions().error(R.drawable.place_holder).placeholder(R.drawable.place_holder))
                                    .into(mBinding.ivFirstLetter);

                            mBinding.tvName.setText(jsonObject.getString(WebFields.USER__NAME));
                            setUpViewPager(jsonObject.getString(WebFields.USER__NAME));
                        } catch (Exception e) {
                            Log.e("asdf", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                }
        );
    }

    private void setUpViewPager(String user) {
        List<Fragment> fragments = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            switch (i) {
                case 0:
                    fragments.add(ProfileCampaignListFragment.newInstance(true, "campaignsCreated", user));
                    break;
                case 1:
                    fragments.add(ProfileCampaignListFragment.newInstance(true, "campaignsContributed", user));
                    break;
            }
        }

        mCampaignViewPagerAdapter = new CampaignViewPagerAdapter(getChildFragmentManager(), fragments);
        mBinding.viewPager.setAdapter(mCampaignViewPagerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llCreated:
                mBinding.viewPager.setCurrentItem(0);
                break;
            case R.id.llContributed:
                mBinding.viewPager.setCurrentItem(1);
                break;
        }
    }

    public void setLengthData(String tag, int length) {
        if (tag.equals("campaignsCreated"))
            mBinding.createdCount.setText(String.valueOf(length));
        if (tag.equals("campaignsContributed"))
            mBinding.contributedCount.setText(String.valueOf(length));
    }
}
