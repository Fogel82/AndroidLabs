package com.example.androidlabs;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WeatherForecastActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "WeatherForecastActivity";

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
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        private Bitmap weatherPic;

        protected String doInBackground(String... input) {
            Log.i(ACTIVITY_NAME, "Started doInBackground");

            return "";
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.i(ACTIVITY_NAME, "Started onProgressUpdate");

        }

        protected void onPostExecute(String result) {
            Log.i(ACTIVITY_NAME, "I got: "+result);
        }
    }
}
