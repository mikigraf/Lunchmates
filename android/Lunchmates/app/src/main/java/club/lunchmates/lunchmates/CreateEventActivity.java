package club.lunchmates.lunchmates;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.google.android.gms.location.places.PlaceReport; // "Place" is not resolved
//import com.google.android.gms.location.places.ui.PlacePicker; // "ui" is not resolved
//import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import club.lunchmates.lunchmates.controller.PreferencesControllerImpl;
import club.lunchmates.lunchmates.controller.interfaces.PreferencesController;
import club.lunchmates.lunchmates.data.InsertResult;
import club.lunchmates.lunchmates.rest.RestHelperImpl;
import club.lunchmates.lunchmates.rest.interfaces.RestHelper;

public class CreateEventActivity extends AppCompatActivity {
    private boolean locationTextCleared = false;
    private TimePicker eventTimePicker;
    private DatePicker eventDatePicker;
    private Calendar calendar;
    private Button btnCreateEvent, btnTimeOK, btnDateOK;
    private int currHour, currMin;
    private int inputHour = 0;
    private int inputMin = 0;
    private int currY, currM, currD;
    private int inputY = 0;
    private int inputM = 0;
    private int inputD = 0;
    private boolean locationOK = false;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_DORTMUND = new LatLngBounds(
            new LatLng(51.492072, 7.401180), new LatLng(51.492072, 7.454738));
    private Place pickedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO show place picker instead of textEdit for event location, i started following this tutorial http://www.truiton.com/2015/04/using-new-google-places-api-android/

        btnCreateEvent = (Button) findViewById(R.id.button_create_event);
        btnCreateEvent.setEnabled(false);

        initDatePicker();
        initTimePicker();

        initLocationEdit();
        initTimeEdit();
        initDateEdit();

