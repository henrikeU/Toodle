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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.Contact;
import com.example.louis_edouard.toodle.moodle.ContactRoot;

import java.io.IOException;

public class ContactActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    View header;
    TextView drawer_txt_name, drawer_txt_email;
    ListView listView;
    ListViewAdapter adapter;
    SharedPreferences preferences;
    DBHelper dbHelper;
    private ContactRoot root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        dbHelper = new DBHelper(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        listView = (ListView) findViewById(R.id.listViewContact);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = (View)navigationView.getHeaderView(0);
        drawer_txt_name = (TextView)header.findViewById(R.id.drawer_txt_name);
        drawer_txt_name.setText(HomeActivity.userFullName);
        drawer_txt_email = (TextView)header.findViewById(R.id.drawer_txt_email);

        RunAPI runAPI = new RunAPI();
        runAPI.execute();
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
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            Cursor c = getCursor();
            final LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(layout, parent, false);

            return v;
        }
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
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
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
            intent = new Intent(this,CalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_message) {
            intent = new Intent(this,MessageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            onBackPressed();
        } else if (id == R.id.nav_send) {
            intent = new Intent(this, SendMessageActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class RunAPI extends AsyncTask<String, Object, ContactRoot> {

        @Override
        protected void onPostExecute(ContactRoot contactRoot) {
            super.onPostExecute(contactRoot);

            drawer_txt_name = (TextView)header.findViewById(R.id.drawer_txt_name);
            drawer_txt_name.setText(HomeActivity.userFullName);

            Cursor c  = dbHelper.getAllContacts();

            String[] from = { DBHelper.KEY_ID, DBHelper.CONTACT_FULLNAME };
            int[] to = { 0, android.R.id.text1 };
            adapter = new ListViewAdapter(ContactActivity.this, android.R.layout.simple_list_item_1, c, from, to, 0);
            listView.setAdapter(adapter);
        }

        @Override
        protected ContactRoot doInBackground(String... params) {
            WebAPI webAPI = new WebAPI(ContactActivity.this, preferences.getString(Globals.KEY_USER_TOKEN, null));
            try {
                root = webAPI.getContacts();
            }
            catch(IOException e){ }

            return root;
        }
    }
}
