package xyz.cybersapien.reqres;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by cybersapien on 28/3/18.
 */

public class NetUtils {

    private static final String TAG = "NetUtils";

    public static final String BASE_URL = "https://reqres.in";

    public static final int CONNECTION_TIMEOUT = 15000;
    public static final int READ_TIMEOUT = 15000;

    public static String createUserString(String name, String job) throws JSONException {
        JSONObject baseObject = new JSONObject();
        baseObject.put("name", name);
        baseObject.put("job", job);
        return baseObject.toString();
    }

    public static String createUser(String userJSON){
        Uri createUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("api")
                .appendPath("users").build();
        try {
            URL userURL = new URL(createUri.toString());
            return makeHttpRequest(userURL, "POST", userJSON);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String makeHttpRequest(URL url, String method, String body) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String response = "";

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(body.getBytes());
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                inputStream = connection.getInputStream();
                response = setupInputStream(inputStream);
            } else {
                Log.d(TAG, "makeHttpRequest: error in request: " + setupInputStream(connection.getErrorStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String makeGETRequest(URL url) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String jsonResponse = "";
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponse = setupInputStream(inputStream);
            } else {
                Log.d("NetworkUtils", "Error connecting to server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();
            connection.disconnect();
        }
        return jsonResponse;
    }

    private static String setupInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedInputStream = new BufferedReader(inputStreamReader);
        String jsonString = "";
        try {
            String line = bufferedInputStream.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedInputStream.readLine();
            }
            jsonString = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
