package club.lunchmates.lunchmates.controller.interfaces;

import android.support.annotation.Nullable;

/**
 * Controller for managing the access of the default share preferences of the app
 */
public interface PreferencesController {
    /**
     * Returns the firebase OAuth token
     * @return The token or <code>null</code> if there is none
     */
    @Nullable
    String getOAuthToken();

    /**
     * Sets the fire base OAuth token
     * @param token The new token
     */
    void setOAuthToken(String token);

    /**
     * Returns the firebase push notification token
     * @return The token or <code>null</code> if there is none
     */
    @Nullable
    String getNotificationToken();

    /**
     * Sets the firebase push notification token
     * @param token The new token
     */
    void setNotificationToken(String token);

    /**
     * Returns the server session token
     * @return The token or <code>null</code> if there is none
     */
    @Nullable
    String getSessionToken();

    /**
     * Sets the server session token
     * @param token The new token
     */
    void setSessionToken(String token);

    /**
     * Returns the id of the current user
     * @return The id of the user or -1 if the user is not authenticated yet
     */
    int getUserId();

    /**
     * Sets the id of the current user
     * @param id The new id
     */
    void setUserId(int id);

    void setGPSSharing(boolean share);

    boolean isGPSSharing();
}
