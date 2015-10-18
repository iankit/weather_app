package weatherwallet.heaven.zion.weatherwallet.UI;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import weatherwallet.heaven.zion.weatherwallet.R;

public class DailyForcastActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forcast);


        String[] daysOfTheWeek = {"Sunday", "Monday", "Tuesday", "Wednsday", "Thrusday", "Friday", "Saturday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                daysOfTheWeek);
        setListAdapter(adapter);
    }
}
