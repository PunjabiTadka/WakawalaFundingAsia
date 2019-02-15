package com.wakawala.ui.fragments;


import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wakawala.R;
import com.wakawala.adapters.CampaignViewPagerAdapter;
import com.wakawala.baseclasses.BaseFragment;
import com.wakawala.databinding.FragmentCampaignListBinding;
import com.wakawala.ui.actvities.FilterActivity;
import com.wakawala.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import static com.wakawala.util.Util.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CampaignListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CampaignListFragment extends BaseFragment {

    private static final String TAG = "CampaignListFragment";
    private FragmentCampaignListBinding mBinding;
    private CampaignViewPagerAdapter mCampaignViewPagerAdapter;
    private Fragment currentFragment = null;

    public CampaignListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static CampaignListFragment newInstance() {
        CampaignListFragment fragment = new CampaignListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_campaign_list, container, false);
        initUI();
        loadData();
        return mBinding.getRoot();
    }

    @Override
    protected void initUI() {
        setUpViewPager();

        mBinding.llViews.getChildAt(0).setSelected(true);
        mBinding.tabLayout.getTabAt(0).getCustomView().setSelected(true);
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (fragments != null)
                    currentFragment = fragments.get(position);
                for (int i = 0; i < mBinding.llViews.getChildCount(); i++) {
                    mBinding.llViews.getChildAt(i).setSelected(i == position);
                    mBinding.tabLayout.getTabAt(i).getCustomView().setSelected(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filter, menu);
    }

    boolean isFilterOpen = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            isFilterOpen = true;
            FilterActivity.newInstance(mContext);
        }
        return super.onOptionsItemSelected(item);
    }

    List<Fragment> fragments = null;

    private void setUpViewPager() {
        fragments = new ArrayList<>();

        int[] mainFragments = {Constants.POPULAR, Constants.RECENT};

        for (int i = 0; i < mainFragments.length; i++) {
            switch (i) {
                case Constants.POPULAR:
                    fragments.add(PopularFragment.newInstance());
                    break;
                case Constants.RECENT:
                    fragments.add(RecentFragment.newInstance());
                    break;
            }
        }
        currentFragment = fragments.get(0);

        mCampaignViewPagerAdapter = new CampaignViewPagerAdapter(getChildFragmentManager(), fragments);
        mBinding.viewPager.setAdapter(mCampaignViewPagerAdapter);

        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        SparseIntArray tabIndicators = Util.getSparseArrayOfCampaignTabStrRes();
        Log.d(TAG, "setUpViewPager() called");
        TextView textView;
        for (int i = 0; i < tabIndicators.size(); i++) {

            textView = new TextView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            textView.setText(getString(tabIndicators.get(i)));
            textView.setTextSize(15);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.selector_black_dark_grey));
            mBinding.tabLayout.getTabAt(i).setCustomView(textView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFilterOpen) {
            if (currentFragment instanceof PopularFragment) {
                ((PopularFragment) currentFragment).filterData();
            }
            if (currentFragment instanceof RecentFragment) {
                ((RecentFragment) currentFragment).filterData();
            }
        }
    }

    @Override
    protected void loadData() {

    }

}
