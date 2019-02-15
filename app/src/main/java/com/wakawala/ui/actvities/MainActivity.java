package com.wakawala.ui.actvities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.Toast;

import com.wakawala.R;
import com.wakawala.adapters.MainViewPagerAdapter;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivityMainBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.ui.fragments.AddCampaignFragment;
import com.wakawala.ui.fragments.CampaignListFragment;
import com.wakawala.ui.fragments.FavoriteFragment;
import com.wakawala.ui.fragments.ProfileFragment;
import com.wakawala.ui.fragments.SearchFragment;
import com.wakawala.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import static com.wakawala.util.Util.Constants;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;
    private MainViewPagerAdapter mMainViewPagerAdapter;

    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_main);

//        if (Util.isNetworkConnected(this)) {
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty(WebFields.DEVICE_ID, Util.getDeviceToken(this));
//            jsonObject.addProperty(WebFields.DEVICE_TYPE, 1);
//            jsonObject.addProperty(WebFields.REGISTRATION_ID, FirebaseInstanceId.getInstance().getToken());
//            jsonObject.addProperty(WebFields.GUI_VERSION, BuildConfig.VERSION_NAME);
//            ApiClient.getClient().registerDevice(LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject).enqueue(
//                    new Callback<Object>() {
//                        @Override
//                        public void onResponse(Call<Object> call, Response<Object> response) {
//                            LoadingHelper.getInstance().dismiss();
//                            try {
//                                String strRespResult = "";
//                                if (response.isSuccessful()) {
//                                    strRespResult = new Gson().toJson(response.body());
//                                } else {
//                                    try {
//                                        strRespResult = response.errorBody().string();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            } catch (Exception e) {
//                                Log.e("onloginex", e.getMessage());
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Object> call, Throwable t) {
//                        }
//                    }
//            );
//        }
//        if (Util.getLocalValue(this, "currency") == null)
//            startActivity(new Intent(this, SelectCurrencyActivity.class));
        //dateTemp();
    }

    public static ViewPager viewPager;

    @Override
    protected void initUI() {
        mBinding = getBinding();

        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.app_name), false);

        setUpViewPager();

        viewPager = mBinding.viewPager;

        mBinding.includedToolbar.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                    || (actionId == EditorInfo.IME_ACTION_DONE)) {
                if (fragments.get(currentPosition) instanceof SearchFragment) {
                    //Toast.makeText(mContext, "sfkdjfkdf " + mBinding.includedToolbar.etSearch.getText().toString(), Toast.LENGTH_SHORT).show();
                    ((SearchFragment) fragments.get(currentPosition)).setSearchString(mBinding.includedToolbar.etSearch.getText().toString());
                }
            }
            return false;
        });
    }

    List<Fragment> fragments;

    private void setUpViewPager() {
        fragments = new ArrayList<>();

        int[] mainFragments = {Constants.CAMPAIGN_LIST, Constants.SEARCH, Constants.ADD_CAMPAIGN,
                Constants.FAVORITE, Constants.PROFILE};

        for (int i = 0; i < mainFragments.length; i++) {
            switch (i) {
                case Constants.CAMPAIGN_LIST:
                    fragments.add(CampaignListFragment.newInstance());
                    break;
                case Constants.SEARCH:
                    fragments.add(SearchFragment.newInstance());
                    break;
                case Constants.ADD_CAMPAIGN:
                    fragments.add(AddCampaignFragment.newInstance());
                    break;
                case Constants.FAVORITE:
                    fragments.add(FavoriteFragment.newInstance());
                    break;
                case Constants.PROFILE:
                    fragments.add(ProfileFragment.newInstance());
                    break;
            }
        }

        mMainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragments);
        mBinding.viewPager.setAdapter(mMainViewPagerAdapter);
//        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                switch (tab.getPosition()) {
//                    case 2:
//                        if (FirebaseAuth.getInstance().getCurrentUser() != null)
//                            fragments.add(AddCampaignFragment.newInstance());
//                        else {
//                            startActivity(new Intent(MainActivity.this, LoginOptionActivity.class));
//                            finish();
//                            Toast.makeText(mContext, "Please sign in to contribute", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
                switch (position) {
                    case Constants.FAVORITE: {
                        changeToolbarTitle(mBinding.includedToolbar.toolbar, getString(R.string.favorite));
                        break;
                    }

                    default:
                        changeToolbarTitle(mBinding.includedToolbar.toolbar, getString(R.string.app_name));
                        break;
                }
                currentPosition = position;
                mBinding.includedToolbar.tvToolbar.setVisibility(View.VISIBLE);
                mBinding.includedToolbar.etSearch.setVisibility(View.GONE);
                switch (position) {
                    case 1:
                        mBinding.includedToolbar.tvToolbar.setVisibility(View.GONE);
                        mBinding.includedToolbar.etSearch.setVisibility(View.VISIBLE);
                        break;
                    case 2:
//                        if (FirebaseAuth.getInstance().getCurrentUser() != null)
//                            fragments.add(AddCampaignFragment.newInstance());
//                        else {
//                            startActivity(new Intent(MainActivity.this, LoginOptionActivity.class));
//                            finish();
//                            Toast.makeText(mContext, "Please sign in to contribute", Toast.LENGTH_SHORT).show();
//                        }

                        if (LoginHelper.getInstance().isLoggedIn() && !LoginHelper.getInstance().getUserData().getId().equals("000")) {
                            fragments.add(AddCampaignFragment.newInstance());
                        } else {
                            startActivity(new Intent(MainActivity.this, LoginOptionActivity.class));
                            finish();
                            Toast.makeText(mContext, "Please sign in to contribute", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        SparseIntArray tabIndicators = Util.getSparseArrayOfMainTabDrawables();
        ImageView imageView;
        for (int i = 0; i < tabIndicators.size(); i++) {
            imageView = new ImageView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, tabIndicators.get(i)));
            mBinding.tabLayout.getTabAt(i).setCustomView(imageView);
        }
        mBinding.viewPager.setCurrentItem(0);
    }

    private int currentPosition = 0;

    @Override
    protected void loadData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fragments.get(currentPosition).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
}
