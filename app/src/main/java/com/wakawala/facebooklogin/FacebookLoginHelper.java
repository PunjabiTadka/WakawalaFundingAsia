package com.wakawala.facebooklogin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FacebookLoginHelper {

    private static FacebookLoginHelper facebookLoginHelper = null;
    private AppCompatActivity activity;
    private CallbackManager callbackManager;

    public static FacebookLoginHelper getInstance(AppCompatActivity activity) {
        if (facebookLoginHelper == null) {
            facebookLoginHelper = new FacebookLoginHelper(activity);
        }
        return facebookLoginHelper;
    }

    public FacebookLoginHelper(AppCompatActivity activity) {
        this.activity = activity;
        this.callbackManager = CallbackManager.Factory.create();
//        this.loginButton = (LoginButton) ((AppCompatActivity) context).findViewById(R.id.facebookLoginButton);
//        this.loginButton.setReadPermissions(Collections.singletonList(EMAIL));
    }

    public void initialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(activity);
    }

    public String getHashkey() {
        try {
            PackageInfo info = activity.getPackageManager()
                    .getPackageInfo(activity.getApplicationContext().getPackageName(),
                            PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
        return "";
    }

    public void setLoginButton(final FacebookLoginInterface facebookLoginInterface) {
        if (isLoggedInOrNot()) {
            Toast.makeText(activity, "Already Logged In", Toast.LENGTH_SHORT).show();
        } else {
            LoginManager.getInstance().logInWithReadPermissions(((AppCompatActivity) activity), Arrays.asList(
                    "public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.e("id", Profile.getCurrentProfile().getId());
                            Log.e("name", Profile.getCurrentProfile().getName());
                            Log.e("linkuri", Profile.getCurrentProfile().getLinkUri().toString());
                            Log.e("first", Profile.getCurrentProfile().getFirstName());
                            Log.e("middle", Profile.getCurrentProfile().getMiddleName());
                            Log.e("last", Profile.getCurrentProfile().getLastName());

                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.v("LoginActivity", response.toString());

                                            try {
                                                String email = object.getString("email");
                                                Log.e("email",email);
                                            } catch (Exception e) {
                                                Log.e("error", e.getMessage());
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();
                            facebookLoginInterface.onSuccess();
                        }

                        @Override
                        public void onCancel() {
                            facebookLoginInterface.onCancel();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            facebookLoginInterface.onError();
                        }
                    });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedInOrNot() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    public void logoutFacebook() {
        LoginManager.getInstance().logOut();
        facebookLoginHelper = null;
    }
}