        initButtons();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(CreateEventActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(CreateEventActivity.this);
        }
        builder.setTitle("Änderungen nicht gespeichert!")
                .setMessage("Änderungen am Event werden nicht gespeichert! Bist du sicher, " +
                        "dass du diesen Screen verlassen willst?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        CreateEventActivity.this.startActivity(new Intent(CreateEventActivity.this, MainActivity.class));
                        CreateEventActivity.this.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

//    boolean checkLocation() {
//        if (location.length() < 2) {
//            btnCreateEvent.setEnabled(false);
//            return false;
//        } else {
//            return true;
//        }
//    }

    boolean checkTime() {
//        if((inputHour < currHour) && isToday()) return false;
//        if((inputHour == currHour) && (inputMin < currHour) && isToday()) return false;
        if (inputHour == 0) {
            btnCreateEvent.setEnabled(false);
            return false;
        }
        return true;
    }

    boolean checkDate() {
        btnCreateEvent.setEnabled(false);
        //if date = today then time must be in future
        if ((inputHour < currHour) && isToday()) return false;
        if ((inputHour == currHour) && (inputMin < currMin) && isToday()) return false;

        //date must be in future too
        if (inputY < currY) return false;
        if ((inputM < currM) && (inputY == currY)) return false;
        if ((inputD < currD) && (inputM == currM) && (inputY == currY)) return false;

        btnCreateEvent.setEnabled(true);
        return true;
    }

    boolean isToday() {
        return inputY == currY && inputM == currM && inputD == currD;
    }

    void initTimePicker() {
        eventTimePicker = (TimePicker) findViewById(R.id.event_time_picker);
        eventTimePicker.setIs24HourView(true);
        eventTimePicker.setVisibility(View.VISIBLE);
    }

    void initDatePicker() {
        eventDatePicker = (DatePicker) findViewById(R.id.event_date_picker);
        eventDatePicker.setVisibility(View.VISIBLE);
    }

    void initLocationEdit() {
        final TextView locationEdit = (TextView) findViewById(R.id.text_CE_location);
        locationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!locationTextCleared) {
                    locationEdit.setText("");
                    locationTextCleared = true;
                }
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_DORTMUND);
                    Intent intent = intentBuilder.build(CreateEventActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {
            locationOK = true;
            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }

            TextView locationEdit = (TextView) findViewById(R.id.text_CE_location);
            locationEdit.setText(name + ", " + address/* + ", " + attributions*/);
            pickedPlace = place;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void initTimeEdit() {
        final TextView timeEdit = (TextView) findViewById(R.id.text_CE_time);
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout tl = (TableLayout) findViewById(R.id.event_inputs_view);
                tl.setVisibility(View.GONE);
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.event_time_picker_layout);
                rl.setVisibility(View.VISIBLE);
            }
        });
    }

    void initDateEdit() {
        final TextView dateEdit = (TextView) findViewById(R.id.text_CE_date);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TableLayout tl = (TableLayout) findViewById(R.id.event_inputs_view);
                tl.setVisibility(View.GONE);
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.event_date_picker_layout);
                rl.setVisibility(View.VISIBLE);
            }
        });
    }

    void initButtons() {
        btnTimeOK = (Button) findViewById(R.id.btn_time_ok);
        btnTimeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currHour = calendar.get(Calendar.HOUR_OF_DAY);
                currMin = calendar.get(Calendar.MINUTE);
                if (Build.VERSION.SDK_INT < 23) {
                    inputHour = eventTimePicker.getCurrentHour();
                    inputMin = eventTimePicker.getCurrentMinute();
                } else {
                    inputHour = eventTimePicker.getHour();
                    inputMin = eventTimePicker.getMinute();
                }

                final TextView timeEdit = (TextView) findViewById(R.id.text_CE_time);
                timeEdit.setText(inputHour + ":" + inputMin);
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.event_time_picker_layout);
                rl.setVisibility(View.GONE);
                TableLayout tl = (TableLayout) findViewById(R.id.event_inputs_view);
                tl.setVisibility(View.VISIBLE);

                if (!checkTime()) {
                    Toast.makeText(CreateEventActivity.this, "Bitte die Uhrzeit prüfen!", Toast.LENGTH_SHORT).show();
                }
                if (locationOK && checkTime() && checkDate()) {
                    btnCreateEvent.setEnabled(true);
                }
            }
        });

        btnDateOK = (Button) findViewById(R.id.btn_date_ok);
        btnDateOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance(Locale.GERMANY);
                currY = calendar.get(Calendar.YEAR);
                currM = calendar.get(Calendar.MONTH) + 1; //Calender.MONTH: return value starts at 0!
                currD = calendar.get(Calendar.DAY_OF_MONTH);
                inputY = eventDatePicker.getYear();
                inputM = eventDatePicker.getMonth() + 1; //.getMonth(): return value starts at 0!
                inputD = eventDatePicker.getDayOfMonth();

                final TextView dateEdit = (TextView) findViewById(R.id.text_CE_date);
                dateEdit.setText(inputD + "." + inputM + "." + inputY);
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.event_date_picker_layout);
                rl.setVisibility(View.GONE);
                TableLayout tl = (TableLayout) findViewById(R.id.event_inputs_view);
                tl.setVisibility(View.VISIBLE);

                if (!checkDate()) {
                    Toast.makeText(CreateEventActivity.this, "Bitte das Datum prüfen!", Toast.LENGTH_SHORT).show();
                }
                if (locationOK && checkTime() && checkDate()) {
                    btnCreateEvent.setEnabled(true);
                }
                if (isToday()) {
                    Toast.makeText(CreateEventActivity.this, "Das Event findet heute statt!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCreateEvent = (Button) findViewById(R.id.button_create_event);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //send location, date and time to the server and go back to the main screen
                RestHelper helper = new RestHelperImpl();
                RestHelper.DataReceivedListener<InsertResult> listener = new RestHelper.DataReceivedListener<InsertResult>() {
                    @Override
                    public void onDataReceived(InsertResult createEventResult) {
                        if (createEventResult == null) {
                            Toast.makeText(CreateEventActivity.this, "Event konnte nicht erstellt werden!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreateEventActivity.this, "Event erstellt!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                Calendar eventDate = Calendar.getInstance();
                eventDate.set(inputY, inputM, inputD, inputHour, inputMin);
                PreferencesController currPrefs = new PreferencesControllerImpl(CreateEventActivity.this);
                Date d = new Date();
                d.setTime(eventDate.getTimeInMillis());
                helper.eventAdd(pickedPlace.getName().toString(),
                        String.valueOf(pickedPlace.getLatLng().latitude),
                        String.valueOf(pickedPlace.getLatLng().longitude),
                        d,
                        currPrefs.getUserId(),
                        listener);
                CreateEventActivity.this.startActivity(new Intent(CreateEventActivity.this, MainActivity.class));
                CreateEventActivity.this.finish();
            }
        });
    }
}
