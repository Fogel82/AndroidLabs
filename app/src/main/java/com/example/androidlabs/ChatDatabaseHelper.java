package com.example.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static final int VERSION_NUM = 1;
    public static final String ACTIVITY_NAME = "ChatDatabaseHelper";
    public static final String DATABASE_NAME = "MeepMeep";
    public static final String TABLE_NAME = "ChatMessageTable";

    public static final String KEY_ID = "ID";
    public static final String KEY_MESSAGE = "MESSAGE";

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.i(ACTIVITY_NAME, "in onCreate(sqLiteDatabase)");

        sqLiteDatabase.execSQL( "CREATE TABLE "+
                TABLE_NAME+
                " ( "+
                KEY_ID+
                " INTEGER PRIMARY KEY AUTOINCREMENT , "+
                KEY_MESSAGE+
                " TEXT);"
        );

        Log.i(ACTIVITY_NAME, "finished onCreate(sqLiteDatabase)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        Log.i(ACTIVITY_NAME, "in onUpgrade(sqLiteDatabase). Old version: "+oldVer+" New version: "+newVer);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(sqLiteDatabase);

        Log.i(ACTIVITY_NAME, "finished onUpgrade(sqLiteDatabase)");
    }
}
