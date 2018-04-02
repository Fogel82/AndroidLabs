package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindowActivity extends AppCompatActivity implements MessageFragment.OnMessageFragmentInteractionListener {

    protected static final String ACTIVITY_NAME = "ChatWindowActivity";

    private static final int MESSAGE_DETAILS_START_CODE = 84;

    protected ListView chatListView;
    protected EditText chatEditText;
    protected Button chatSendButton;

    protected ArrayList<String> chatMessagesList;

    protected ChatAdapter chatAdapter;

    protected SQLiteDatabase writableDb;

    protected boolean isTabletView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        isTabletView = (findViewById(R.id.chatWindowFrameLayout) != null);

        chatListView = findViewById(R.id.chatListView);
        chatEditText = findViewById(R.id.chatEditText);
        chatSendButton = findViewById(R.id.chatSendButton);

        chatMessagesList = new ArrayList<String>();

        loadMessagesFromDatabase();

        // ChatWindowActivity is a Context
        chatAdapter = new ChatAdapter(this);

        chatListView.setAdapter(chatAdapter);

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(ACTIVITY_NAME, "entered handleMessageListClick(parent, view, position: "+position+", id: "+id+")");

                String selectedMessage = chatMessagesList.get(position);
                Log.i(ACTIVITY_NAME, "selectedMessage: "+selectedMessage);

                if (isTabletView) {
                    // launch Fragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    MessageFragment fragment = MessageFragment.newInstance(selectedMessage, String.valueOf(id));
                    fragmentTransaction.add(R.id.chatWindowFrameLayout, fragment);
                    fragmentTransaction.commit();
                }
                else {
                    Log.i(ACTIVITY_NAME, "Not in tablet view; launching MessageDetailsActivity");

                    // Write message details to SharedPreferences for activity to use.
                    Context context = getApplicationContext();
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    Log.i(ACTIVITY_NAME, "Writing item info to SharedPreferences. selectedMessage: "+selectedMessage+" id: "+id+".");

                    // TODO: this ID is wrong. Need a method to query the DB for the string and return the DB ID here.

                    editor.putLong(getString(R.string.chat_message_id_key), id);
                    editor.putString(getString(R.string.chat_message_details_key), selectedMessage);
                    editor.apply();

                    // start MessageDetailsActivity activity
                    Intent intent = new Intent(ChatWindowActivity.this, MessageDetailsActivity.class);
                    startActivityForResult(intent, MESSAGE_DETAILS_START_CODE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        writableDb.close();
    }

    public void onDeleteMessageButtonClicked(String messageToDelete) {
        // from fragment?
        Log.i(ACTIVITY_NAME, "Entered onDeleteMessageButtonClicked("+messageToDelete+")");
        deleteMessageFromDatabase(messageToDelete);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // from MessageDetailsActivity?
        if (requestCode == MESSAGE_DETAILS_START_CODE) {
            if (resultCode == MessageFragment.DELETE_MESSAGE_RETURN_CODE) {
                String idToDelete = data.getStringExtra(getString(R.string.chat_message_to_delete_key));

                Log.i(ACTIVITY_NAME, "Got message id: "+idToDelete+" for deletion.");
                deleteMessageFromDatabase(idToDelete);
            }
            else {
                Log.i(ACTIVITY_NAME, "Got MessageDetails start code, but got result code: "+resultCode+"; doing nothing.");
            }
        }
        else {
            Log.i(ACTIVITY_NAME, "Got request code: "+requestCode+"; doing nothing.");
        }
    }

    private void deleteMessageFromDatabase(String messageId) {
        Log.i(ACTIVITY_NAME, "Entered deleteMessageFromDatabase("+messageId+")");
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);

        writableDb = dbHelper.getWritableDatabase();

        Cursor c = null;
        try {
            c = writableDb.rawQuery("select * from "+ChatDatabaseHelper.TABLE_NAME+" where "+ChatDatabaseHelper.KEY_ID+" = ?", new String[] { messageId });

            Log.i(ACTIVITY_NAME, "Cursor’s column count = " + c.getColumnCount() );

            for (int i=0; i<c.getColumnCount(); i++) {
                Log.i(ACTIVITY_NAME, "Column "+i+": "+c.getColumnName(i));
            }

            while(c.moveToNext() ) {
                String retrievedId = c.getString( 0 );
                String retrievedChatMessage = c.getString(1);
                Log.i(ACTIVITY_NAME, "ID:" + retrievedId + " Message: "+retrievedChatMessage );

                if (retrievedId == null || !retrievedId.equals(messageId)) {
                    Log.e(ACTIVITY_NAME, "Got bad ID from SQL: "+retrievedId);
                }
                else {
                    // TODO: sql delete
                    chatMessagesList.remove(retrievedChatMessage);
                    chatAdapter.notifyDataSetChanged();
                }
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
