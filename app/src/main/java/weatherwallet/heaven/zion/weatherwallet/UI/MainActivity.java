package weatherwallet.heaven.zion.weatherwallet.UI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import weatherwallet.heaven.zion.weatherwallet.R;
import weatherwallet.heaven.zion.weatherwallet.Weather.Current;
import weatherwallet.heaven.zion.weatherwallet.Weather.Forcast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Forcast mForcast;

    @Bind(R.id.timeLabel)
    TextView mTimeLabel;

    @Bind(R.id.temperatureLevel)
    TextView mTemperatureLabel;

    @Bind(R.id.humidityValue)
    TextView mHumidityValue;

    @Bind(R.id.precipValue)
    TextView mPreciValue;

    @Bind(R.id.summeryTextView)
    TextView mSummeryTextView;

    @Bind(R.id.iconImageView)
    ImageView mIconImageView;

    @Bind(R.id.refreshImageView)
    ImageView mRefreshImageView;

    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);


        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }


        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForcast(latitude, longitude);
            }
        });

        getForcast(latitude, longitude);

        Log.d(TAG, "The Main UI code is runing!");

    }

    private void getForcast(double latitude, double longitude) {
        String apiKEY = "82eb05ec4250bdb76e1c94e9de47a522";

        if (isNetworkAvailable()) {
            togglrRefresh();
            String forcastURL = "https://api.forecast.io/forecast/"
                    + apiKEY + "/" + latitude + "," + longitude + "?units=si";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forcastURL)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            togglrRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            togglrRefresh();
                        }
                    });
                    try {
                        String jasonData = response.body().string();
                        Log.v(TAG, jasonData);
                        if (response.isSuccessful()) {
                            mForcast = forcastDetails(jasonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception Cought", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception Cought", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, (R.string.netwotk_unavailable),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void togglrRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForcast.getCurrent();
        mTemperatureLabel.setText(current.getTemperature() + "");
        mHumidityValue.setText(current.getHumidity() + "");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mPreciValue.setText(current.getPrecipValue() + "%");
        mSummeryTextView.setText(current.getSummery());
        Drawable drawable = ContextCompat.getDrawable(this, current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forcast forcastDetails(String jasonData) throws JSONException {
        Forcast forcast = new Forcast();
        forcast.setCurrent(getCurrentDetsils(jasonData));

        return forcast;
    }

    private Current getCurrentDetsils(String jasonData) throws JSONException {
        JSONObject forecast = new JSONObject(jasonData);
        String timezone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");
        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipValue(currently.getDouble("precipProbability"));
        current.setSummery(currently.getString("summary"));
        current.setTimeZone(timezone);
        Log.d(TAG, current.getFormattedTime());
        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        networkInfo = manager.getActiveNetworkInfo();
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
