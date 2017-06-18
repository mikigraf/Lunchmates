package club.lunchmates.lunchmates.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import club.lunchmates.lunchmates.rest.RestHelperImpl;
import club.lunchmates.lunchmates.rest.interfaces.RestHelper;

public class LunchmatesInstanceIDService extends FirebaseInstanceIdService {
    private final String TAG = "LMInstanceIDService";
    public LunchmatesInstanceIDService() {
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Received new token " + refreshedToken);

        RestHelper restHelper = new RestHelperImpl();
        // TODO: Pass the correct user id
        //restHelper.userUpdateToken(1, refreshedToken, null);
    }
}
