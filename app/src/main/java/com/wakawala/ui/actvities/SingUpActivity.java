package com.wakawala.ui.actvities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wakawala.BuildConfig;
import com.wakawala.R;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivitySingUpBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.helpers.ValidationHelper;
import com.wakawala.util.Util;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingUpActivity extends BaseActivity {

    private ActivitySingUpBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorDarkBlue));
        }
        decorView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_login_option));
        setView(R.layout.activity_sing_up);
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, "", true);
        mBinding.includedToolbar.toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private boolean checkValidation() {
        if (mBinding.edUsername.getText().toString().isEmpty()) {
            Toast.makeText(mContext, R.string.str_please_enter_username, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mBinding.edEmail.getText().toString().isEmpty()) {
            Toast.makeText(mContext, R.string.str_please_enter_email_address, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ValidationHelper.isValidEmail(mBinding.edEmail.getText().toString())) {
            Toast.makeText(mContext, R.string.str_please_enter_valid_email_address, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mBinding.edPwd.getText().toString().isEmpty()) {
            Toast.makeText(mContext, R.string.str_please_enter_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!mBinding.radioIndividual.isChecked() && !mBinding.radioOrganization.isChecked()) {
            Toast.makeText(mContext, R.string.str_please_select_account_type, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void loadData() {
        setAgreeText();
        mBinding.btnSignup.setOnClickListener(view -> {

            if (checkValidation()) {
                registerUser("");
            }
            //MainActivity.newInstance(SingUpActivity.this);
        });
    }

    private void registerUser(String pictureUrl) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(WebFields.USER_NAME, mBinding.edUsername.getText().toString());
        jsonObject.addProperty(WebFields.EMAIL, mBinding.edEmail.getText().toString());
        jsonObject.addProperty(WebFields.PASSWORD, mBinding.edPwd.getText().toString());
        if (mBinding.radioIndividual.isChecked()) {
            jsonObject.addProperty(WebFields.USER_TYPE, "1");
        }
        if (mBinding.radioOrganization.isChecked()) {
            jsonObject.addProperty(WebFields.USER_TYPE, "2");
        }
        jsonObject.addProperty(WebFields.PROFILE_PIC, pictureUrl);

        LoadingHelper.getInstance().show(this);
        ApiClient.getClient().register(jsonObject).enqueue(
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

                            if (jsonObject.has(WebFields.ERROR_CODE)) {
                                Toast.makeText(SingUpActivity.this,
                                        jsonObject.getString(WebFields.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("onloginex", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.e("onfailure", t.getMessage() + " " + t.getStackTrace());
                        LoadingHelper.getInstance().dismiss();
                        Toast.makeText(mContext, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        finish();
                        if (Util.isNetworkConnected(SingUpActivity.this)) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(WebFields.DEVICE_ID, Util.getDeviceToken(SingUpActivity.this));
                            jsonObject.addProperty(WebFields.DEVICE_TYPE, 1);
                            jsonObject.addProperty(WebFields.REGISTRATION_ID, FirebaseInstanceId.getInstance().getToken());
                            jsonObject.addProperty(WebFields.GUI_VERSION, BuildConfig.VERSION_NAME);
                            ApiClient.getClient().registerDevice("", jsonObject).enqueue(
                                    new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {
                                            LoadingHelper.getInstance().dismiss();
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            LoadingHelper.getInstance().dismiss();
                                        }
                                    }
                            );
                        }
                    }
                }
        );
    }

    private void uploadPhoto(String str) {
        if (str.isEmpty()) return;
        File file = new File(str);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

        LoadingHelper.getInstance().show(this);
        Call<Object> call = ApiClient.getClient()
                .uploadPicture(MultipartBody.Part.createFormData(WebFields.PICTURE, file.getName(), reqFile));

        call.enqueue(new Callback<Object>() {
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

                    jsonObject.getString(WebFields.URL);
                    registerUser(jsonObject.getString(WebFields.URL));
                } catch (Exception e) {
                    Log.e("uploadex", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LoadingHelper.getInstance().dismiss();
            }
        });
    }

    /**
     * This method will make some text area clickable.
     */
    private void setAgreeText() {
        final Intent intent = new Intent(this, LoginOptionActivity.class);
        String str = getString(R.string.agree_terms);
        SpannableString ss = new SpannableString(str);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
//                startActivity(intent);
                showToast("opening privacy policy screen!!");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 37, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.tvAgree.setText(ss);
        mBinding.tvAgree.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvAgree.setHighlightColor(Color.TRANSPARENT);
    }
}
