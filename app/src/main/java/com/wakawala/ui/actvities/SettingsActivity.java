package com.wakawala.ui.actvities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wakawala.R;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivitySettingsBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.helpers.User;
import com.wakawala.util.Util;
import com.wakawala.util.Utility;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding mBinding;

    public static void newInstance(Context context) {
        context.startActivity(new Intent(context, SettingsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_settings);
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.settings), true);

        mBinding.tvLogout.setOnClickListener(view -> logout());

        mBinding.tvChangePassword.setOnClickListener(view -> Utility.callActivity(SettingsActivity.this, ChangePasswordActivity.class));

        mBinding.tvHelpCentre.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hello@wakawala.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Wakawala Help");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(SettingsActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.tvReport.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hello@wakawala.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Wakawala Report");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(SettingsActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.tvTerms.setOnClickListener(view -> {
            String url = "http://www.wakawala.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        mBinding.tvPolicy.setOnClickListener(view -> {
            String url = "http://www.wakawala.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }

    private void logout() {
        LoadingHelper.getInstance().show(this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(WebFields.DEVICE_ID, Util.getDeviceToken(this));

        ApiClient.getClient().logout("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject)
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
                            LoginHelper.getInstance().clearUserData();
                            Utility.callNewActivity(SettingsActivity.this, LoginOptionActivity.class);
                        } catch (Exception e) {
                            Log.e("editprofileex", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LoadingHelper.getInstance().dismiss();
                    }
                });
    }

    private void sendEmail() {
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setDataAndType(Uri.parse("mailto:"), "text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SettingsActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void loadData() {

    }
}