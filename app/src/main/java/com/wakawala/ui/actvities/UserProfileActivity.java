package com.wakawala.ui.actvities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.wakawala.R;
import com.wakawala.adapters.CampaignViewPagerAdapter;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivityUserProfileBinding;
import com.wakawala.ui.fragments.ProfileCampaignListFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class UserProfileActivity extends BaseActivity implements View.OnClickListener {

    private ActivityUserProfileBinding mBinding;
    private CampaignViewPagerAdapter mCampaignViewPagerAdapter;
    public static UserProfileActivity userProfileActivity;

    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, UserProfileActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_user_profile);
        userProfileActivity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userProfileActivity = null;
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.app_name), true);

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

        Glide.with(mContext).load(getIntent().getStringExtra(WebFields.USER_PROFILE_PIC))
                .into(mBinding.ivFirstLetter);

        mBinding.tvName.setText(getIntent().getStringExtra(WebFields.USER__NAME));
        setUpViewPager(getIntent().getStringExtra(WebFields.USER__NAME));
    }

    private void setUpViewPager(String user) {
        List<Fragment> fragments = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            switch (i) {
                case 0:
                    fragments.add(ProfileCampaignListFragment.newInstance(false, "campaignsCreated", user));
                    break;
                case 1:
                    fragments.add(ProfileCampaignListFragment.newInstance(false, "campaignsContributed", user));
                    break;
            }
        }

        mCampaignViewPagerAdapter = new CampaignViewPagerAdapter(getSupportFragmentManager(), fragments);
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

    @Override
    protected void loadData() {

    }
}