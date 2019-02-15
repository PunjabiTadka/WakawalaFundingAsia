package com.wakawala.adapters;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CampaignViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mListFragments;

    public CampaignViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mListFragments = new ArrayList<>(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragments.get(position);
    }

    @Override
    public int getCount() {
        return mListFragments.size();
    }
}