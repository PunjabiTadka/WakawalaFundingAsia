package com.wakawala.ui.actvities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wakawala.ApplicationLoader;
import com.wakawala.R;
import com.wakawala.adapters.FilterAdapter;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivityFilterBinding;
import com.wakawala.model.FilterModel;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FilterActivity extends BaseActivity {

    private ActivityFilterBinding mBinding;
    private FilterAdapter mCategoryAdapter;
    private FilterAdapter mCountryAdapter;

    ArrayList<FilterModel> categoryModels;
    ArrayList<FilterModel> countryModels;

    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, FilterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_filter);
        ApplicationLoader.getInstance().setCategory("");
        ApplicationLoader.getInstance().setCountry("");
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();

        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.filter), true, R.drawable.ic_cross);

        mBinding.rvCategories.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        mBinding.rvCountry.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));

        categoryModels = new ArrayList<>();
        countryModels = new ArrayList<>();

        categoryModels.add(new FilterModel("Health & Medical"));
        categoryModels.add(new FilterModel("Accidents & Emergencies"));
        categoryModels.add(new FilterModel("Disaster Relief"));
        categoryModels.add(new FilterModel("Food"));
        categoryModels.add(new FilterModel("Elderly"));
        categoryModels.add(new FilterModel("Veterans"));
        categoryModels.add(new FilterModel("Kids & Family"));
        categoryModels.add(new FilterModel("Schools & Education"));
        categoryModels.add(new FilterModel("Non-Profit & Charity"));
        categoryModels.add(new FilterModel("Religious Organizations"));
        categoryModels.add(new FilterModel("Memorials & Funerals"));
        categoryModels.add(new FilterModel("Politics & Public Office"));
        categoryModels.add(new FilterModel("Pets & Animals"));
        categoryModels.add(new FilterModel("Sports & Teams"));
        categoryModels.add(new FilterModel("Trips & Adventures"));
        categoryModels.add(new FilterModel("Clubs & Community"));
        categoryModels.add(new FilterModel("Creative art, Music & Film"));
        categoryModels.add(new FilterModel("Others"));

        countryModels.add(new FilterModel("US"));
        countryModels.add(new FilterModel("UK"));
        countryModels.add(new FilterModel("Russia"));
        countryModels.add(new FilterModel("China"));
        countryModels.add(new FilterModel("India"));
        countryModels.add(new FilterModel("Sri Lanka"));
        countryModels.add(new FilterModel("New Zealand"));
        countryModels.add(new FilterModel("Australia"));
        countryModels.add(new FilterModel("Tazakhstan"));
        countryModels.add(new FilterModel("Pakistan"));
        countryModels.add(new FilterModel("Spain"));
        countryModels.add(new FilterModel("Japan"));
        countryModels.add(new FilterModel("South Africa"));
        countryModels.add(new FilterModel("Ethiopia"));
        countryModels.add(new FilterModel("Iceland"));
        countryModels.add(new FilterModel("Greenland"));

        mCategoryAdapter = new FilterAdapter(this, categoryModels);
        mCountryAdapter = new FilterAdapter(this, countryModels);

        mBinding.rvCategories.setAdapter(mCategoryAdapter);
        mBinding.rvCountry.setAdapter(mCountryAdapter);

        mBinding.tvApplyFilters.setOnClickListener(view -> {
            Log.e("cate " + mCategoryAdapter.getSelectedSize(), "country " + mCountryAdapter.getSelectedSize());

            ApplicationLoader.getInstance().setCountry(mCountryAdapter.getSelectedArray());
            ApplicationLoader.getInstance().setCategory(mCategoryAdapter.getSelectedArray());
            onBackPressed();
        });
    }

    @Override
    protected void loadData() {

    }

    public static String getShortFormCategory(String fullForm) {
        Log.e("category", fullForm);
        switch (fullForm) {
            case "Health & Medical":
                return "HM";
            case "Accidents & Emergencies":
                return "AE";
            case "Disaster Relief":
                return "DR";
            case "Food":
                return "FD";
            case "Elderly":
                return "ED";
            case "Veterans":
                return "VT";
            case "Kids & Family":
                return "KF";
            case "Schools & Education":
                return "SE";
            case "Non-Profit & Charity":
                return "NP";
            case "Religious Organizations":
                return "RO";
            case "Memorials & Funerals":
                return "MF";
            case "Politics & Public Office":
                return "PO";
            case "Pets & Animals":
                return "PA";
            case "Sports & Teams":
                return "ST";
            case "Trips & Adventures":
                return "TA";
            case "Clubs & Community":
                return "CC";
            case "Creative art, Music & Film":
                return "CA";
            case "Others":
                return "OT";
            default:
                return "";
        }
    }
}
