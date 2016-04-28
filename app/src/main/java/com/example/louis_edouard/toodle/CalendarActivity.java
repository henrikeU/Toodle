package com.example.louis_edouard.toodle;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.CalendarEvent;
import com.example.louis_edouard.toodle.moodle.EventWeek;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ExpandableListView.OnChildClickListener {
    ExpandableListView lvCalendar;
    Calendar calendar;
    TextView drawer_txt_name;
    CalendarAdapter listAdapter;
    View header;
    private ActionMode mActiveActionMode;
    private ActionMode.Callback mLastCallback;
    private boolean mInActionMode;
    private EventWeek events;
    private SharedPreferences preferences;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        dbHelper = new DBHelper(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*******/
        lvCalendar = (ExpandableListView)findViewById(R.id.expandableListView_calendar);
        lvCalendar.setOnChildClickListener(this);
        //lvCalendar.setonlon

        header = (View)navigationView.getHeaderView(0);
        RunAPI runAPI = new RunAPI();
        runAPI.execute();
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
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_event) {
            Intent intent = new Intent(this, CalendarAddEventActivity.class);
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
            intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_course) {
            intent = new Intent(this,CoursActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_calendar) {
            onBackPressed();
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
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(CalendarActivity.this, CalendarEventActivity.class);
        startActivity(intent);
        return true;
    }

    private class CalendarAdapter extends SimpleCursorTreeAdapter {
        private Context mContext;
        private LayoutInflater layoutInflater;
        private int groupLayout, childLayout;

        public CalendarAdapter(Context context, Cursor cursor, int groupLayout, String[] groupFrom,
                               int[] groupTo, int childLayout, String[] childFrom, int[] childTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo, childLayout, childFrom, childTo);
            this.mContext = context;
            this.layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            this.groupLayout = groupLayout;
            this.childLayout = childLayout;
        }

        @Override
        protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
            super.bindChildView(view, context, cursor, isLastChild);
            TextView text = (TextView)view.findViewById(R.id.txt_event_hours);
            String eventStart = cursor.getString(cursor.getColumnIndex("eventStart"));
            String eventEnd = cursor.getString(cursor.getColumnIndex("eventEnd"));
            String formattedTime =  eventStart.equals(eventEnd) ? eventStart : eventStart + " - " + eventEnd;
            text.setText(formattedTime);
            TextView txtEventDescription = (TextView)view.findViewById(R.id.txt_event_description);
            String eventDescription = cursor.getString(cursor.getColumnIndex(DBHelper.EVENT_DESCRIPTION));
            String streventDescription = Globals.HtmlToText(eventDescription);
            if(streventDescription.length() > 0) {
                txtEventDescription.setText(streventDescription);
                txtEventDescription.setVisibility(View.VISIBLE);
            }
            else
                txtEventDescription.setVisibility(View.GONE);
        }

        @Override
        protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
            super.bindGroupView(view, context, cursor, isExpanded);
            String weekDay = "";
            String strDate = cursor.getString(cursor.getColumnIndex("testing"));
            String[] tabDate  = strDate.split("-");
            String month = getResources().getStringArray(R.array.months)[Integer.parseInt(tabDate[1])];
            String day = tabDate[2];
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
            Date date;
            try {
                date = dateFormat.parse(strDate);
                weekDay = getResources().getStringArray(R.array.weekDays)[Globals.dayOfWeek(date) - 1];
            }catch (ParseException e){
                e.printStackTrace();
            }
            String formattedDate = weekDay + " " + day + " " + month.toLowerCase();
            TextView textView = (TextView) view.findViewById(R.id.list_calendar_groupHeader);
            textView.setText(formattedDate);

        }

        @Override
        public View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
            Cursor c = getCursor();
            final LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(childLayout, parent, false);

            return v;
        }

        @Override
        public View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
            Cursor c = getCursor();
            final LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(groupLayout, parent, false);

            return v;
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            String day = groupCursor.getString(groupCursor.getColumnIndex("testing"));
            Cursor c = dbHelper.getEventsByMonth(day);
            return c;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private class RunAPI extends AsyncTask<String, Object, Calendar> {

        @Override
        protected void onPostExecute(Calendar calendar) {
            super.onPostExecute(calendar);
            Cursor c  = dbHelper.getEventsByMonth();
            String[] groupFrom = {DBHelper.KEY_ID, "testing" };
            int[] groupTo = { 0, R.id.list_calendar_groupHeader };
            String[] childFrom = {DBHelper.KEY_ID, "eventStart", DBHelper.EVENT_NAME, DBHelper.EVENT_DESCRIPTION };
            int[] childTo = {0, R.id.txt_event_hours, R.id.txt_event_title, R.id.txt_event_description };

            listAdapter = new CalendarAdapter(CalendarActivity.this, c, R.layout.list_group_calendar, groupFrom, groupTo,
                    R.layout.list_item_calendar, childFrom, childTo);
            lvCalendar.setAdapter(listAdapter);

            for(int i = 0; i < listAdapter.getGroupCount(); i++)
                lvCalendar.expandGroup(i);

            drawer_txt_name = (TextView)header.findViewById(R.id.drawer_txt_name);
            drawer_txt_name.setText(HomeActivity.userFullName);

        }

        @Override
        protected Calendar doInBackground(String... params) {

            WebAPI webAPI = new WebAPI(CalendarActivity.this, preferences.getString(Globals.KEY_USER_TOKEN, null));
            try {
                calendar = webAPI.getEvent(preferences.getInt(Globals.KEY_USER_ID, 0));
            }
            catch(IOException e){ }

            return calendar;
        }
    }
}
