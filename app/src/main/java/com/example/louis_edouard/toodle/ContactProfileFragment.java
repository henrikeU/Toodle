package com.example.louis_edouard.toodle;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_PHONE = "phone";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_CITY = "city";
    private static final String ARG_POSITION = "position";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mName, mEmail, mPhone, mAddress, mCity;

    // TODO: Rename and change types and number of parameters
    public static ContactProfileFragment newInstance(String name, String email, String phone, String address, String city) {
        ContactProfileFragment fragment = new ContactProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_PHONE, phone);
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
            mParam1 = getArguments().getInt(ARG_POSITION);
            mName = getArguments().getString(ARG_NAME);
            mEmail = getArguments().getString(ARG_EMAIL);
            mPhone = getArguments().getString(ARG_PHONE);
            mAddress = getArguments().getString(ARG_ADDRESS);
            mCity = getArguments().getString(ARG_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_profile, container, false);
        TextView name = (TextView)v.findViewById(R.id.profile_name);
        TextView email = (TextView)v.findViewById(R.id.profile_email);
        TextView phone = (TextView)v.findViewById(R.id.profile_phone);
        TextView address = (TextView)v.findViewById(R.id.profile_address);
        TextView city = (TextView)v.findViewById(R.id.profile_city);
        name.setText(mName);
        email.setText(mEmail);
        phone.setText(mPhone);
        address.setText(mAddress);
        city.setText(mCity);
        return v;
    }


}
