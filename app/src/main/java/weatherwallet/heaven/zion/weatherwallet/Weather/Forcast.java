package weatherwallet.heaven.zion.weatherwallet.Weather;

/**
 * Created by Zion on 16/10/15.
 */
public class Forcast {
    private Current mCurrent;
    private Hourly[] mHourly;
    private Daily[] mDaily;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hourly[] getHourly() {
        return mHourly;
    }

    public void setHourly(Hourly[] hourly) {
        mHourly = hourly;
    }

    public Daily[] getDaily() {
        return mDaily;
    }

    public void setDaily(Daily[] daily) {
        mDaily = daily;
    }
}
