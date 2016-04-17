package com.example.louis_edouard.toodle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.CalendarEvent;
import com.example.louis_edouard.toodle.moodle.Globals;
import com.example.louis_edouard.toodle.moodle.UserProfile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import android.os.Handler;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, AdapterView.OnItemClickListener{
    ListView listViewHome;
    Button btnCoursHome,btnMessHome,btnCalendHome,btnTousHome;
    TextView drawer_txt_name, drawer_txt_email;
    View header;
    private SharedPreferences preferences;
    private ListViewAdapter listViewAdapter;
    private UserProfile userProfile;
    private Calendar calendar;
    public static String userFullName, userName;
    DBHelper dbHelper;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        token = preferences.getString(Globals.KEY_USER_TOKEN, null);
        String userName = preferences.getString(Globals.KEY_USER_USERNAME, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);

        // drawer navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = (View)navigationView.getHeaderView(0);
        drawer_txt_name = (TextView)header.findViewById(R.id.drawer_txt_name);
        drawer_txt_email = (TextView)header.findViewById(R.id.drawer_txt_email);

        //overflow menu
        long now = System.currentTimeMillis();
        long last = preferences.getLong(Globals.KEY_LAST_CONNECTION, 0);
        long delta = (now - last)/1000;
        //mettre a jour la derniere visite
        SharedPreferences.Editor prefEditor = preferences.edit();;
        prefEditor.putLong(Globals.KEY_LAST_CONNECTION, now);
        prefEditor.apply();

        RunAPI runAPI = new RunAPI();
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                if(Globals.IsConnected(HomeActivity.this))
                    new UpdateTask().execute();

                handler.postDelayed(this, Globals.REFRESH_TIME);
            }
        };

        if(Globals.IsConnected(this)) {
            runAPI.execute();
            handler.postDelayed(r, Globals.REFRESH_TIME);
        }

        listViewHome = (ListView)findViewById(R.id.listViewHome);
        btnCoursHome = (Button)findViewById(R.id.btnCoursHome);
        btnMessHome = (Button)findViewById(R.id.btnMessHome);
        btnCalendHome = (Button)findViewById(R.id.btnCalendHome);
        btnTousHome = (Button)findViewById(R.id.btnContactHome);
        btnCoursHome.setOnClickListener(this);
        btnMessHome.setOnClickListener(this);
        btnCalendHome.setOnClickListener(this);
        btnTousHome.setOnClickListener(this);
        listViewHome.setOnItemClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.preference) {
            Intent intent = new Intent(this,Preference.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_home) {
            onBackPressed();
        } else if (id == R.id.nav_course) {
            intent = new Intent(this,CoursActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(this,CalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_message) {
            intent = new Intent(this,MessageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            intent = new Intent(this,ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            intent = new Intent(this, SendMessageActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.btnCoursHome:
                intent.setClass(this,CoursActivity.class);
                break;
            case R.id.btnCalendHome:
                intent.setClass(this,CalendarActivity.class);
                break;
            case R.id.btnMessHome:
                intent.setClass(this,MessageActivity.class);
                break;
            case R.id.btnContactHome:
                intent.setClass(this,ContactActivity.class);
                break;
        }

        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,DetailsActivity.class);
        String home = calendar.events.get(position).name;
        intent.putExtra("home",home);
        startActivity(intent);
    }

    private class ListViewAdapter extends SimpleCursorAdapter {
        LayoutInflater layoutInflater;
        Context context;
        private int layout;

        protected ListViewAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            this.context = context;
            this.layout = layout;
            layoutInflater= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);

            long unixSeconds = cursor.getInt(cursor.getColumnIndex(DBHelper.EVENT_TIMESTART));
            String strEventTime = Globals.EventConvertDate(unixSeconds);
            TextView eventTime = (TextView) view.findViewById(R.id.listitem_event_time);
            eventTime.setText(strEventTime);

            String html = cursor.getString(cursor.getColumnIndex(DBHelper.EVENT_DESCRIPTION));
            String streventDescription  = Globals.HtmlToText(html);
            TextView eventDescription = (TextView) view.findViewById(R.id.listitem_event_description);
            if(streventDescription.length() > 0)
                eventDescription.setText(streventDescription);
            else
                eventDescription.setVisibility(View.GONE);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            Cursor c = getCursor();
            final LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(layout, parent, false);

            return v;
        }
    }

    private class UpdateTask extends AsyncTask<String, Object, Calendar> {

        @Override
        protected void onPostExecute(Calendar calendar) {
            super.onPostExecute(calendar);
            Cursor c  = dbHelper.getAllFutureEvents();
            listViewAdapter.changeCursor(c);
            listViewAdapter.notifyDataSetChanged();
        }

        @Override
        protected Calendar doInBackground(String... params) {
            WebAPI webAPI = new WebAPI(HomeActivity.this, token);
            try {
                calendar = webAPI.getEvent(userProfile.userid);
                webAPI.updateCours(userProfile.userid);
            }
            catch(IOException e){ }

            return calendar;
        }
    }

    public class RunAPI extends AsyncTask<String, Object, UserProfile> {

        @Override
        protected void onPostExecute(UserProfile userProfile) {
            super.onPostExecute(userProfile);
            userFullName = userProfile.fullname;
            setTitle(userFullName);

            Cursor c  = dbHelper.getAllFutureEvents();

            String[] from = {DBHelper.KEY_ID, DBHelper.EVENT_NAME, DBHelper.EVENT_DESCRIPTION, DBHelper.EVENT_TIMESTART};
            int[] to = {0, R.id.listitem_event_name, R.id.listitem_event_description, R.id.listitem_event_time };
            listViewAdapter = new ListViewAdapter(HomeActivity.this, R.layout.listview_home, c, from, to, 0);
            listViewHome.setAdapter(listViewAdapter);

            drawer_txt_name.setText(userFullName);
            drawer_txt_email.setText(userName);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(Globals.KEY_USER_ID, userProfile.userid);
            editor.apply();
        }

        @Override
        protected UserProfile doInBackground(String... params) {
            WebAPI webAPI = new WebAPI(HomeActivity.this, token);
            try {
                userProfile = webAPI.getUserProfile();
                calendar = webAPI.getEvent(userProfile.userid);
                webAPI.updateCours(userProfile.userid);
            }
            catch(IOException e){ }

            return userProfile;
        }
    }
}
