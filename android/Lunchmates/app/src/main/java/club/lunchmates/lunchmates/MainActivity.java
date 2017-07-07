package club.lunchmates.lunchmates;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import club.lunchmates.lunchmates.controller.PreferencesControllerImpl;
import club.lunchmates.lunchmates.controller.interfaces.PreferencesController;
import club.lunchmates.lunchmates.data.AuthenticationResult;
import club.lunchmates.lunchmates.data.Event;
import club.lunchmates.lunchmates.data.User;
import club.lunchmates.lunchmates.rest.RestHelperImpl;
import club.lunchmates.lunchmates.rest.interfaces.RestHelper;

import static android.R.attr.value;
import static android.R.attr.widgetCategory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    private static final String BASE_URL = "http://172.22.210.106:9999/";
    private static final int RESULT_CODE_LOGIN = 815;
    private TextView nearbyNumber;
    private TextView startingNumber;
    private GoogleMap mMap;
    private LatLngBounds mapCameraBounds;
    private static final int DOUBLE_BACK_TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long msBackPressed = 0;
    private static boolean isLogedIn = false;
    HashMap<Marker, Integer> markerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getIntExtra("loginAgain", 0) == 1) {
            isLogedIn = false;
        }
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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, CreateEventActivity.class));
            }
        });

        FloatingActionButton refresh = (FloatingActionButton) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MainActivity.this.startActivity(new Intent(MainActivity.this, CreateEventActivity.class));
                markEventsOnTheMap();
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
//        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(!isLogedIn) {
            signIn();
            isLogedIn = true;
        }

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RESULT_CODE_LOGIN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RESULT_CODE_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			///////DEBUG
            Toast.makeText(MainActivity.this, "RESULT_CODE_LOGIN: " + result.getStatus(), Toast.LENGTH_LONG).show();
//            initEventsNumbers();
            //update user name and email in the navbar
//            TextView userName = (TextView) findViewById(R.id.userName);
//            TextView userEmail = (TextView) findViewById(R.id.userEmail);
//            userName.setText("Test");
//            userEmail.setText("Test");
//            Toast.makeText(MainActivity.this, "User name> " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(MainActivity.this, "Email> " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
			/////////

            if (result.isSuccess()) {
                Toast.makeText(MainActivity.this, "RESULT_CODE_LOGIN_SUCCESS!", Toast.LENGTH_SHORT).show();
                sendGoogleLoginToken(result.getSignInAccount().getIdToken());
                isLogedIn = true;

                initEventsNumbers();

                //update user name and email in the navbar

                TextView userN = (TextView) findViewById(R.id.userName);
                TextView userE = (TextView) findViewById(R.id.userEmail);
                userN.setText(result.getSignInAccount().getDisplayName());
                userE.setText(result.getSignInAccount().getEmail());
            }
        }
    }

    private void sendGoogleLoginToken(String token) {
        RestHelper helper = new RestHelperImpl();
        RestHelper.DataReceivedListener<AuthenticationResult> listener = new RestHelper.DataReceivedListener<AuthenticationResult>() {
            @Override
            public void onDataReceived(AuthenticationResult result) {
                if(result == null) {
                    Toast.makeText(MainActivity.this, "Login connection error", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if(!result.isSuccess()) {
                        Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PreferencesController prefs = new PreferencesControllerImpl(MainActivity.this);
                    Log.d("bla", "token" + result.getSessionToken());
                    prefs.setSessionToken(result.getSessionToken());
                    prefs.setUserId(result.getUserId());

                }
            }
        };
       helper.login(token, listener);
    }

//    private void sendGoogleLoginToken(String token) {
//        RestHelper helper = new RestHelperImpl();
//        RestHelper.DataReceivedListener<AuthenticationResult> listener = new RestHelper.DataReceivedListener<AuthenticationResult>() {
//            @Override
//            public void onDataReceived(AuthenticationResult result) {
//                if(result == null) {
//                    Toast.makeText(MainActivity.this, "Login connection error", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    if(!result.isSuccess()) {
//                        Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    PreferencesController prefs = new PreferencesControllerImpl(MainActivity.this);
//                    Log.d("bla", "token" + result.getSessionToken());
//                    prefs.setSessionToken(result.getSessionToken());
//                    prefs.setUserId(result.getUserId());
//
//                }
//            }
//        };
//        helper.login(token, listener);
//    }

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
            MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        markEventsOnTheMap();
//    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent i = new Intent(MainActivity.this, ShowEventActivity.class);
                if (markerId.containsKey(marker)) {
                    i.putExtra("event_id", markerId.get(marker));
                    i.putExtra("event_author", marker.getSnippet());
                    i.putExtra("event_location", marker.getTitle());
                    i.putExtra("event_Lat", marker.getPosition().latitude);
                    i.putExtra("event_Lng", marker.getPosition().longitude);
                }
                MainActivity.this.startActivity(i);
            }
        });

        markEventsOnTheMap();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
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
    }

    public void markEventsOnTheMap() {
        ////////DEBUG
        markerId = new HashMap<>();
        LatLng foodFak = new LatLng(51.4935117f, 7.4161913f);
//        Marker m = mMap.addMarker(new MarkerOptions()
//                .position(foodFak)
//                .title("Food Fakultät")
//                .snippet("HerrDöner"));
//        markerId.put(m, 123);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(foodFak, 15.0f));
//
//        LatLng mensaTU = new LatLng(51.4930692f, 7.4139248f);
//        m = mMap.addMarker(new MarkerOptions()
//                .position(mensaTU)
//                .title("Hauptmensa TU Dortmund")
//                .snippet("Powereater93"));
//        markerId.put(m, 124);
        /////////

        RestHelper helper = new RestHelperImpl();
        RestHelper.DataReceivedListener<List<Event>> listener = new RestHelper.DataReceivedListener<List<Event>>() {
            @Override
            public void onDataReceived(List<Event> data) {
                if (data != null) {
                    markerId = new HashMap<>();

                    LatLng eLocation = new LatLng(51.4930692f, 7.4139248f);
                    for (Event e : data) {
                        eLocation = new LatLng(Double.parseDouble(e.getX()), Double.parseDouble(e.getY()));

                        Marker m = mMap.addMarker(new MarkerOptions().position(eLocation).title(e.getName())
                                .snippet(e.getAuthor() + "")); //TODO: get user name
                        markerId.put(m, e.getId());
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eLocation, 10.0f));
                }
            }
        };
        helper.eventGetAll(listener);
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
                    }
                    nearbyNumber = (TextView) findViewById(R.id.nearbyNumber);
                    nearbyNumber.setText(String.valueOf(counter));
                }
            }
        };
        helper.eventGetAll(listener);

        RestHelper helper2 = new RestHelperImpl();
        RestHelper.DataReceivedListener<Integer> listener2 = new RestHelper.DataReceivedListener<Integer>() {
            @Override
            public void onDataReceived(Integer data) {
                startingNumber = (TextView) findViewById(R.id.startingNumber);
                startingNumber.setText(String.valueOf(data));
            }
        };
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
