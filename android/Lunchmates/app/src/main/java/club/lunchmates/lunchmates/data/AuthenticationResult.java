package club.lunchmates.lunchmates.data;

/**
 * Created by jonas on 27.06.17.
 */

public class AuthenticationResult {
    private int userId;
    private boolean success;
    private String sessionToken;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
