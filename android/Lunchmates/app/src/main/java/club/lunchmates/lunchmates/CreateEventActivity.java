package club.lunchmates.lunchmates;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {
    private boolean locationTextCleared = false;
    private boolean timeTextCleared = false;
    private boolean dateTextCleared = false;
    private TimePicker eventTimePicker;
    private DatePicker eventDatePicker;
    private Calendar calendar;
    private String format = "";
    private Button btnCreateEvent;
    private int currHour, currMin, inputHour, inputMin;
    private int currY, currM, currD, inputY, inputM, inputD;
    private String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDatePicker();
        initTimePicker();
        btnCreateEvent = (Button) findViewById(R.id.button_create_event);
        btnCreateEvent.setEnabled(false);

        final EditText locationEdit = (EditText) findViewById(R.id.text_location);
        locationEdit.clearFocus();
        locationEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !locationTextCleared) {
                    locationEdit.setText("");
                    locationTextCleared = true;
                } else {
                    location = locationEdit.getText().toString();
                    if (!checkLocation()) {
                        Toast.makeText(CreateEventActivity.this, "Bitte das Ort prüfen!", Toast.LENGTH_SHORT).show();
                    }
                    if (checkLocation() && checkTime() && checkDate()) {
                        btnCreateEvent.setEnabled(true);
                    }
                }
            }
        });

        final TextView timeEdit = (TextView) findViewById(R.id.text_time);
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeTextCleared) {
                    timeEdit.setText("");
                    timeTextCleared = true;
                    eventTimePicker.setVisibility(View.VISIBLE);
                } else {
                    calendar = Calendar.getInstance();
                    currHour = calendar.get(Calendar.HOUR_OF_DAY);
                    currMin = calendar.get(Calendar.MINUTE);
                    eventTimePicker.setVisibility(View.VISIBLE);
//                    eventTimePicker.
                    inputHour = 0;//Integer.parseInt(timeEdit.getText().toString().split(":")[0]);
                    inputMin = 0;//Integer.parseInt(timeEdit.getText().toString().split(":")[1]);

                    if (!checkTime()) {
                        Toast.makeText(CreateEventActivity.this, "Bitte die Uhrzeit prüfen! H:M>" + currHour + ":" + currMin, Toast.LENGTH_SHORT).show();
                    }
                    if (checkLocation() && checkTime() && checkDate()) {
                        btnCreateEvent.setEnabled(true);
                    }
                }
            }
        });

//        timeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus && !timeTextCleared) {
//                    timeEdit.setText("");
//                    timeTextCleared = true;
//                } else {
//                    calendar = Calendar.getInstance();
//                    currHour = calendar.get(Calendar.HOUR_OF_DAY);
//                    currMin = calendar.get(Calendar.MINUTE);
//                    inputHour = Integer.parseInt(timeEdit.getText().toString().split(":")[0]);
//                    inputMin = Integer.parseInt(timeEdit.getText().toString().split(":")[1]);
//
//                    if (!checkTime()) {
//                        Toast.makeText(CreateEventActivity.this, "Bitte die Uhrzeit prüfen! H:M>" + currHour + ":" + currMin, Toast.LENGTH_SHORT).show();
//                    }
//                    if (checkLocation() && checkTime() && checkDate()) {
//                        btnCreateEvent.setEnabled(true);
//                    }
//                }
//            }
//        });

        final TextView dateEdit = (TextView) findViewById(R.id.text_date);
        dateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !dateTextCleared) {
                    dateEdit.setText("");
                    dateTextCleared = true;
                } else {
                    calendar = Calendar.getInstance();
                    currY = calendar.get(Calendar.YEAR);
                    currM = calendar.get(Calendar.MONTH);
                    currD = calendar.get(Calendar.DAY_OF_MONTH);
                    if (!checkDate()) {
                        Toast.makeText(CreateEventActivity.this, "Bitte das Datum prüfen! Y.M.D>" + currY + "." + currM + "." + currD, Toast.LENGTH_SHORT).show();
                    }
                    if (checkLocation() && checkTime() && checkDate()) {
                        btnCreateEvent.setEnabled(true);
                    }
                }
            }
        });

        btnCreateEvent = (Button) findViewById(R.id.button_create_event);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateEventActivity.this, "Not implemented yet!", Toast.LENGTH_SHORT).show();
            }
        });
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

    boolean checkLocation() {
        if (location.length() < 2) {
            return false;
        } else {
            return true;
        }
    }

    boolean checkTime() {
//        if((inputHour < currHour) && isToday()) return false;
//        if((inputHour == currHour) && (inputMin < currHour) && isToday()) return false;

        return true;
    }

    boolean checkDate() {
        //if date = today then time must be in future
        if ((inputHour < currHour) && isToday()) return false;
        if ((inputHour == currHour) && (inputMin < currHour) && isToday()) return false;

        //date must be in future too
        if (inputY < currY) return false;
        if ((inputM < currM) && (inputY == currY)) return false;
        if ((inputD < currD) && (inputM == currM) && (inputY == currY)) return false;

        return true;
    }

    boolean isToday() {
        if (inputY == currY && inputM == currM && inputD == currD) {
            return true;
        }
        return false;
    }

    void initTimePicker() {
        eventTimePicker = (TimePicker) findViewById(R.id.event_time_picker);
        eventTimePicker.setVisibility(View.GONE);
        eventTimePicker.setIs24HourView(true);
    }

    void initDatePicker() {

    }
}
