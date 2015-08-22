package weatherwallet.heaven.zion.weatherwallet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKEY = "82eb05ec4250bdb76e1c94e9de47a522";
        double latitude = 37.8267;
        double longitude = -122.423;
        if (isNetworkAvailable()) {
            String forcastURL = "https://api.forecast.io/forecast/" + apiKEY + "/" + latitude + "," + longitude;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forcastURL)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jasonData=response.body().string();
                        Log.v(TAG, jasonData);
                        if (response.isSuccessful()) {
                             mCurrentWeather = getCurrentDetsils(jasonData);
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception Cought", e);
                    }
                    catch (JSONException  e) {
                        Log.e(TAG, "Exception Cought", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, (R.string.netwotk_unavailable),
                    Toast.LENGTH_LONG).show();
        }

        Log.d(TAG, "The Main UI code is runing!");

    }

    private CurrentWeather getCurrentDetsils(String jasonData) throws JSONException{
        JSONObject forecast = new JSONObject(jasonData);
        String timezone =  forecast.getString("timezone");
        Log.i(TAG,"From JSON"+timezone);

        JSONObject currently = forecast.getJSONObject("currently");
        CurrentWeather currentWeather=new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummery(currently.getString("summary"));
        currentWeather.setTimeZone(timezone);
        Log.d(TAG,currentWeather.getFormattedTime());
        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        networkInfo = manager.getActiveNetworkInfo() ;
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertUserMessage dialog = new AlertUserMessage();
        dialog.show(getFragmentManager(), "Error_dialog");
    }


}
