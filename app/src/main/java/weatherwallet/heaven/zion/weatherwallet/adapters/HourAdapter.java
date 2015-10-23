package weatherwallet.heaven.zion.weatherwallet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import weatherwallet.heaven.zion.weatherwallet.R;
import weatherwallet.heaven.zion.weatherwallet.weather.Hour;

/**
 * Created by Zion on 22/10/15.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    private Hour[] mHours;
    private Context mContext;

    public HourAdapter(Context contexts,Hour[] hours){
        mContext = contexts;
        mHours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_element,parent,false);
        HourViewHolder viewHolder = new HourViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTimeLabel;
        public TextView mTemperatureLabel;
        public TextView mSummeryLabel;
        public ImageView mIconImageView;

        public HourViewHolder(View itemView) {
            super(itemView);
            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mTemperatureLabel=(TextView)itemView.findViewById(R.id.temperatureLabel);
            mSummeryLabel=(TextView) itemView.findViewById(R.id.summeryLabelHourly);
            mIconImageView=(ImageView)itemView.findViewById(R.id.iconImageLabel);
        }
        public void bindHour(Hour hour){
            mTimeLabel.setText(hour.getHour());
            mSummeryLabel.setText(hour.getSummary());
            mTemperatureLabel.setText(hour.getTemperature()+ "");
            mIconImageView.setImageResource(hour.getIconId());

        }

        @Override
        public void onClick(View v) {
            String time = mTimeLabel.getText().toString();
            String temperature= mTemperatureLabel.getText().toString() ;
            String summery= mSummeryLabel.getText().toString();
            String message = String.format("At %s it will be %s F and %s",
                    time,
                    temperature,
                    summery);

            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

        }
    }
}
