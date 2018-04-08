package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

public class MessageDetailsActivity extends AppCompatActivity implements MessageFragment.OnMessageFragmentInteractionListener {

    protected static String ACTIVITY_NAME = "MessageDetailsActivity";

    private String messageString;
    private String messageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        FrameLayout frameLayout = findViewById(R.id.messageDetailsFrameLayout);

        if (frameLayout != null) {
            if (getMessageDetailsFromSharedPreferences()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                MessageFragment fragment = MessageFragment.newInstance(messageString, messageId);
                fragmentTransaction.add(R.id.messageDetailsFrameLayout, fragment);
                fragmentTransaction.commit();
            }
        }
        else {
            Log.e(ACTIVITY_NAME, "Got null FrameLayout; check IDs?");
        }
    }

    public void onDeleteMessageButtonClicked(String messageToDelete) {
        Log.i(ACTIVITY_NAME, "Got message to delete id: "+messageToDelete);

        Intent intent = new Intent();
        intent.putExtra(getString(R.string.chat_message_to_delete_key), messageId);
        setResult(MessageFragment.DELETE_MESSAGE_RETURN_CODE, intent);
        finish();
    }

    private boolean getMessageDetailsFromSharedPreferences() {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        messageId = sharedPref.getString(getString(R.string.chat_message_id_key), "");
        messageString = sharedPref.getString(getString(R.string.chat_message_details_key), "");

        if (messageId.isEmpty()) {
            Log.e(ACTIVITY_NAME, "Did not load message ID from SharedPreferences!");
            return false;
        }

        if (messageString.isEmpty()) {
            Log.e(ACTIVITY_NAME, "Did not load message text from SharedPreferences!");
            return false;
        }

        return true;
    }
}
