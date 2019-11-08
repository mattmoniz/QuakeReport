package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the USGS dataset and return an {@link Earthquake} object to represent a single earthquake.
     */

    public static List<Earthquake> fetchEarthQuakeData(String requestUrl){
        //create url object
        URL url =createUrl(requestUrl);

        //perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try{
            jsonResponse=makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream",e);
        }
        // Extract relevant fields from the JSON response and create an {@link Earthquakes} object
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        // Return the {@link Earthquakes}
        return earthquakes;
    }


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */

    //Separate method to generate the Arraylist of Earthquake objects.
    public static List<Earthquake> extractFeatureFromJson(String earthQuakeJSON) {

        //If the JSON string is empty or null, then return early
        if(TextUtils.isEmpty(earthQuakeJSON)){
            return null;
        }

        //Create arrayList to add earthquakes too
        List<Earthquake>earthquakes=new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            //Create JSON Object from JSON Response string.
            JSONObject baseJsonResponse=new JSONObject(earthQuakeJSON);

            //Extract the JSONArray with key names "features"
            //features contains the list of earthquakes
            JSONArray earthquakeArray=baseJsonResponse.getJSONArray("features");


            //loop iterates through each earthquake listed in the features JSON array,
            //we take out whatever data we're looking for, create a new earthquake object with our data,
            //and then add it to the earthquakes arraylist on our screen.
            for (int i=0;i<earthquakeArray.length();i++){
                JSONObject earthquakeObject = earthquakeArray.getJSONObject(i);
                JSONObject properties = earthquakeObject.getJSONObject("properties");

                //All values can be displayed as strings because they
                // are only being displayed to the screen.
                double magnitude = properties.getDouble("mag");
                String location=properties.getString("place");
                long time=properties.getLong("time");
                // Extract the value for the key called "url"
                String url = properties.getString("url");


                //created new earthquake
                Earthquake earthquake=new Earthquake(magnitude,location,time,url);

                //adds new earthquake to the list of earthquakes
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        /** Sample JSON response for a USGS query */
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */

    private static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream !=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
             BufferedReader reader= new BufferedReader(inputStreamReader);
             String line = reader.readLine();
             while (line !=null){
                 output.append(line);
                 line=reader.readLine();
             }
        }
        return output.toString();
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }



}