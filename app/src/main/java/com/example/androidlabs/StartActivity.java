package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "StartActivity";
    protected Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(ACTIVITY_NAME, "in onCreate(Bundle)");

        startButton = findViewById(R.id.startButton);

        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(ACTIVITY_NAME, "in onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(ACTIVITY_NAME, "in onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(ACTIVITY_NAME, "in onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(ACTIVITY_NAME, "in onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(ACTIVITY_NAME, "in onDestroy()");
    }

    public void sendStartData(View view) {

        // start ListItemsActivity activity
        Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
        startActivityForResult(intent, 5);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == 5) {
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");

            if (responseCode == Activity.RESULT_OK) {
                Log.i(ACTIVITY_NAME, "Received response code RESULT_OK");

                String messagePassed = data.getStringExtra("Response");

                Log.i(ACTIVITY_NAME, "R.string.startReturnFromCliMessage: " + getString(R.string.startReturnFromCliMessage));
                Log.i(ACTIVITY_NAME, "messagePassed: " + messagePassed);

                Toast toast = Toast.makeText(this, getString(R.string.startReturnFromCliMessage) + " " + messagePassed, Toast.LENGTH_LONG); //this is the ListActivity
                toast.show(); //display your message box
            } else {
                Log.i(ACTIVITY_NAME, "Got response code: " + responseCode);
            }
        }
        else {
            Log.i(ACTIVITY_NAME, "Got requestCode: "+requestCode);
        }
    }

    public void handleStartChat(View view) {
        Log.i(ACTIVITY_NAME, "Entered handleStartChat(View)");

        // start StartActivity activity
        Intent intent = new Intent(StartActivity.this, ChatWindowActivity.class);
        startActivityForResult(intent, 82);
    }

    public void handleStartWeatherForecast(View view) {
        Log.i(ACTIVITY_NAME, "Entered handleStartWeatherForecast(View)");

        // start StartActivity activity
        Intent intent = new Intent(StartActivity.this, WeatherForecastActivity.class);
        startActivityForResult(intent, 83);
    }
}
