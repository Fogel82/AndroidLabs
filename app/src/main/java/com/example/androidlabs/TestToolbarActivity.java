package com.example.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class TestToolbarActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "TestToolbarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.action_one:
                Log.d(ACTIVITY_NAME, "action_one");
                Snackbar.make(findViewById(R.id.toolbar), "You clicked action one!", Snackbar.LENGTH_LONG)
                        .setAction("Action one", null).show();
                break;
            case R.id.action_two:
                Log.d(ACTIVITY_NAME, "action_two");
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_fire_missiles)
                        .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i(ACTIVITY_NAME, "User fired missiles! What a jerk!");
                                // FIRE ZE MISSILES!
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i(ACTIVITY_NAME, "User cancelled");
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog missilesDialog = builder.create();
                missilesDialog.show();
                break;
            case R.id.action_three:
                Log.d(ACTIVITY_NAME, "action_three");
                break;
            case R.id.action_about:

                Toast toast = Toast.makeText(this , getString(R.string.about_toast_text), Toast.LENGTH_LONG); //this is the ListActivity
                toast.show(); //display your message box
                break;
            default:
                Log.d(ACTIVITY_NAME, "unknown action");

        }

        return true;
    }
}
