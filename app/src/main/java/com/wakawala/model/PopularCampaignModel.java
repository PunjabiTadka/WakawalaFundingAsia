package com.wakawala.model;

public class PopularCampaignModel {
    private int campaignId;
    private String userName, description, userProfilePic, city, country, likeCount, shareCount, commentCount;
    private String category, title, photoLink;
    private long startDate, endDate;
    private Integer currentAmount, targetAmount;
    private int userType;
    private boolean isLiked = false, isFavourite = false;

    public PopularCampaignModel() {
    }

    public PopularCampaignModel(int campaignId, String userName, String userProfilePic, int userType, String likeCount,
                                String shareCount, String commentCount, String city, String country, boolean isLiked,
                                boolean isFavourite, long startDate, long endDate, String category, String title,
                                int currentAmount, int targetAmount, String photoLink, String description) {
        this.campaignId = campaignId;
        this.userName = userName;
        this.userProfilePic = userProfilePic;
        this.userType = userType;
        this.likeCount = likeCount;
        this.shareCount = shareCount;
        this.commentCount = commentCount;
        this.city = city;
        this.country = country;
        this.isFavourite = isFavourite;
        this.isLiked = isLiked;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.title = title;
        this.currentAmount = currentAmount;
        this.targetAmount = targetAmount;
        this.photoLink = photoLink;
        this.description=description;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserType() {
        return userType;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getShareCount() {
        return shareCount;
    }

    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(int targetAmount) {
        this.targetAmount = targetAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
