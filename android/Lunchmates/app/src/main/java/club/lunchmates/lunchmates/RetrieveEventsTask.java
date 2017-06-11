package club.lunchmates.lunchmates;

import android.os.AsyncTask;
import android.widget.TextView;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RetrieveEventsTask extends AsyncTask<String, Void, String> {

        private TextView t;
        public RetrieveEventsTask(TextView t){
            this.t = t;
        }

        protected String doInBackground(String... urli) {
            URL url = null;
            try {
                url = new URL(urli[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    ByteArrayOutputStream buf = new ByteArrayOutputStream();

                    int result = in.read();
                    while (result != -1) {
                        buf.write((byte) result);
                        result = in.read();
                    }

                    String answer = buf.toString("UTF-8");
                    return answer;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



        protected void onPostExecute(String result)
        {
            Pattern pattern = Pattern.compile("\\{[^}]*\\}");
            Matcher matcher = pattern.matcher(result);
            LinkedList<String> list = new LinkedList<String>();
            // Loop through and find all matches and store them into the List
            while(matcher.find()) {
                list.add(matcher.group());
            }
            t.setText(Integer.toString(list.size()));
        }
}
