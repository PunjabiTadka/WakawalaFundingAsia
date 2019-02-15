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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wakawala.R;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivityLoginBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.helpers.User;
import com.wakawala.util.Util;
import com.wakawala.util.Utility;

import org.json.JSONObject;

import java.io.IOException;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorDarkBlue));
        }
        decorView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_login_option));
        setView(R.layout.activity_login);
    }

    private boolean checkValidation() {
        if (mBinding.edEmail.getText().toString().isEmpty()) {
            Toast.makeText(mContext, R.string.str_please_enter_username, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mBinding.edPwd.getText().toString().isEmpty()) {
            Toast.makeText(mContext, R.string.str_please_enter_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, "", true);
        mBinding.includedToolbar.toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        mBinding.btnLogin.setOnClickListener(view -> {
            if (checkValidation()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(WebFields.USER_NAME, mBinding.edEmail.getText().toString());
                jsonObject.addProperty(WebFields.PASSWORD, mBinding.edPwd.getText().toString());
                jsonObject.addProperty(WebFields.DEVICE_ID, Util.getDeviceToken(this));

                LoadingHelper.getInstance().show(this);
                ApiClient.getClient().login(jsonObject).enqueue(
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
                                        Toast.makeText(LoginActivity.this, jsonObject.getString(WebFields.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                                    } else {
                                        User user = new User(
                                                jsonObject.getString(WebFields.ID),
                                                jsonObject.getString(WebFields.AUTH_TOKEN),
                                                jsonObject.getString(WebFields.EXPIRES_IN)
                                        );
                                        LoginHelper.getInstance().setUserData(user);
                                        Utility.callNewActivity(LoginActivity.this, MainActivity.class);
                                    }
                                } catch (Exception e) {
                                    Log.e("onloginex", e.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                LoadingHelper.getInstance().dismiss();
                            }
                        }
                );
            }
        });
    }

    @Override
    protected void loadData() {
        setSignUpText();
    }

    private void setSignUpText() {
        final Intent intent = new Intent(this, LoginOptionActivity.class);
        String str = getString(R.string.dont_have_an_account_signup);
        SpannableString ss = new SpannableString(str);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
//                startActivity(intent);
                //               showToast("opening Sign up screen!!");
                startActivity(new Intent(LoginActivity.this, SingUpActivity.class));

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 27, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.tvSignup.setText(ss);
        mBinding.tvSignup.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvSignup.setHighlightColor(Color.TRANSPARENT);
    }

}
