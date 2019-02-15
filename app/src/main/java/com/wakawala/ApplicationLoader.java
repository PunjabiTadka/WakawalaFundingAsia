package com.wakawala;

import android.app.Application;
import android.os.StrictMode;

public class ApplicationLoader extends Application {

    private static ApplicationLoader applicationLoader = null;

    public static ApplicationLoader getInstance() {
        return applicationLoader;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationLoader = this;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private String country = "", category = "";

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String categoryFullForm = "", categoryShortForm = "";

    public String getCategoryFullForm() {
        return categoryFullForm;
    }

    public String getCategoryShortForm() {
        return categoryShortForm;
    }

    public void setCategoryFullForm(String categoryFullForm) {
        this.categoryFullForm = categoryFullForm;
    }

    public void setCategoryShortForm(String categoryShortForm) {
        this.categoryShortForm = categoryShortForm;
    }

    /* city and country in add campaign */
    private String campaignCountry = "", campaignCity = "";
    private int campaignCountryId = 0, campaignCityId = 0;

    public String getCampaignCountry() {
        return campaignCountry;
    }

    public void setCampaignCountry(String campaignCountry) {
        this.campaignCountry = campaignCountry;
    }

    public String getCampaignCity() {
        return campaignCity;
    }

    public void setCampaignCity(String campaignCity) {
        this.campaignCity = campaignCity;
    }

    public int getCampaignCountryId() {
        return campaignCountryId;
    }

    public void setCampaignCountryId(int campaignCountryId) {
        this.campaignCountryId = campaignCountryId;
    }

    public int getCampaignCityId() {
        return campaignCityId;
    }

    public void setCampaignCityId(int campaignCityId) {
        this.campaignCityId = campaignCityId;
    }
}
