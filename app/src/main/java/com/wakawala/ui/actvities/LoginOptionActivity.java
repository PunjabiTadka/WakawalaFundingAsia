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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.wakawala.R;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.databinding.ActivityLoginOptionBinding;
import com.wakawala.facebooklogin.FacebookLoginHelper;
import com.wakawala.facebooklogin.FacebookLoginInterface;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.helpers.User;
import com.wakawala.util.Utility;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class LoginOptionActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private ActivityLoginOptionBinding mBinding;
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorDarkBlue));
        }
        decorView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_login_option));
        setView(R.layout.activity_login_option);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            MainActivity.newInstance(LoginOptionActivity.this);
            finish();
        }
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();

        Glide.with(this).load(R.drawable.background_splash_screen)
                .apply(new RequestOptions().error(R.drawable.background_splash_screen)
                        .placeholder(R.drawable.background_splash_screen))
                .into(mBinding.ivProfileImage);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void loadData() {
        setAgreeText();
        setSignUpText();
        mBinding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(
                        "000",
                        "",
                        ""
                );
                LoginHelper.getInstance().setUserData(user);
                Utility.callNewActivity(LoginOptionActivity.this, MainActivity.class);
            }
        });

        mBinding.btnLogin.setOnClickListener(view ->
                startActivity(new Intent(LoginOptionActivity.this, LoginActivity.class)));
        mBinding.btnFbLogin.setOnClickListener(view -> callFacebook());
        mBinding.btnGoogleLogin.setOnClickListener(view -> callGoogleLogin());
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: ");
        } else {
            Log.e("asdf", "a");
        }
    }

    private void callFacebook() {
        if (FacebookLoginHelper.getInstance(this).isLoggedInOrNot())
            FacebookLoginHelper.getInstance(this).logoutFacebook();
        FacebookLoginHelper.getInstance(this).setLoginButton(
                new FacebookLoginInterface() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(LoginOptionActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginOptionActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(LoginOptionActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookLoginHelper.getInstance(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void callGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    private void setSignUpText() {
        final Intent intent = new Intent(this, LoginOptionActivity.class);
        String str = getString(R.string.dont_have_an_account_signup);
        SpannableString ss = new SpannableString(str);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
//                startActivity(intent);
                //               showToast("opening Sign up screen!!");
                startActivity(new Intent(LoginOptionActivity.this, SingUpActivity.class));
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


//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            // Successfully signed in
//            if (resultCode == RESULT_OK) {
//
//
//                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                if (currentUser != null) {
//
//                    final HashMap<String, String> map = new HashMap<>();
//                    final HashMap<String, String> profileMap = new HashMap<>();
//
//                    FirebaseDatabase.getInstance().getReference("users")
//                            .child(currentUser.getUid())
//                            .child("profileInfo")
//                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                    ProfileInfo profileInfo = dataSnapshot.getValue(ProfileInfo.class);
//                                    if (profileInfo != null) {
//
//                                        if (profileInfo.getPhoneNumber() != null && profileInfo.getPhoneNumber().trim().length() != 0)
//                                            profileMap.put("phoneNumber", profileInfo.getPhoneNumber().trim());
//
//                                        if (profileInfo.getEmail() != null && profileInfo.getEmail().trim().length() != 0)
//                                            profileMap.put("email", profileInfo.getEmail().trim());
//
//                                        if (profileInfo.getBio() != null && profileInfo.getBio().trim().length() != 0)
//                                            profileMap.put("bio", profileInfo.getBio().trim());
//
//
//                                    }
//
//                                    if (currentUser.getEmail() != null)
//                                        profileMap.put("email", currentUser.getEmail());
//                                    if (currentUser.getPhoneNumber() != null)
//                                        profileMap.put("phoneNumber", currentUser.getPhoneNumber());
//
//                                    FirebaseDatabase.getInstance()
//                                            .getReference()
//                                            .child("users")
//                                            .child(currentUser.getUid())
//                                            .child("profileInfo")
//                                            .setValue(profileMap)
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//
//
//                                                    map.put("userType", "individual");
//                                                    map.put("userName", currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "");
//                                                    map.put("profileImageURL", currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "");
//
//
//                                                    FirebaseDatabase.getInstance()
//                                                            .getReference()
//                                                            .child("users")
//                                                            .child(currentUser.getUid())
//                                                            .child("userInfo")
//                                                            .setValue(map)
//                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                @Override
//                                                                public void onComplete(@NonNull Task<Void> task) {
//
//                                                                    MainActivity.newInstance(LoginOptionActivity.this);
//                                                                    finish();
//
//                                                                }
//
//                                                            });
//                                                }
//                                            });
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//
//
//                }
//            } else {
//                // Sign in failed
//                if (response == null) {
//                    // User pressed back button
//                    Util.showSnackbar(mBinding.btnSkip, "SignIn cancelled");
//                    return;
//                }
//
//                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
//                    Util.showSnackbar(mBinding.btnSkip, "No Internet Connection");
//                    return;
//                }
//
//                Util.showSnackbar(mBinding.btnSkip, "Unknown error");
//
//            }
//        }
//    }

}
