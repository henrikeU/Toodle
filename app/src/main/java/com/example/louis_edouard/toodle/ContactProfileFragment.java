package com.example.louis_edouard.toodle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ContactProfileFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_NAME = "name";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_PHONE = "phone";
    private static final String ARG_CELL = "cell";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_CITY = "city";

    // TODO: Rename and change types of parameters
    private String mName, mEmail, mPhone, mCell, mAddress, mCity;
    private TextView name, email, phone, cell, address, city;
    private Button btnSend;

    // TODO: Rename and change types and number of parameters
    public static ContactProfileFragment newInstance(String name, String email, String phone, String cell, String address, String city) {
        ContactProfileFragment fragment = new ContactProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_PHONE, phone);
        args.putString(ARG_CELL, cell);
        args.putString(ARG_ADDRESS, address);
        args.putString(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    public ContactProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
            mEmail = getArguments().getString(ARG_EMAIL);
            mPhone = getArguments().getString(ARG_PHONE);
            mCell =getArguments().getString(ARG_CELL);
            mAddress = getArguments().getString(ARG_ADDRESS);
            mCity = getArguments().getString(ARG_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_profile, container, false);

        name = (TextView)v.findViewById(R.id.profile_name);
        email = (TextView)v.findViewById(R.id.profile_email);
        phone = (TextView)v.findViewById(R.id.profile_phone);
        cell = (TextView)v.findViewById(R.id.profile_cell);
        address = (TextView)v.findViewById(R.id.profile_address);
        city = (TextView)v.findViewById(R.id.profile_city);
        btnSend = (Button)v.findViewById(R.id.contactProfile_btnSend);

        name.setText(mName);
        email.setText(mEmail == null ? "N/A" : mEmail);
        phone.setText(mPhone == null ? "N/A" : mPhone);
        cell.setText(mCell == null ? "N/A" : mCell);
        address.setText(mAddress == null ? "N/A" : mAddress);
        city.setText(mCity == null ? "N/A" : mCity);
        phone.setOnClickListener(this);
        cell.setOnClickListener(this);
        email.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        return v;

    }


    @Override
    public void onClick(View v) {
        if(v == email && mEmail != null){
            if(Globals.isMailClientPresent(getActivity())) {
                try {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("message/rfc822");
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(sendIntent, null));
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Sending an email", "Mail failed", activityException);
                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.setTitle(getResources().getString(R.string.alert_title_mailApp_missing));
                dialog.setMessage(getResources().getString(R.string.alert_message_mailApp_missing));
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
        if(v == cell && mCell != null){
            callContact(mCell);
        }
        if(v == phone && mPhone != null){
           callContact(mPhone);
        }
        if(v == btnSend) {
            Intent intent = new Intent(getActivity(), SendMessageActivity.class);
            intent.putExtra(SendMessageActivity.ARG_NAME, mName);
            startActivity(intent);
        }
    }

    public void callContact(String number){
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+ number));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }
    }
}
