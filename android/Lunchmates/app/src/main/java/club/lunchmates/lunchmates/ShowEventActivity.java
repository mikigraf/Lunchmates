package club.lunchmates.lunchmates;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.OrientationEventListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


public class ShowEventActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap eventMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int event_id = intent.getIntExtra("event_id", 0);

        initLayout();

//        SupportMapFragment se_mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.show_event_map);
//        se_mapFragment.getMapAsync(ShowEventActivity.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(ShowEventActivity.this);
        eventMap = googleMap;
        eventMap.setMyLocationEnabled(true);
//        Add a marker for me and my lunch mate.
//        LatLng me = new LatLng(51.493674f, 7.420105f);
//        LatLng mate = new LatLng();
//        eventMap.addMarker(new MarkerOptions().position().title("Ich"));
//        eventMap.addMarker(new MarkerOptions().position().title("Mate"));
//        eventMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fhDO, 15.0f));
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
            if (!eventMap.isMyLocationEnabled())
                eventMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
            }

            if (myLocation != null) {
                LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                eventMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14), 1500, null);
            }
        }
    }

//    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
//        @Override
//        public void onMyLocationChange(Location location) {
//            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//            mMarker = mMap.addMarker(new MarkerOptions().position(loc));
//            if(mMap != null){
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//            }
//        }
//    };

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int height = metrics.heightPixels;
//        int width = metrics.widthPixels;
//
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//
//            Toast.makeText(ShowEventActivity.this, "Landscape", Toast.LENGTH_SHORT).show();
//
//            FrameLayout fl_top = (FrameLayout) findViewById(R.id.show_event_top);
//            int h_top = fl_top.getHeight();
//            int w_top = fl_top.getWidth();
//            fl_top.setRotation(90.0f);
//            ViewGroup.LayoutParams params_top = fl_top.getLayoutParams();
//            params_top.height = height;
//            params_top.width = width / 2;
//
//            fl_top.setLayoutParams(params_top);
////            fl_top.setTranslationX((w_top - h_top) / 2);
////            fl_top.setTranslationY((h_top - w_top) / 2);
//
//            FrameLayout fl_bottom = (FrameLayout) findViewById(R.id.show_event_bottom);
//            int h_bottom = fl_bottom.getHeight();
//            int w_bottom = fl_bottom.getWidth();
//            //fl_top.setRotation(180.0f); not necessary!
//            ViewGroup.LayoutParams params_bottom = fl_top.getLayoutParams();
//            params_bottom.height = height;
//            params_bottom.width = width / 2;
//            fl_bottom.setLayoutParams(params_bottom);
////            fl_bottom.setTranslationX((w_bottom - h_bottom) / 2);
////            fl_bottom.setTranslationY((h_bottom - w_bottom) / 2);
//
//        }
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(ShowEventActivity.this, "Portrait", Toast.LENGTH_SHORT).show();
//
//        }
//    }

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
