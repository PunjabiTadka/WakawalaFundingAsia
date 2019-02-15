package com.wakawala.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wakawala.R;
import com.wakawala.adapters.CampaignViewPagerAdapter;
import com.wakawala.baseclasses.BaseFragment;
import com.wakawala.databinding.FragmentSearchBinding;
import com.wakawala.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends BaseFragment {

    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding mBinding;
    private CampaignViewPagerAdapter mCampaignViewPagerAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        initUI();
        loadData();
        return mBinding.getRoot();
    }

    int currentPosition = 0;

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
                currentPosition = position;
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

    List<Fragment> fragments = null;

    private void setUpViewPager() {
        fragments = new ArrayList<>();

        int[] mainFragments = {Util.Constants.CAMPAIGN_ID, Util.Constants.CAMPAIGN_NAME, Util.Constants.USERNAME};

        for (int i = 0; i < mainFragments.length; i++) {
            switch (i) {
                case Util.Constants.CAMPAIGN_ID:
                    fragments.add(SearchCampaignListFragment.newInstance(0));
                    break;
                case Util.Constants.CAMPAIGN_NAME:
                    fragments.add(SearchCampaignListFragment.newInstance(1));
                    break;
                case Util.Constants.USERNAME:
                    fragments.add(SearchUsernameCampaignListFragment.newInstance());
                    break;
            }
        }

        mCampaignViewPagerAdapter = new CampaignViewPagerAdapter(getChildFragmentManager(), fragments);
        mBinding.viewPager.setAdapter(mCampaignViewPagerAdapter);

        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        SparseIntArray tabIndicators = Util.getSparseArrayOfSearchTabStrRes();
        Log.d(TAG, "setUpViewPager() called");
        TextView textView;
        for (int i = 0; i < tabIndicators.size(); i++) {

            textView = new TextView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            textView.setText(getString(tabIndicators.get(i)));
            textView.setTextSize(14);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.selector_black_dark_grey));
            mBinding.tabLayout.getTabAt(i).setCustomView(textView);
        }
    }

    public void setSearchString(String string) {
        if (fragments.get(currentPosition) != null)
            switch (currentPosition) {
                case 0:
                case 1:
                    ((SearchCampaignListFragment) fragments.get(currentPosition)).setQueryString(string);
                    break;
                case 2:
                    ((SearchUsernameCampaignListFragment) fragments.get(currentPosition)).setQueryString(string);
                    break;
            }
    }

    @Override
    protected void loadData() {

    }
}
