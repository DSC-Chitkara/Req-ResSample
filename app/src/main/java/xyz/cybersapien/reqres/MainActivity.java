package xyz.cybersapien.reqres;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            String value = NetUtils.createUserString("Nikhil Bansal", "Droid Instructor");

            ReqResAsyncTask asyncTask = new ReqResAsyncTask();
            asyncTask.execute(value);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ReqResAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String value = strings[0];
            return NetUtils.createUser(value);
        }

        @Override
        protected void onPostExecute(String s) {
            TextView tv = findViewById(R.id.base_text_view);
            tv.setText(s);
        }
    }
}
