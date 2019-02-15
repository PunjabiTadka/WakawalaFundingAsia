package com.wakawala.ui.actvities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wakawala.R;
import com.wakawala.adapters.CampaignViewPagerAdapter;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivityChartBinding;
import com.wakawala.ui.fragments.ChartFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class ChartActivity extends BaseActivity implements View.OnClickListener {

    private ActivityChartBinding mBinding;
    private CampaignViewPagerAdapter mCampaignViewPagerAdapter;

    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, ChartActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_chart);
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.app_name), true);

        setUpViewPager();

        mBinding.viewDonations.setSelected(true);


        mBinding.llDonations.setOnClickListener(this);
        mBinding.llVisits.setOnClickListener(this);
    }

    private void setUpViewPager() {



    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llDonations:

                mBinding.viewDonations.setSelected(true);

                mBinding.viewVisited.setSelected(false);
//                mBinding.viewPager.setCurrentItem(0);
                break;
            case R.id.llVisits:

                mBinding.viewDonations.setSelected(false);

                mBinding.viewVisited.setSelected(true);
//                mBinding.viewPager.setCurrentItem(1);
                break;
        }
    }
}
