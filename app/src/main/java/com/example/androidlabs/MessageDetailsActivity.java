package com.example.androidlabs;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class MessageDetailsActivity extends AppCompatActivity implements MessageFragment.OnFragmentInteractionListener {

    protected static String ACTIVITY_NAME = "MessageDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        FrameLayout frameLayout = findViewById(R.id.messageDetailsFrameLayout);

        if (frameLayout != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            MessageFragment fragment = new MessageFragment();
            fragmentTransaction.add(R.id.messageDetailsFrameLayout, fragment);
            fragmentTransaction.commit();
        }
        else {
            Log.e(ACTIVITY_NAME, "Got null FrameLayout; check IDs?");
        }
    }

    public void onFragmentInteraction(Uri uri) {
        // nothing; just trying to shut up Android Studio
    }
}
