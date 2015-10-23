package weatherwallet.heaven.zion.weatherwallet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import weatherwallet.heaven.zion.weatherwallet.R;
import weatherwallet.heaven.zion.weatherwallet.adapters.DayAdapter;
import weatherwallet.heaven.zion.weatherwallet.weather.Day;

public class DailyForecastActivity extends Activity {

    private Day[] mDays;
    @Bind(android.R.id.list)
    ListView mListView;
    @Bind(android.R.id.empty)
    TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dayOfTheWeek = mDays[position].getDayOfTheWeek();
                String conditions = mDays[position].getSummary();
                String highTemp = mDays[position].getTemperatureMax() + "";
                String messages = String.format("On %s the highest temperature will be %s F and it will be a %s",
                        dayOfTheWeek,
                        highTemp,
                        conditions);

                Toast.makeText(DailyForecastActivity.this, messages, Toast.LENGTH_LONG).show();
            }
        });

    }
}







