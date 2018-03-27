package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindowActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindowActivity";

    protected ListView chatListView;
    protected EditText chatEditText;
    protected Button chatSendButton;

    protected ArrayList<String> chatMessagesList;

    protected ChatAdapter chatAdapter;

    protected SQLiteDatabase writableDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatListView = findViewById(R.id.chatListView);
        chatEditText = findViewById(R.id.chatEditText);
        chatSendButton = findViewById(R.id.chatSendButton);

        chatMessagesList = new ArrayList<String>();

        loadMessagesFromDatabase();

        // ChatWindowActivity is a Context
        chatAdapter = new ChatAdapter(this);

        chatListView.setAdapter(chatAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        writableDb.close();
    }

    private void loadMessagesFromDatabase() {
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);

        writableDb = dbHelper.getWritableDatabase();

        Cursor c = null;
        try {
            c = writableDb.rawQuery("select * from "+ChatDatabaseHelper.TABLE_NAME, new String[] { });

            Log.i(ACTIVITY_NAME, "Cursor’s column count = " + c.getColumnCount() );

            for (int i=0; i<c.getColumnCount(); i++) {
                Log.i(ACTIVITY_NAME, "Column "+i+": "+c.getColumnName(i));
            }

            while(c.moveToNext() ) {
                String retrievedMsg = c.getString( 1 );
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + retrievedMsg );
                chatMessagesList.add(retrievedMsg);
            }

        }
        catch (Exception e) {
            Log.e(ACTIVITY_NAME, "Got exception", e);
        }
        finally {
            if (c != null) {
                c.close();
            }

        }
    }

    public void sendChatMessage(View view) {
        Log.i(ACTIVITY_NAME, "Entered sendChatMessage(View)");

        String chatMessage = chatEditText.getText().toString();

        Log.i(ACTIVITY_NAME, "Got chat message "+chatMessage);
        chatMessagesList.add(chatMessage);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ChatDatabaseHelper.KEY_MESSAGE, chatMessage);

        writableDb.insert(ChatDatabaseHelper.TABLE_NAME, "NullColumnName", contentValues);

        // clear chat text after sending the message
        chatEditText.setText("");

        // reload getCount()/getView()
        chatAdapter.notifyDataSetChanged();
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return chatMessagesList != null ? chatMessagesList.size() : 0;
        }

        public String getItem(int position) {
            if (chatMessagesList == null || chatMessagesList.size() < position) {
                return "";
            }

            return chatMessagesList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindowActivity.this.getLayoutInflater();

            View result;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);


            TextView message = result.findViewById(R.id.message_text);
            message.setText( getItem(position) ); // get the string at position
            return result;
        }
    }
}
