package com.example.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbarActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "TestToolbarActivity";

    private String savedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        savedMessage = "";

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
                String textToDisplay = (savedMessage == null || savedMessage.isEmpty())?"You clicked action one!":savedMessage;
                Snackbar.make(findViewById(R.id.toolbar), textToDisplay, Snackbar.LENGTH_LONG)
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
                AlertDialog.Builder builderThree = new AlertDialog.Builder(this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();

                // This intermediate variable is necessary to be able to get R.id.newMessageEditText
                // later. Otherwise, it is always null.
                final View customDialogLayout = inflater.inflate(R.layout.dialog_three, null);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builderThree.setView(customDialogLayout)
                        // Add action buttons
                        .setPositiveButton(R.string.newMessageText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText newMessageEditText = customDialogLayout.findViewById(R.id.newMessageEditText);

                                if (newMessageEditText == null) {
                                    Log.e(ACTIVITY_NAME, "newMessageEditText is null; WTF?");
                                }
                                else if (newMessageEditText.getText() == null) {
                                    Log.e(ACTIVITY_NAME, "newMessageEditText.getText() is null; WTF?");
                                }
                                else {
                                    savedMessage = newMessageEditText.getText().toString();
                                    Log.i(ACTIVITY_NAME, "User clicked New Message. Saving: "+savedMessage);
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i(ACTIVITY_NAME, "User cancelled");
                                // User cancelled the dialog
                            }
                        });
                AlertDialog dialogThree = builderThree.create();
                dialogThree.show();
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
