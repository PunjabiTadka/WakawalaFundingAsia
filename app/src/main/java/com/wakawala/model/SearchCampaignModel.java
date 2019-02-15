package com.wakawala.model;

public class SearchCampaignModel {
    private String username, profilePic, email, campaignCode;

    public SearchCampaignModel() {
    }

    public SearchCampaignModel(String username, String profilePic, String email, String campaignCode) {
        this.username = username;
        this.profilePic = profilePic;
        this.email = email;
        this.campaignCode = campaignCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }
}
