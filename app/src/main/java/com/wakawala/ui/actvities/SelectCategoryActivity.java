package com.wakawala.ui.actvities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wakawala.ApplicationLoader;
import com.wakawala.R;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivitySelectCategoryBinding;
import com.wakawala.databinding.RowActivitySelectCategoryBinding;
import com.wakawala.view.ItemDecorationAlbumColumns;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectCategoryActivity extends BaseActivity {

    private ActivitySelectCategoryBinding mBinding;
    ArrayList<CategoryModel> models;
    SelectCategoryAdapter selectCategoryAdapter;

    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, SelectCategoryActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_select_category);
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.select_a_category), true);

        models = new ArrayList<>();

        models.add(new CategoryModel("Health & Medical", R.drawable.helathmedical));
        models.add(new CategoryModel("Accidents & Emergencies", R.drawable.accident));
        models.add(new CategoryModel("Disaster Relief", R.drawable.disaster_relief));
        models.add(new CategoryModel("Food", R.drawable.food));
        models.add(new CategoryModel("Elderly", R.drawable.elderly));
        models.add(new CategoryModel("Veterans", R.drawable.veteran));
        models.add(new CategoryModel("Kids & Family", R.drawable.kids_family));
        models.add(new CategoryModel("Schools & Education", R.drawable.schools_education));
        models.add(new CategoryModel("Non-Profit & Charity", R.drawable.nonprofit));
        models.add(new CategoryModel("Religious Organizations", R.drawable.religious));
        models.add(new CategoryModel("Memorials & Funerals", R.drawable.memorial_funeral));
        models.add(new CategoryModel("Politics & Public Office", R.drawable.politics));
        models.add(new CategoryModel("Pets & Animals", R.drawable.petsanimals));
        models.add(new CategoryModel("Sports & Teams", R.drawable.sports_teams));
        models.add(new CategoryModel("Trips & Adventures", R.drawable.trips_adventures));
        models.add(new CategoryModel("Clubs & Community", R.drawable.community));
        models.add(new CategoryModel("Creative art, Music & Film", R.drawable.creative));
        models.add(new CategoryModel("Others", R.drawable.others));

        selectCategoryAdapter = new SelectCategoryAdapter();
        mBinding.recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(10, 3));
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mBinding.recyclerView.setAdapter(selectCategoryAdapter);
    }

    @Override
    protected void loadData() {

    }

    class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.SelectCategoryViewHolder> {

        private LayoutInflater layoutInflater;

        SelectCategoryAdapter() {
            this.layoutInflater = LayoutInflater.from(SelectCategoryActivity.this);
        }

        @NonNull
        @Override
        public SelectCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RowActivitySelectCategoryBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.row_activity_select_category, parent, false);
            return new SelectCategoryViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectCategoryViewHolder holder, int position) {
            holder.mBinding.tvName.setText(models.get(position).name);
            Glide.with(SelectCategoryActivity.this).load(models.get(position).image)
                    .into(holder.mBinding.ivImage);
            holder.mBinding.llParent.setOnClickListener(view -> {
                Log.e("sdf", models.get(position).name);
                ApplicationLoader.getInstance().setCategoryFullForm(models.get(position).name);
                ApplicationLoader.getInstance().setCategoryShortForm(FilterActivity.getShortFormCategory(models.get(position).name));
                onBackPressed();
            });
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        class SelectCategoryViewHolder extends RecyclerView.ViewHolder {
            private RowActivitySelectCategoryBinding mBinding;

            SelectCategoryViewHolder(@NonNull RowActivitySelectCategoryBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }
        }
    }

    class CategoryModel {
        String name;
        int image;

        public CategoryModel(String name, int image) {
            this.name = name;
            this.image = image;
        }
    }
}