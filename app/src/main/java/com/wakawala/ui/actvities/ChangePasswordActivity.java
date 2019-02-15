package com.wakawala.ui.actvities;

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
import com.wakawala.databinding.ActivityChangePasswordBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.helpers.User;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {

    private ActivityChangePasswordBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_change_password);
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.change_password), true);

        mBinding.tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation())
                    callChangePassword();
            }
        });
    }

    private boolean checkValidation() {
        if (mBinding.etOldPassword.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Enter Old Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mBinding.etNewPassword.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Enter New Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!mBinding.etNewPassword.getText().toString().equals(mBinding.etConfirmPassword.getText().toString())) {
            Toast.makeText(mContext, "Both Password fields must be same.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void loadData() {
    }

    private void callChangePassword() {
        LoadingHelper.getInstance().show(this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(WebFields.OLD_PASSWORD, mBinding.etOldPassword.getText().toString());
        jsonObject.addProperty(WebFields.NEW_PASSWORD, mBinding.etNewPassword.getText().toString());

        ApiClient.getClient().changePassword("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject)
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
                            JSONObject jsonObject = new JSONObject(strRespResult);

                            if (jsonObject.has(WebFields.ERROR_CODE)) {
                                Toast.makeText(ChangePasswordActivity.this, jsonObject.getString(WebFields.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                            } else {
                                User user = LoginHelper.getInstance().getUserData();
                                user.setAuthToken(jsonObject.getString(WebFields.AUTH_TOKEN));
                                LoginHelper.getInstance().setUserData(user);
                            }
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
}
