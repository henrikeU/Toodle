package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.CalendarEvent;
import com.example.louis_edouard.toodle.moodle.Globals;
import com.example.louis_edouard.toodle.moodle.UserProfile;

import java.io.IOException;

public class HomeDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, AdapterView.OnItemClickListener{
    ListView listViewHome;
    Button btnCoursHome,btnMessHome,btnCalendHome,btnTousHome;
    SharedPreferences preferences;
    HomeAdapter homeAdapter;
    TextView drawer_txt_name;
    TextView drawer_txt_email;
    private UserProfile userProfile;
    private Calendar calendar;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawer);
        //drawer menu

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        /************************/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /***********************/
        header = (View)navigationView.getHeaderView(0);

        // changer le menu
//        Menu m = navigationView.getMenu();
//        m.clear();
//        MenuItem j;
//        j = m.add(R.id.groupePrincipal, 0, 0, "Archives");
//        j.setIcon(android.R.drawable.ic_menu_gallery);
//        j = m.add(R.id.groupePrincipal, 1, 1, "Recherche");
//        j.setIcon(android.R.drawable.ic_menu_search);
//        m.setGroupCheckable(R.id.groupePrincipal, true, true);
        /*********/

        //overflow menu
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        long now = System.currentTimeMillis();
        long last = prefs.getLong("DerniereVisite", 0);

        long delta = (now - last)/1000;

        Toast.makeText(this, "Derniere visite il y a " + delta + " secnodes"
                , Toast.LENGTH_LONG).show();
        //mettre a jour la derniere visite
        SharedPreferences.Editor prefeditor= prefs.edit();
        prefeditor.putLong("derniereVisite",now);
        prefeditor.apply();
        prefeditor.commit();
        /***************/
        preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        RunAPI runAPI = new RunAPI();
        runAPI.execute();
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
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
//            intent = new Intent(this,ContactActivity.class);
//            startActivity(intent);
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
        Intent intent;

        switch (v.getId()) {
            case R.id.btnCoursHome:
                intent = new Intent(this,CoursActivity.class);

                //intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                startActivity(intent);
                break;
            case R.id.btnCalendHome:
                intent = new Intent(this,CalendarActivity.class);
                startActivity(intent);
                //intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                break;
            case R.id.btnMessHome:
                intent = new Intent(this,MessageActivity.class);
                startActivity(intent);
                // intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                break;
            case R.id.btnContactHome:
                // intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,DetailsActivity.class);
        String home = calendar.events.get(position).name;
        intent.putExtra("home",home);
        startActivity(intent);
    }

    private class HomeAdapter extends BaseAdapter {
        LayoutInflater inflaterHome;
        public HomeAdapter() {
            inflaterHome= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return calendar.events.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vHome = convertView;
            if(vHome==null){
                vHome=inflaterHome.inflate(android.R.layout.simple_list_item_1,parent,false);
            }

            TextView text = (TextView)vHome.findViewById(android.R.id.text1);
            text.setText(calendar.events.get(position).name);
            //time.setText(Globals.ConvertDate(message.timecreated));
            Log.d("time_test", calendar.events.get(position).timestart + "");
            CalendarEvent calendarEvent = calendar.events.get(position);
            Globals.EventConvertDate(calendarEvent.timestart);
            return vHome;
        }
    }

    public class RunAPI extends AsyncTask<String, Object, UserProfile> {

        @Override
        protected void onPostExecute(UserProfile userProfile) {
            super.onPostExecute(userProfile);
            homeAdapter = new HomeAdapter();
            listViewHome.setAdapter(homeAdapter);
            setTitle(userProfile.fullname);

            drawer_txt_name = (TextView)header.findViewById(R.id.drawer_txt_name);
            drawer_txt_name.setText(userProfile.fullname);
            drawer_txt_email = (TextView)header.findViewById(R.id.drawer_txt_email);
            //TODO: retrieve user's email address
            drawer_txt_email.setText(userProfile.fullname + "@email.com");
            // saving user's data to shared preferences file
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(Globals.KEY_USER_ID, userProfile.userid);
            editor.apply();
        }

        @Override
        protected UserProfile doInBackground(String... params) {
            WebAPI webAPI = new WebAPI(preferences.getString(Globals.KEY_USER_TOKEN, null));
            try {
                userProfile = webAPI.getUserProfile();
                calendar = webAPI.getEvent();
            }
            catch(IOException e){ }
            return userProfile;
        }
    }
}
