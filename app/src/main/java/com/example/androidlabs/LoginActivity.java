package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LoginActivity";

    protected Button loginButton;
    protected EditText loginEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(ACTIVITY_NAME, "in onCreate(Bundle)");

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loginButton = (Button)findViewById(R.id.loginButton);
        loginEditText = (EditText)findViewById(R.id.loginEditText);
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

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String retrievedEmail = sharedPref.getString(getString(R.string.default_email_key), getString(R.string.email_default_value));
        Log.i(ACTIVITY_NAME, "Got: "+retrievedEmail+" from SharedPreferences.");
        loginEditText.setText(retrievedEmail);
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

    public void sendLoginData(View view) {

        // write entered email address to SharedPreferences
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Log.i(ACTIVITY_NAME, "Writing email "+loginEditText.getText().toString()+" to SharedPreferences.");

        editor.putString(getString(R.string.default_email_key), loginEditText.getText().toString());
        editor.commit();

        // start StartActivity activity
        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
        startActivity(intent);
    }
}
