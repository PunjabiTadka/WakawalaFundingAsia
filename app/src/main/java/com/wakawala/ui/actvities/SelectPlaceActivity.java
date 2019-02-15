package com.wakawala.ui.actvities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.wakawala.R;
import com.wakawala.adapters.SelectPlaceAdapter;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.model.PlaceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPlaceActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    boolean isCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Select");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_left_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        isCountry = getIntent().getExtras().getBoolean("isCountry");

        loadData();

    }

    private void loadData() {
        if (isCountry) {
            LoadingHelper.getInstance().show(this);
            ApiClient.getClient().getCountry("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken())
                    .enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            LoadingHelper.getInstance().dismiss();
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
                                JSONArray jsonArray = new JSONArray(strRespResult);

                                List<PlaceModel> placeModels = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("country " + i, jsonObject.toString());
                                    PlaceModel placeModel = new PlaceModel(
                                            jsonObject.getInt(WebFields.ID),
                                            jsonObject.getString(WebFields.NAME));
                                    placeModels.add(placeModel);
                                }
                                recyclerView.setAdapter(new SelectPlaceAdapter(placeModels, isCountry));
                            } catch (Exception e) {
                                Log.e("asdf", e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            LoadingHelper.getInstance().dismiss();
                        }
                    });
        } else {
            LoadingHelper.getInstance().show(this);
            ApiClient.getClient().getCity("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(),
                    "location/city?countryId=1")
                    .enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            LoadingHelper.getInstance().dismiss();
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
                                JSONArray jsonArray = new JSONArray(strRespResult);
                                List<PlaceModel> placeModels = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("city " + i, jsonObject.toString());
                                    PlaceModel placeModel = new PlaceModel(
                                            jsonObject.getInt(WebFields.ID),
                                            jsonObject.getString(WebFields.NAME));
                                    placeModels.add(placeModel);
                                }
                                recyclerView.setAdapter(new SelectPlaceAdapter(placeModels, isCountry));
                            } catch (Exception e) {
                                Log.e("asdf", e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            LoadingHelper.getInstance().dismiss();
                        }
                    });
        }

//        FirebaseDatabase.getInstance().getReference(isCountry ? "countries" : "cities")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        try {
//
//                            List<String> data = new ArrayList<>();
//                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                                data.add(dataSnapshot1.getKey());
//
//                            if (isCountry)
//                                recyclerView.setAdapter(new SelectPlaceAdapter(data, AddCampaignFragment.country));
//                            else
//                                recyclerView.setAdapter(new SelectPlaceAdapter(data, AddCampaignFragment.city));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
    }
}
