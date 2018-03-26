package com.example.androidlabs;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatListView = findViewById(R.id.chatListView);
        chatEditText = findViewById(R.id.chatEditText);
        chatSendButton = findViewById(R.id.chatSendButton);

        chatMessagesList = new ArrayList<String>();

        // ChatWindowActivity is a Context
        chatAdapter = new ChatAdapter(this);

        chatListView.setAdapter(chatAdapter);
    }

    public void sendChatMessage(View view) {
        Log.i(ACTIVITY_NAME, "Entered sendChatMessage(View)");

        String chatMessage = chatEditText.getText().toString();

        Log.i(ACTIVITY_NAME, "Got chat message "+chatMessage);
        chatMessagesList.add(chatMessage);

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

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindowActivity.this.getLayoutInflater();

            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);


            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }
    }
}
