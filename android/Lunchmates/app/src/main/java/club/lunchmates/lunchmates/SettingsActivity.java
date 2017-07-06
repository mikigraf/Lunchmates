package club.lunchmates.lunchmates;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import club.lunchmates.lunchmates.controller.PreferencesControllerImpl;
import club.lunchmates.lunchmates.controller.interfaces.PreferencesController;
import club.lunchmates.lunchmates.data.Event;
import club.lunchmates.lunchmates.data.UpdateResult;
import club.lunchmates.lunchmates.data.User;
import club.lunchmates.lunchmates.rest.RestHelperImpl;
import club.lunchmates.lunchmates.rest.interfaces.RestHelper;

public class SettingsActivity extends AppCompatActivity {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final PreferencesController preferences;
        preferences = new PreferencesControllerImpl(this);


        Switch switchLocation = (Switch) findViewById(R.id.switch_location);
        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int gpsStatus = 0;
                try {
                    gpsStatus = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }

                if (isChecked) {
                    //if gps is deactivated prompt user to activate it
                    if(gpsStatus==0){
                        Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(onGPS);
                    }
                    preferences.setGPSSharing(true);
                    Toast.makeText(SettingsActivity.this, "Location sharing is " + boolToString(preferences.isGPSSharing()), Toast.LENGTH_SHORT).show();
                } else {
                    //dont track the user any more
                    preferences.setGPSSharing(false);
                    Toast.makeText(SettingsActivity.this, "Location sharing is " + boolToString(preferences.isGPSSharing()), Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button buttonLogout = (Button) findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go to the login screen again?
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(SettingsActivity.this, "Du hast dich abgemeldet", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                i.putExtra("loginAgain", 1);
                SettingsActivity.this.startActivity(i);
                SettingsActivity.this.finish();
            }
        });


        Button buttonDeleteProfile = (Button) findViewById(R.id.button_delete_profile);
        buttonDeleteProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //remove user from the db
                RestHelper helper = new RestHelperImpl();
                RestHelper.DataReceivedListener<UpdateResult> listener = new RestHelper.DataReceivedListener<UpdateResult>() {
                    @Override
                    public void onDataReceived(UpdateResult data) {
                        if (data != null) {
                            data.setStatus(preferences.getUserId());
                        }
                    }
                };
                helper.userDelete(preferences.getUserId(), listener);
                FirebaseAuth.getInstance().getCurrentUser().delete();

                //go to the main screen again and show login window
                Toast.makeText(SettingsActivity.this, "Dein Profil wurde gelöscht", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                i.putExtra("loginAgain", 1);
                SettingsActivity.this.startActivity(i);
                SettingsActivity.this.finish();
            }
        });

    }


    public String boolToString(boolean value) {
        return value ? "true" : "false";
    }
}
