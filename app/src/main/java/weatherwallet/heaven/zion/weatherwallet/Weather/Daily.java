package weatherwallet.heaven.zion.weatherwallet.Weather;

/**
 * Created by Zion on 16/10/15.
 */
public class Daily {
    private long mTime;
    private String Summery;
    private double mTemperature;
    private String mIcon;
    private String mTimeZone;

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummery() {
        return Summery;
    }

    public void setSummery(String summery) {
        Summery = summery;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }
}
