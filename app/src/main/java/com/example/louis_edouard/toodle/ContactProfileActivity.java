package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.ContactRoot;
import com.example.louis_edouard.toodle.moodle.UserProfileSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactProfileActivity extends AppCompatActivity {

    ViewPager pager;
    DBHelper dbHelper;
    SharedPreferences preferences;
    List<UserProfileSearch> contacts;
    public static final String ARG_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_contact_profile));

        Intent intent = getIntent();
        int currentPosition = intent.getIntExtra(ARG_POSITION, 0);
        preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        pager = (ViewPager)findViewById(R.id.container);

        contacts = new ArrayList<>();
        dbHelper = new DBHelper(this);
        Cursor c = dbHelper.getAllContacts();

        c.moveToFirst();
        while (!c.isAfterLast()){
            UserProfileSearch user = new UserProfileSearch();
            user.fullname = c.getString(c.getColumnIndex(DBHelper.CONTACT_FULLNAME));
            user.email = c.getString(c.getColumnIndex(DBHelper.CONTACT_EMAIL));
            user.phone1 = c.getString(c.getColumnIndex(DBHelper.CONTACT_PHONE));
            user.phone2 = c.getString(c.getColumnIndex(DBHelper.CONTACT_CELL));
            user.address = c.getString(c.getColumnIndex(DBHelper.CONTACT_ADDRESS));
            user.city = c.getString(c.getColumnIndex(DBHelper.CONTACT_CITY));
            contacts.add(user);

            c.moveToNext();
        }

        DynamicPagerAdapter pagerAdapter = new DynamicPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(currentPosition);
    }


    public class DynamicPagerAdapter extends FragmentStatePagerAdapter {

        public DynamicPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            UserProfileSearch contact = contacts.get(position);
            return ContactProfileFragment.newInstance(contact.fullname, contact.email, contact.phone1, contact.phone2, contact.address, contact.city);
        }

        @Override
        public int getCount() {
            return contacts.size();
        }

    }

}
