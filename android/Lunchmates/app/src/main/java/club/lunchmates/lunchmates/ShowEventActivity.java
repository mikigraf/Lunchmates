package club.lunchmates.lunchmates;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import club.lunchmates.lunchmates.data.Event;
import club.lunchmates.lunchmates.rest.RestHelperImpl;
import club.lunchmates.lunchmates.rest.interfaces.RestHelper;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ShowEventActivity extends AppCompatActivity
        implements OnMapReadyCallback, OnMyLocationButtonClickListener {

    private GoogleMap eventMap;
    private int currEventId;
    private String mate = "";
    private String location;
    private double eventLat;
    private double eventLng;
    private Date currEventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment se_mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.show_event_map);
        se_mapFragment.getMapAsync(ShowEventActivity.this);


        Button buttonArrived = (Button) findViewById(R.id.button_arrived);
        buttonArrived.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(ShowEventActivity.this, "not implemented yet!", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonNotArrived = (Button) findViewById(R.id.button_not_arrived);
        buttonNotArrived.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(ShowEventActivity.this, "not implemented yet!", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        currEventId = intent.getIntExtra("event_id", 0);
        mate = intent.getStringExtra("event_author");
        location = intent.getStringExtra("event_location");
        eventLat = intent.getDoubleExtra("event_Lat", 0.0);
        eventLng = intent.getDoubleExtra("event_Lng", 0.0);
        TextView mateNameView = (TextView) findViewById(R.id.text_SE_event_name);
        TextView locationView = (TextView) findViewById(R.id.text_SE_location);
        TextView dateView = (TextView) findViewById(R.id.text_SE_date);
        TextView timeView = (TextView) findViewById(R.id.text_SE_time);
        mateNameView.append(" " + mate);
        locationView.append(" " + location);

        ///////DEBUG
        Toast.makeText(ShowEventActivity.this, " Event ID: " + currEventId, Toast.LENGTH_SHORT).show();

//        dateView.append(" " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "." +
//                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "." +
//                Calendar.getInstance().get(Calendar.YEAR));
//        timeView.append(" " + Calendar.getInstance().get(Calendar.HOUR) + ":" +
//                Calendar.getInstance().get(Calendar.MINUTE));
        //////

        RestHelper helper = new RestHelperImpl();
        RestHelper.DataReceivedListener<List<Event>> listener = new RestHelper.DataReceivedListener<List<Event>>() {
            @Override
            public void onDataReceived(List<Event> data) {
                if (data != null) {
                    for (Event e : data) {
                        if (e.getId() == currEventId) {
                            currEventDate = e.getDate();

                            TextView dateView = (TextView) findViewById(R.id.text_SE_date);
                            TextView timeView = (TextView) findViewById(R.id.text_SE_time);

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                            String dateS = dateFormat.format(currEventDate);
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            String timeS = timeFormat.format(currEventDate);
                            dateView.append(dateS);
                            timeView.append(timeS);

                        }
                    }
                }
            }
        };
        helper.eventGetAll(listener);

        initLayout();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        MapsInitializer.initialize(ShowEventActivity.this);
        eventMap = googleMap;
        //TODO show my location on the map, the blue dot
//        eventMap.setOnMyLocationButtonClickListener(this);
//        eventMap.setMyLocationEnabled(true);
        markEventOnTheMap();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            if (!eventMap.isMyLocationEnabled()) ;
            eventMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void markEventOnTheMap() {

        ///////DEBUG
        LatLng eLL = new LatLng(eventLat, eventLng);
        Marker mateMarker = eventMap.addMarker(new MarkerOptions()
                .position(eLL)
                .title(location)
                .snippet(mate));
        eventMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eLL, 15.0f));
        ///////

        RestHelper helper = new RestHelperImpl();
        RestHelper.DataReceivedListener<List<Event>> listener = new RestHelper.DataReceivedListener<List<Event>>() {
            @Override
            public void onDataReceived(List<Event> data) {
                if (data != null) {
                    LatLng eLocation = null;
                    for (Event e : data) {
                        if (e.getId() == currEventId) {
                            eLocation = new LatLng(Float.parseFloat(e.getX()), Float.parseFloat(e.getY()));
                            eventMap.addMarker(new MarkerOptions().position(eLocation).title(location)
                                    .snippet(mate));
                        }
                    }
                    eventMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eLocation, 15.0f));
                }
            }
        };
        helper.eventGetAll(listener);
    }

    public void initLayout() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;

        //set height of the bottom frame to 60% of display height
        FrameLayout fl = (FrameLayout) findViewById(R.id.show_event_bottom);
        ViewGroup.LayoutParams params = fl.getLayoutParams();
        params.height = (int) (height * 0.6);
        fl.setLayoutParams(params);
    }
}
