package club.lunchmates.lunchmates.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class LunchmatesInstanceIDService extends FirebaseInstanceIdService {
    private final String TAG = "LMInstanceIDService";
    public LunchmatesInstanceIDService() {
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Received new token " + refreshedToken);

        // TODO: Send token to server
    }
}
