package club.lunchmates.lunchmates;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import club.lunchmates.lunchmates.data.Event;
import club.lunchmates.lunchmates.data.Position;
import club.lunchmates.lunchmates.rest.RestHelperImpl;
import club.lunchmates.lunchmates.rest.interfaces.RestHelper;


public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://10.0.2.2:9999/";
    private TextView nearbyNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t = (TextView) findViewById(R.id.nearbyNumber);
        //new RetrieveEventsTask(t).execute(getAbsoluteUrl("events"));
        RestHelper helper = new RestHelperImpl();

        RestHelper.DataReceivedListener<List<Event>> listener = new RestHelper.DataReceivedListener<List<Event>>() {
            @Override
            public void onDataReceived(List<Event> data) {
                if(data != null) {
                    for(Event e : data) {
                        Log.i("MainActivity", "Event " + e.getName() + " " + e.getX() + " " + e.getY());
                    }
                }
            }
        };
        helper.eventGetAll(listener);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
