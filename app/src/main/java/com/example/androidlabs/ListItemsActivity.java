package com.example.androidlabs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ListItemsActivity";

    protected static final int REQUEST_IMAGE_CAPTURE = 1;

    protected Switch cliSwitch;
    protected ImageButton cliImageButton;
    protected CheckBox cliCheckBox;
    protected RadioButton cliRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(ACTIVITY_NAME, "in onCreate(Bundle)");

        setContentView(R.layout.activity_list_items);
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

        cliSwitch = findViewById(R.id.cliSwitch);
        cliImageButton = findViewById(R.id.cliImageButton);
        cliCheckBox = findViewById(R.id.cliCheckBox);
        cliRadioButton = findViewById(R.id.cliRadioButton);
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

    public void handleImageButtonClick(View view) {
        // this is what happens when the image button is clicked
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            if (imageBitmap != null) {
                cliImageButton.setImageBitmap(imageBitmap);
            }
            else {
                Log.w(ACTIVITY_NAME, "Got null imageBitmap from camera!");
            }

        }
    }

    public void handleSwitchClick(View view) {
        boolean isSwitchOn = cliSwitch.isChecked();

        CharSequence text = (isSwitchOn ? getString(R.string.cli_switch_on_text) : getString(R.string.cli_switch_off_text));
        int duration = (isSwitchOn ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);

        Toast toast = Toast.makeText(this , text, duration); //this is the ListActivity
        toast.show(); //display your message box
    }

    public void handleCheckBoxClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.cliCheckBoxDialogMessage) //Add a dialog message to strings.xml

                .setTitle(R.string.message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent resultIntent = new Intent(  );
                        resultIntent.putExtra("Response", getString(R.string.cliCheckBoxClickedOkMessage));
                        setResult(Activity.RESULT_OK, resultIntent);

                        // finish the activity and return to the caller
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog; do nothing
                    }
                })
                .show();
    }
}
