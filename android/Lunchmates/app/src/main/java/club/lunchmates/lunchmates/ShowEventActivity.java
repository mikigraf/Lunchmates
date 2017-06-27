package club.lunchmates.lunchmates;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ShowEventActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap eventMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLayout();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.show_event_map);
//        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        eventMap = googleMap;

// Add a marker for FH Dortmund and move the camera.
//        LatLng fhDO = new LatLng(51.493674f, 7.420105f);
//        eventMap.addMarker(new MarkerOptions().position(fhDO).title("FH DO"));
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
        }
        eventMap.setMyLocationEnabled(true);
        //eventMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fhDO, 15.0f));
    }

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
