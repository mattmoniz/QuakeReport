package com.example.android.quakereport;

public class Earthquake {

    // Earthquake location
    private String mLocation;

    // Earthquake magnitude
    private double mMagnitude;

    // Earthquake date
    private Long mDate;

    /** Time of the earthquake */
    private long mTimeInMilliseconds;

    private String mUrl;


    /*
     * Create a new Earthquake object.
     *
     * @param vLocation is the location of the earthquake
     * @param vMagnitude is the magnitude of the earthquake
     * @param vtimeInMilliseconds is the date of the earthquake in milliseconds
     * */
    public Earthquake(double vMagnitude,String vLocation,  long vtimeInMilliseconds, String url)
    {
        mLocation = vLocation;
        mMagnitude = vMagnitude;
        mTimeInMilliseconds = vtimeInMilliseconds;
        mUrl = url;
    }

    /**
     * Get the earthquake location
     */
    public String getLocation() {
        return mLocation;
    }

    /**
     * Get the earthquake magnitude
     */
    public double getMagnitude() {
        return mMagnitude;
    }

    /**
     * Get the earthquake magnitude
     */
    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    /**
     * Returns the website URL to find more information about the earthquake.
     */
    public String getUrl() { return mUrl; }


}
