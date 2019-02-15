package com.wakawala.ui.actvities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wakawala.BuildConfig;
import com.wakawala.R;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.util.Util;

import org.json.JSONObject;

import java.io.IOException;

import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    private final int SPLASH_TIMEOUT = 2000;
    private Runnable myRunnable;
    private Handler myHandler;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
        decorView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        //decorView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_login_option));
        setView(R.layout.activity_splash);

//        if (Util.isNetworkConnected(this)) {
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty(WebFields.DEVICE_ID, Util.getDeviceToken(this));
//            jsonObject.addProperty(WebFields.DEVICE_TYPE, 1);
//            jsonObject.addProperty(WebFields.REGISTRATION_ID, FirebaseInstanceId.getInstance().getToken());
//            jsonObject.addProperty(WebFields.GUI_VERSION, BuildConfig.VERSION_NAME);
//            ApiClient.getClient().registerDevice("", jsonObject).enqueue(
//                    new Callback<Object>() {
//                        @Override
//                        public void onResponse(Call<Object> call, Response<Object> response) {
//                            LoadingHelper.getInstance().dismiss();
//                        }
//
//                        @Override
//                        public void onFailure(Call<Object> call, Throwable t) {
//                            LoadingHelper.getInstance().dismiss();
//                        }
//                    }
//            );
//        }
    }

    @Override
    protected void initUI() {
        //mBinding = getBinding();
        ivProfileImage = findViewById(R.id.ivProfileImage);
        Glide.with(this).load(R.drawable.background_splash_screen)
                .apply(new RequestOptions().error(R.drawable.background_splash_screen)
                        .placeholder(R.drawable.background_splash_screen))
                .into(ivProfileImage);
        myHandler = new Handler();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        myHandler.postDelayed(myRunnable = () -> {

            //startActivity(new Intent(SplashActivity.this, MainActivity.class));
            if (!isFinishing()) {
                //PreferenceUtility preferenceUtility = PreferenceUtility.getInstance(SplashActivity.this);
//                if (!preferenceUtility.getBooleanData(PreferenceUtility.HAS_LOGGED_IN)) {
//                    startActivity(new Intent(SplashActivity.this, LoginOptionActivity.class));
//                } else {
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                }

                if (LoginHelper.getInstance().isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginOptionActivity.class));
                }
            }
            finish();
        }, SPLASH_TIMEOUT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopHandler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopHandler();
    }

    private void stopHandler() {
        if (myHandler != null) {
            myHandler.removeCallbacks(myRunnable);
        }
    }

    private void temp(){
        ApiClient.getClient().getPopularCampaign("","").enqueue(
                new Callback<Object>() {
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
                            JSONObject jsonObject = new JSONObject(strRespResult);
                        } catch (Exception e) {
                            Log.e("popularex", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LoadingHelper.getInstance().dismiss();
                    }
                });
    }
}
