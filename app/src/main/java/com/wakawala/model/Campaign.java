package com.wakawala.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Observable;

public class Campaign {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("goal")
    @Expose
    private Integer goal;
    @SerializedName("goalInUSD")
    @Expose
    private Integer goalInUSD;
    @SerializedName("imageSize")
    @Expose
    private String imageSize;
    @SerializedName("localGoalCurrencySymbol")
    @Expose
    private String localGoalCurrencySymbol;
    @SerializedName("moneyCollected")
    @Expose
    private Integer moneyCollected;
    @SerializedName("numberOfLikes")
    @Expose
    private Integer numberOfLikes;
    @SerializedName("numberOfShares")
    @Expose
    private Integer numberOfShares;
    @SerializedName("numberOfVisitors")
    @Expose
    private Integer numberOfVisitors;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("timeStamp")
    @Expose
    private long timeStamp;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("titleLowercased")
    @Expose
    private String titleLowercased;
    @SerializedName("uploadedImages")
    @Expose
    private List<String> uploadedImages = null;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("youtubeVideoId")
    @Expose
    private String youtubeVideoId;

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Integer getGoalInUSD() {
        return goalInUSD;
    }

    public void setGoalInUSD(Integer goalInUSD) {
        this.goalInUSD = goalInUSD;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getLocalGoalCurrencySymbol() {
        return localGoalCurrencySymbol;
    }

    public void setLocalGoalCurrencySymbol(String localGoalCurrencySymbol) {
        this.localGoalCurrencySymbol = localGoalCurrencySymbol;
    }

    public Integer getMoneyCollected() {
        return moneyCollected;
    }

    public void setMoneyCollected(Integer moneyCollected) {
        this.moneyCollected = moneyCollected;
    }

    public Integer getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Integer numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Integer getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(Integer numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public Integer getNumberOfVisitors() {
        return numberOfVisitors;
    }

    public void setNumberOfVisitors(Integer numberOfVisitors) {
        this.numberOfVisitors = numberOfVisitors;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleLowercased() {
        return titleLowercased;
    }

    public void setTitleLowercased(String titleLowercased) {
        this.titleLowercased = titleLowercased;
    }

    public List<String> getUploadedImages() {
        return uploadedImages;
    }

    public void setUploadedImages(List<String> uploadedImages) {
        this.uploadedImages = uploadedImages;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }

    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }
}
