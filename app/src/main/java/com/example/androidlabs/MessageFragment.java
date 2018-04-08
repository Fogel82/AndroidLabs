package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMessageFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    private static final String FRAGMENT_TYPE = "MessageFragment";

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CHAT_MESSAGE_STRING = "chat_message_string";
    private static final String ARG_CHAT_MESSAGE_ID = "chat_message_id";

    public static final int DELETE_MESSAGE_RETURN_CODE = 8899;

    private String messageString;
    private String messageId;

    private TextView messageFragmentMessageTextView;
    private TextView messageFragmentIdTextView;
    private Button messageFragmentDeleteButton;

    private OnMessageFragmentInteractionListener mListener;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param chatMessageString Chat message.
     * @param chatMessageId Message ID in DB.
     * @return A new instance of fragment MessageFragment.
     */
    public static MessageFragment newInstance(String chatMessageString, String chatMessageId) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHAT_MESSAGE_STRING, chatMessageString);
        args.putString(ARG_CHAT_MESSAGE_ID, chatMessageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            messageString = getArguments().getString(ARG_CHAT_MESSAGE_STRING);
            messageId = getArguments().getString(ARG_CHAT_MESSAGE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // init GUI objects...I hope
        messageFragmentMessageTextView = view.findViewById(R.id.messageFragmentMessageTextView);
        messageFragmentIdTextView = view.findViewById(R.id.messageFragmentIdTextView);
        messageFragmentDeleteButton = view.findViewById(R.id.messageFragmentDeleteButton);

        if (messageFragmentMessageTextView != null) {
            Log.i(FRAGMENT_TYPE, "Setting messageFragmentMessageTextView text to: "+messageString);
            messageFragmentMessageTextView.setText(messageString);
        }
        else {
            Log.e(FRAGMENT_TYPE, "Got null messageFragmentMessageTextView");
        }

        if (messageFragmentIdTextView != null) {
            Log.i(FRAGMENT_TYPE, "Setting messageFragmentIdTextView text to: "+messageId);
            messageFragmentIdTextView.setText(messageId);
        }
        else {
            Log.e(FRAGMENT_TYPE, "Got null messageFragmentIdTextView");
        }

        if (messageFragmentDeleteButton != null) {
            messageFragmentDeleteButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i(FRAGMENT_TYPE, "You clicked the delete button!");
                            onButtonPressed(messageId);
                        }
                    }
            );
        }
        else {
            Log.e(FRAGMENT_TYPE, "Got null messageFragmentDeleteButton");
        }
    }

    public void onButtonPressed(String messageToDelete) {
        if (mListener != null) {
            Log.i(FRAGMENT_TYPE, "mListener not null. Calling onDeleteMessageButtonClicked("+messageToDelete+")");
            mListener.onDeleteMessageButtonClicked(messageToDelete);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMessageFragmentInteractionListener) {
            mListener = (OnMessageFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMessageFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMessageFragmentInteractionListener {
        void onDeleteMessageButtonClicked(String messageToDelete);
    }
}
