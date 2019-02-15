package com.wakawala.model;

import java.util.Observable;

import androidx.databinding.Bindable;

public class CampaignModel{
    private Campaign campaign;
    private String userName;
    private String userType;
    private String userProfileURL;

    public CampaignModel(){}

    public CampaignModel(Campaign campaign, String userName, String userType, String userProfileURL) {
        this.campaign = campaign;
        this.userName = userName;
        this.userType = userType;
        this.userProfileURL = userProfileURL;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserProfileURL() {
        return userProfileURL;
    }

    public void setUserProfileURL(String userProfileURL) {
        this.userProfileURL = userProfileURL;
    }
}
