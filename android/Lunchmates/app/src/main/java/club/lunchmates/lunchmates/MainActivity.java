package club.lunchmates.lunchmates;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

import club.lunchmates.lunchmates.controller.PreferencesControllerImpl;
import club.lunchmates.lunchmates.controller.interfaces.PreferencesController;
import club.lunchmates.lunchmates.data.Event;
import club.lunchmates.lunchmates.rest.RestHelperImpl;
import club.lunchmates.lunchmates.rest.interfaces.RestHelper;

import static android.R.attr.value;
import static android.R.attr.widgetCategory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private static final String BASE_URL = "http://10.0.2.2:9999/";
    private TextView nearbyNumber;
    private TextView startingNumber;
    private GoogleMap mMap;
    private LatLngBounds mapCameraBounds;
    private static final int DOUBLE_BACK_TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long msBackPressed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PreferencesController pref;
        pref = new PreferencesControllerImpl(this);
        TextView userName = (TextView) findViewById(R.id.userName);
//        userName.setText(pref.getUserName());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //new RetrieveEventsTask(t).execute(getAbsoluteUrl("events"));
        RestHelper helper = new RestHelperImpl();

        RestHelper.DataReceivedListener<List<Event>> listener = new RestHelper.DataReceivedListener<List<Event>>() {
            @Override
            public void onDataReceived(List<Event> data) {
                if (data != null) {
                    for (Event e : data) {
                        Log.i("MainActivity", "Event " + e.getName() + " " + e.getX() + " " + e.getY());
                    }
                }
            }
        };
        helper.eventGetAll(listener);

//        initEventsNumbers();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, CreateEventActivity.class));    //CreateEventActivity.class));
                MainActivity.this.finish();
            }
        });

        //for testing only
        FloatingActionButton fab_test = (FloatingActionButton) findViewById(R.id.fab_test);
        fab_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ShowEventActivity.class));
                MainActivity.this.finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //close the app if back is twice pressed in DOUBLE_BACK_TIME_INTERVAL seconds
            if (msBackPressed + DOUBLE_BACK_TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed(); //close the app
                return;
            } else {
                Toast.makeText(this, "Nochmaliges Tippen beendet die App.", Toast.LENGTH_SHORT).show();
            }

            msBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            item.setChecked(true);
            MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent i = new Intent(MainActivity.this, ShowEventActivity.class);
                i.putExtra("event_id", 123456);
                MainActivity.this.startActivity(i);
                MainActivity.this.finish();
//               Toast.makeText(MainActivity.this, "Info Window clicked!", Toast.LENGTH_SHORT).show();
            }
        });

//        mMap.getUiSettings().setCompassEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        LatLng mensaTU = new LatLng(51.4930692f, 7.4139248f);
        LatLng foodFak = new LatLng(51.4935117f, 7.4161913f);

        mMap.addMarker(new MarkerOptions()
                .position(mensaTU)
                .title("Hauptmensa TU Dortmund")
                .snippet("Powereater93"));
        mMap.addMarker(new MarkerOptions()
                .position(foodFak)
                .title("Food Fakultät")
                .snippet("HerrDöner"));

//        markEventsOnTheMap();
//        mMap.setLatLngBoundsForCameraTarget(mapCameraBounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mensaTU, 15.0f));


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    public void markEventsOnTheMap() {
        RestHelper helper = new RestHelperImpl();

        RestHelper.DataReceivedListener<List<Event>> listener = new RestHelper.DataReceivedListener<List<Event>>() {
            @Override
            public void onDataReceived(List<Event> data) {
                if (data != null) {
                    LatLng eLocation = null;
                    for (Event e : data) {
                        eLocation = new LatLng(Float.parseFloat(e.getX()), Float.parseFloat(e.getY()));
                        mMap.addMarker(new MarkerOptions().position(eLocation).title(e.getName())
                                .snippet("Powereater93"));
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eLocation, 15.0f));
                }
                findMapCameraBounds(data);
            }
        };
        helper.eventGetAll(listener);
    }

    public LatLngBounds findMapCameraBounds(List<Event> eventData) {
        LatLng eLocation = null;
        LatLng mostLeftDown, mostRightUp;//, mostUp, mostDown;
        float[] lat = new float[eventData.size()];
        float[] lng = new float[eventData.size()];

        for (int i = 0; i < eventData.size(); i++) {
            lat[i] = Float.parseFloat(eventData.get(i).getX());
            lng[i] = Float.parseFloat(eventData.get(i).getY());
        }

        Arrays.sort(lat);
        Arrays.sort(lng);
        mostLeftDown = new LatLng(lat[0], lng[0]);
        mostRightUp = new LatLng(lat[lat.length-1], lng[lng.length-1]);

        mapCameraBounds = new LatLngBounds(new LatLng(51.4930692f, 7.4139248f), new LatLng(51.4935117f, 7.4161913f));   //new LatLngBounds(mostLeftDown, mostRightUp);
        return mapCameraBounds;
    }

    public void initEventsNumbers() {
        RestHelper helper = new RestHelperImpl();
        RestHelper.DataReceivedListener<List<Event>> listener = new RestHelper.DataReceivedListener<List<Event>>() {
            @Override
            public void onDataReceived(List<Event> data) {
                if (data != null) {
                    int counter = 0;
                    for (Event e : data) {
                        counter++;
                        Log.i("MainActivity", "Event " + e.getName() + " " + e.getX() + " " + e.getY());
                    }
                    nearbyNumber = (TextView) findViewById(R.id.nearbyNumber);
                    nearbyNumber.setText(counter);
                    startingNumber = (TextView) findViewById(R.id.startingNumber);
                    startingNumber.setText(counter);
                }
            }
        };
//        helper.userGetEvents(preferences.getUserId(), listener);
//        helper.
    }
}
