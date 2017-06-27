package club.lunchmates.lunchmates.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import club.lunchmates.lunchmates.controller.interfaces.PreferencesController;

public class PreferencesControllerImpl implements PreferencesController {
    private final String O_AUTH_TOKEN = "oauth";
    private final String NOTIFICATION_TOKEN = "notificationToken";
    private final String SESSION_TOKEN = "sessionToken";
    private final String USER_ID = "userId";
    private final String GPS_SHARING = "gps";

    private SharedPreferences preferences;

    public PreferencesControllerImpl(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public String getOAuthToken() {
        return preferences.getString(O_AUTH_TOKEN, null);
    }

    @Override
    public void setOAuthToken(String token) {
        if(token == null) {
            throw new IllegalArgumentException("OAuth token is null");
        }

        preferences.edit().putString(O_AUTH_TOKEN, token).apply();
    }

    @Override
    public String getNotificationToken() {
        return preferences.getString(NOTIFICATION_TOKEN, null);
    }

    @Override
    public void setNotificationToken(String token) {
        if(token == null) {
            throw new IllegalArgumentException("Notification token is null");
        }

        preferences.edit().putString(NOTIFICATION_TOKEN, token).apply();
    }

    @Override
    public String getSessionToken() {
        return preferences.getString(SESSION_TOKEN, null);
    }

    @Override
    public void setSessionToken(String token) {
        if(token == null) {
            throw new IllegalArgumentException("Session token is null");
        }

        preferences.edit().putString(SESSION_TOKEN, token).apply();
    }

    @Override
    public int getUserId() {
        return preferences.getInt(USER_ID, -1);
    }

    @Override
    public void setUserId(int id) {
        preferences.edit().putInt(USER_ID, id).commit();
    }

    @Override
    public void setGPSSharing(boolean share) {
        preferences.edit().putBoolean(GPS_SHARING, share).commit();
    }

    @Override
    public boolean isGPSSharing() {
        return preferences.getBoolean(GPS_SHARING, false);
    }
}
