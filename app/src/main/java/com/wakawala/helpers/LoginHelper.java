package com.wakawala.helpers;

import com.wakawala.api.ApiClient;
import com.wakawala.prefs.SessionManager;

public class LoginHelper {

    private static LoginHelper loginHelper = null;
    public static final String USER_DATA = "USERDATA";

    public static LoginHelper getInstance() {
        if (loginHelper == null) {
            loginHelper = new LoginHelper();
        }
        return loginHelper;
    }

    public void setUserData(User user) {
        SessionManager.setSessionString(USER_DATA, ApiClient.gson.toJson(user));
    }

    public User getUserData() {
        return ApiClient.gson.fromJson(SessionManager.getSessionString(USER_DATA, ""), User.class);
    }

    public boolean isLoggedIn() {
        return !SessionManager.getSessionString(USER_DATA, "").isEmpty();
    }

    public void clearUserData() {
        SessionManager.setSessionString(USER_DATA, "");
    }

    public void updateProfile() {
        User user = getUserData();

        setUserData(user);
    }
}
