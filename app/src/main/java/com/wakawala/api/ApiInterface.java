package com.wakawala.api;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiInterface {

    @Multipart
    @POST("media/uploadpicture")
    Call<Object> uploadPicture(@Part MultipartBody.Part multipart);

    @POST("account/login")
    Call<Object> login(@Body JsonObject jsonBody);

    @POST("account/register")
    Call<Object> register(@Body JsonObject jsonBody);

    @POST("device/register")
    Call<Object> registerDevice(@Header("Authorization") String token, @Body JsonObject jsonBody);

    @GET
    Call<Object> getPopularCampaign(@Header("Authorization") String token, @Url String url);

    @GET
    Call<Object> getRecentCampaign(@Header("Authorization") String token, @Url String url);

    @GET
    Call<Object> CampaignSearchId(@Header("Authorization") String token, @Url String url);

    @GET
    Call<Object> GetCreatedCampaignByUser(@Header("Authorization") String token, @Url String url);

    @GET("location/country")
    Call<Object> getCountry(@Header("Authorization") String token);

    @GET
    Call<Object> getCity(@Header("Authorization") String token, @Url String url);

    @GET("account")
    Call<Object> getProfile(@Header("Authorization") String token);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @PUT("account/logout")
    Call<Object> logout(@Header("Authorization") String token, @Body JsonObject jsonBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("account/changepassword")
    Call<Object> changePassword(@Header("Authorization") String token, @Body JsonObject jsonBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @PUT("account/editprofile")
    Call<Object> editProfile(@Header("Authorization") String token, @Body JsonObject jsonBody);

    @Multipart
    @PUT("account/edituserprofilepic")
    Call<Object> uploadProfilePicture(@Header("Authorization") String token, @Part MultipartBody.Part multipart);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("campaign/like")
    Call<Object> like(@Header("Authorization") String token, @Body JsonObject jsonBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("campaign/unlike")
    Call<Object> unlike(@Header("Authorization") String token, @Body JsonObject jsonBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("campaign/favourite")
    Call<Object> favourite(@Header("Authorization") String token, @Body JsonObject jsonBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("campaign/unfavourite")
    Call<Object> unfavourite(@Header("Authorization") String token, @Body JsonObject jsonBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("campaign/share")
    Call<Object> share(@Header("Authorization") String token, @Body JsonObject jsonBody);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("campaign/report")
    Call<Object> report(@Header("Authorization") String token, @Body JsonObject jsonBody);


}