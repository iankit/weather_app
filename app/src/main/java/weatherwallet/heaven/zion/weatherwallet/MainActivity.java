package weatherwallet.heaven.zion.weatherwallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    String apiKEY = "82eb05ec4250bdb76e1c94e9de47a522";
    double latitude  = 37.8267;
    double longitude = -122.423;
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
                    if (response.isSuccessful()){
                        Log.v(TAG,response.body().string());
                    }
                    else{
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    Log.e(TAG,"Exception Cought",e);
                }
            }
        });
        Log.d(TAG, "The Main UI code is runing!");

    }

    private void alertUserAboutError() {
        AlertUserMessage dialog = new AlertUserMessage();
        dialog.show(getFragmentManager(),"Error_dialog");
    }


}
