package com.example.androidlabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherForecastActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "WeatherForecastActivity";
    private static final String DOWNLOAD_URL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";

    protected ImageView weatherImageView;
    protected TextView weatherCurrentTempTextView;
    protected TextView weatherMinTempTextView;
    protected TextView weatherMaxTempTextView;
    protected ProgressBar weatherProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weatherImageView = findViewById(R.id.weatherImageView);
        weatherCurrentTempTextView = findViewById(R.id.weatherCurrentTempTextView);
        weatherMinTempTextView = findViewById(R.id.weatherMinTempTextView);
        weatherMaxTempTextView = findViewById(R.id.weatherMaxTempTextView);
        weatherProgressBar = findViewById(R.id.weatherProgressBar);

        weatherProgressBar.setVisibility(View.VISIBLE);

        ForecastQuery query = new ForecastQuery();
        query.execute();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        private Bitmap weatherPic;

        protected String doInBackground(String... input) {
            Log.i(ACTIVITY_NAME, "Started doInBackground");

            try {
                downloadUrl(DOWNLOAD_URL);
            }
            catch (IOException ioe) {
                Log.e(ACTIVITY_NAME, "Got IOException", ioe);
            }
            catch (XmlPullParserException xppe) {
                Log.e(ACTIVITY_NAME, "Got XmlPullParserException", xppe);
            }

            return "";
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.i(ACTIVITY_NAME, "Started onProgressUpdate");

            weatherProgressBar.setVisibility(View.VISIBLE);
            weatherProgressBar.setProgress(progress[0]);

        }

        protected void onPostExecute(String result) {
            Log.i(ACTIVITY_NAME, "I got: " + result);

            weatherImageView.setImageBitmap(weatherPic);
            weatherCurrentTempTextView.setText(currentTemp);
            weatherMinTempTextView.setText(minTemp);
            weatherMaxTempTextView.setText(maxTemp);

            weatherProgressBar.setVisibility(View.INVISIBLE);
        }


        private void downloadUrl(String urlString) throws IOException, XmlPullParserException {

            // Get input stream from URL
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();

            // Set up XML parser with input stream
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(conn.getInputStream(), null);
            parser.nextTag();

            readFeed(parser);
        }

        private void readTemperature(XmlPullParser parser) {
            String value = parser.getAttributeValue(null, "value");
            String min = parser.getAttributeValue(null, "min");
            String max = parser.getAttributeValue(null, "max");

            Log.i(ACTIVITY_NAME, "Found temperature tag. Got - value: "+value+" min: "+min+" max:"+max);

            publishProgress(25);
            if (min != null) {
                minTemp = min;
            }

            publishProgress(50);
            if (max != null) {
                maxTemp = max;
            }

            publishProgress(75);
            if (value != null) {
                currentTemp = value;
            }
        }

        private void readWeather(XmlPullParser parser) {
            String icon = parser.getAttributeValue(null, "icon");

            Log.i(ACTIVITY_NAME, "Found weather tag. Got - icon: "+icon);

            if (icon != null) {
                publishProgress(99);

                String localFileName = icon+".png";

                if (fileExistence(localFileName)) {
                    Log.i(ACTIVITY_NAME, "Loading previously-downloaded icon "+localFileName+" from disk");
                    weatherPic = readFileFromDisk(localFileName);
                }
                else {
                    Log.i(ACTIVITY_NAME, "New icon "+localFileName+"; getting from internet");
                    weatherPic = readFileFromInternet(icon);
                }
            }
        }

        private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

            parser.require(XmlPullParser.START_TAG, null, "current");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();

                Log.i(ACTIVITY_NAME, "XmlParser read tag: "+name);

                // Starts by looking for the entry tag
                if (name.equals("temperature")) {
                    readTemperature(parser);
                }
                else if (name.equals("weather")) {
                    readWeather(parser);
                }
                //else {
                //    skip(parser);
                //}
            }
        }

        // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
        // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
        // finds the matching END_TAG (as indicated by the value of "depth" being 0).
        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

        private boolean fileExistence(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        private Bitmap readFileFromDisk(String imageFile) {
            FileInputStream fis = null;
            try {
                File file = getBaseContext().getFileStreamPath(imageFile);
                fis = new FileInputStream(file);
                return BitmapFactory.decodeStream(fis);
            }
            catch (FileNotFoundException e) {
                Log.e(ACTIVITY_NAME, "Got FileNotFoundException: ", e);
            }

            return null;
        }

        private Bitmap readFileFromInternet(String icon) {
            String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";

            Log.i(ACTIVITY_NAME, "Built "+iconUrl+" as URL");

            Bitmap imageBitmap = HttpUtils.getImage(iconUrl);

            Log.i(ACTIVITY_NAME, "Got "+(imageBitmap==null?"null":"non-null")+" weatherPic from HttpUtils");

            if (imageBitmap != null) {
                try {
                    FileOutputStream outputStream = openFileOutput( icon + ".png", Context.MODE_PRIVATE);
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
                catch (FileNotFoundException fnfe) {
                    Log.e(ACTIVITY_NAME, "Got FileNotFoundException:", fnfe);
                    imageBitmap = null;

                }
                catch (IOException ioe) {
                    Log.e(ACTIVITY_NAME, "Got IOException:", ioe);
                    imageBitmap = null;
                }
            }

            return imageBitmap;
        }
    }
}
