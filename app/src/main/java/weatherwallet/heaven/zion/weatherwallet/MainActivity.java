package weatherwallet.heaven.zion.weatherwallet;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather mCurrentWeather;

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
    @Bind(R.id.refreshImageView) ImageView mRefreshImageView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);
        final double latitude = 37.8267;
        final double longitude = -122.423;
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForcast(latitude,longitude);
            }
        });

        getForcast(latitude,longitude);

        Log.d(TAG, "The Main UI code is runing!");

    }

    private void getForcast(double latitude , double longitude) {
        String apiKEY = "82eb05ec4250bdb76e1c94e9de47a522";

        if (isNetworkAvailable()) {
            togglrRefresh();
            String forcastURL = "https://api.forecast.io/forecast/"
                    + apiKEY + "/" + latitude + "," + longitude;

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
                            mCurrentWeather = getCurrentDetsils(jasonData);
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
        if(mProgressBar.getVisibility()==View.INVISIBLE){
        mProgressBar.setVisibility(View.VISIBLE);
        mRefreshImageView.setVisibility(View.INVISIBLE);
    }
    else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrentWeather.getTemperature() + "");
        mHumidityValue.setText(mCurrentWeather.getHumidity()+"");
        mTimeLabel.setText("At " + mCurrentWeather.getFormattedTime() + " it will be");
        mPreciValue.setText(mCurrentWeather.getPrecipValue() + "%");
        mSummeryTextView.setText(mCurrentWeather.getSummery());
        Drawable drawable = ContextCompat.getDrawable(this,mCurrentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private CurrentWeather getCurrentDetsils(String jasonData) throws JSONException {
        JSONObject forecast = new JSONObject(jasonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON" + timezone);

        JSONObject currently = forecast.getJSONObject("currently");
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipValue(currently.getDouble("precipProbability"));
        currentWeather.setSummery(currently.getString("summary"));
        currentWeather.setTimeZone(timezone);
        Log.d(TAG, currentWeather.getFormattedTime());
        return currentWeather;
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
