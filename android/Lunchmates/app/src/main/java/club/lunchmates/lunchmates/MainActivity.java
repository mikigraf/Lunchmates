package club.lunchmates.lunchmates;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://10.0.2.2:9999/";
    private TextView nearbyNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t = (TextView) findViewById(R.id.nearbyNumber);
        new RetrieveEventsTask(t).execute(getAbsoluteUrl("events"));
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
