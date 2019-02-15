package com.wakawala.ui.actvities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wakawala.R;
import com.wakawala.api.ApiClient;
import com.wakawala.api.LoadingHelper;
import com.wakawala.api.WebFields;
import com.wakawala.baseclasses.BaseActivity;
import com.wakawala.cropimage.CropImage;
import com.wakawala.cropimage.CropImageView;
import com.wakawala.databinding.ActivityEditProfileBinding;
import com.wakawala.helpers.LoginHelper;
import com.wakawala.helpers.ValidationHelper;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity {

//    CircleImageView userPicture;
//    EditText userName;
//    EditText userEmail;
//    EditText userPhoneNumber, bio;
//    View update;
//
//    UserInfo currentUserInfo;
//    ProfileInfo currentProfileInfo;
//    String userId;

    private ActivityEditProfileBinding mBinding;

    CircleImageView ivProfileImage;
    TextView tvChangeProfile;
    EditText etName, etWebsite, etPhone, etEmail, etBio;

    TextView update;

    private String selectedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_edit_profile);
//        userName = findViewById(R.id.userName);
//
//        userPicture = findViewById(R.id.userPicture);
//
//        userEmail = findViewById(R.id.userEmail);
//
//        userPhoneNumber = findViewById(R.id.userPhoneNumber);
//
//        bio = findViewById(R.id.bio);
//
//        update = findViewById(R.id.update);
//
//
//        userId = FirebaseAuth.getInstance().getUid();
//
//        if (userId != null && userId.trim().length() != 0) {
//
//
//            FirebaseDatabase.getInstance()
//                    .getReference("users")
//                    .child(userId)
//                    .child("profileInfo")
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            try {
//                                if (dataSnapshot.getValue() != null) {
//
//                                    currentProfileInfo = dataSnapshot.getValue(ProfileInfo.class);
//                                    if (currentProfileInfo != null) {
//
//                                        if (currentProfileInfo.getPhoneNumber() != null && currentProfileInfo.getPhoneNumber().trim().length() != 0)
//                                            userPhoneNumber.setText(currentProfileInfo.getPhoneNumber());
//
//                                        if (currentProfileInfo.getEmail() != null && currentProfileInfo.getEmail().trim().length() != 0)
//                                            userEmail.setText(currentProfileInfo.getEmail());
//
//                                        if (currentProfileInfo.getBio() != null && currentProfileInfo.getBio().trim().length() != 0)
//                                            bio.setText(currentProfileInfo.getBio());
//
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//
//            FirebaseDatabase.getInstance()
//                    .getReference("users")
//                    .child(userId)
//                    .child("userInfo")
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            try {
//                                if (dataSnapshot.getValue() != null) {
//
//                                    currentUserInfo = dataSnapshot.getValue(UserInfo.class);
//                                    if (currentUserInfo != null) {
//
//
//                                        if (currentUserInfo.getProfileImageURL() != null && currentUserInfo.getProfileImageURL().trim().length() != 0)
//                                            Picasso.with(EditProfileActivity.this)
//                                                    .load(currentUserInfo.getProfileImageURL())
//                                                    .into(userPicture);
//
//                                        if (currentUserInfo.getUserName() != null && currentUserInfo.getUserName().trim().length() != 0)
//                                            userName.setText(currentUserInfo.getUserName());
//
//
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//        }
//
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                HashMap<String, Object> userInfoMap = new HashMap<>();
//                HashMap<String, Object> profileInfoMap = new HashMap<>();
//
//                userInfoMap.put("userName", userName.getText().toString().trim());
//                userInfoMap.put("userType", "individual");
//
//                String profileImageUrl = "";
//                if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null && FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString().trim().length() != 0)
//                    profileImageUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
//
//                userInfoMap.put("profileImageURL", profileImageUrl);
//
//                profileInfoMap.put("bio", bio.getText().toString().trim());
//                profileInfoMap.put("email", userEmail.getText().toString().trim());
//                profileInfoMap.put("phoneNumber", userPhoneNumber.getText().toString().trim());
//
//                DatabaseReference ref = FirebaseDatabase.getInstance()
//                        .getReference("users")
//                        .child(userId);
//
//                ref.child("userInfo")
//                        .setValue(userInfoMap);
//
//                ref.child("profileInfo")
//                        .setValue(profileInfoMap);
//
//
//                finish();
//
//
//            }
//        });
    }

    @Override
    protected void initUI() {
        mBinding = getBinding();
        setToolbar(mBinding.includedToolbar.toolbar, getString(R.string.edit_profile), true);

        update = findViewById(R.id.update);
        update.setEnabled(false);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvChangeProfile = findViewById(R.id.tvChangeProfile);
        tvChangeProfile.setEnabled(false);
        etName = findViewById(R.id.etName);
        etName.setEnabled(false);
        etWebsite = findViewById(R.id.etWebsite);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etEmail.setEnabled(false);
        etBio = findViewById(R.id.etBio);

    }

    @Override
    protected void loadData() {
        LoadingHelper.getInstance().show(this);
        ApiClient.getClient().getProfile("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken()).enqueue(
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

                            Glide.with(EditProfileActivity.this).load(jsonObject.getString(WebFields.PROFILEPIC))
                                    .into(ivProfileImage);

                            if (jsonObject.has(WebFields.USER__NAME))
                                etName.setText(jsonObject.getString(WebFields.USER__NAME));
                            if (jsonObject.has(WebFields.WEBSITE.toLowerCase()))
                                etWebsite.setText(jsonObject.getString(WebFields.WEBSITE.toLowerCase()));
                            if (jsonObject.has(WebFields.PHONE_NUMBER))
                                etPhone.setText(jsonObject.getString(WebFields.PHONE_NUMBER));
                            if (jsonObject.has(WebFields.EMAIL.toLowerCase()))
                                etEmail.setText(jsonObject.getString(WebFields.EMAIL.toLowerCase()));
                            if (jsonObject.has(WebFields.BIO.toLowerCase()))
                                etBio.setText(jsonObject.getString(WebFields.BIO.toLowerCase()));

                            update.setEnabled(true);
                            tvChangeProfile.setEnabled(true);
                        } catch (Exception e) {
                            Log.e("asdf", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LoadingHelper.getInstance().dismiss();
                    }
                }
        );

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUpdate();
            }
        });

        tvChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStoragePermissions();
            }
        });
    }

    private boolean checkValidation() {
        if (etName.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Enter Name", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (etWebsite.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Enter Website", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (etPhone.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Enter Phone", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Enter Email", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (ValidationHelper.isValidEmail(etEmail.getText().toString())) {
            Toast.makeText(mContext, "Enter Valid Email", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (etBio.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Enter Bio", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void callUpdate() {
        if (checkValidation()) return;
        LoadingHelper.getInstance().show(this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(WebFields.WEBSITE, etWebsite.getText().toString());
        jsonObject.addProperty(WebFields.BIO, etBio.getText().toString());
        jsonObject.addProperty(WebFields.PHONENUMBER, etPhone.getText().toString());

        ApiClient.getClient().editProfile("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(), jsonObject)
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

    private void checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((AppCompatActivity) this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
            return;
        }
        callSelectedOption();
    }

    private void callSelectedOption() {
        CropImage.startPickImageActivity((AppCompatActivity) this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE:
                if (permissions.length == 1)
                    CropImage.activity(Uri.parse(selectedImagePath))
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);
                break;
            case 1000:
                callSelectedOption();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                    selectedImagePath = CropImage.getPickImageResultUri(this, data).toString();

                    // For API >= 23 we need to check specifically that we have permissions to read external storage.
                    if (CropImage.isReadExternalStoragePermissionsRequired(this, Uri.parse(selectedImagePath))) {
                        // request permissions and handle the result in onRequestPermissionsResult()
//                        cropImageUri = imageUri;
                        if (Build.VERSION.SDK_INT > 22) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                        }
                    } else {
                        // no permissions required or already grunted, can start crop image activity
                        CropImage.activity(Uri.parse(selectedImagePath))
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(this);
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();

                    try {
                        selectedImagePath = resultUri.getPath();

                        if (new File(selectedImagePath).length() > 1048576)
                            selectedImagePath = new Compressor(this)
                                    .compressToFile(new File(selectedImagePath))
                                    .getAbsolutePath();
                        Glide.with(this).asBitmap().load(selectedImagePath)
                                .apply(new RequestOptions()
                                        .transform(new CircleCrop())
                                        .placeholder(R.drawable.place_holder)
                                        .error(R.drawable.place_holder)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true))
                                .into(ivProfileImage);
                        callUpdateProfilePic();
                    } catch (Exception e) {
                        Log.e("getimageex", e.getMessage());
                    }
                    break;
            }
    }

    private void callUpdateProfilePic() {
        if (selectedImagePath.isEmpty()) return;
        File file = new File(selectedImagePath);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);


        LoadingHelper.getInstance().show(this);
        Call<Object> call = ApiClient.getClient()
                .uploadProfilePicture("Bearer " + LoginHelper.getInstance().getUserData().getAuthToken(),
                        MultipartBody.Part.createFormData(WebFields.PICTURE, file.getName(), reqFile));

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
}
