package com.example.louis_edouard.toodle;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.Message;
import com.example.louis_edouard.toodle.moodle.RootMessage;

import java.io.IOException;


public class MessageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, AdapterView.OnItemClickListener{
    View header;
    FloatingActionButton floatingActionButton;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    SharedPreferences preferences;
    ListView lvMessage;
    private MessageAdapter adapterMessage;
    private RootMessage rootMessage;
    private int userId;
    TextView drawer_txt_name;
    TextView drawer_txt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_drawer);

        preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        userId = preferences.getInt(Globals.KEY_USER_ID, 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = (View)navigationView.getHeaderView(0);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        lvMessage = (ListView)findViewById(R.id.lvMessage);
        lvMessage.setOnItemClickListener(this);

        RunAPI runAPI = new RunAPI();
        runAPI.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
         else
            super.onBackPressed();
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
            intent = new Intent(this,CalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_message) {
            onBackPressed();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ConversationActivity.class);
        Message message =  rootMessage.messages.get(position);
        int userIdTo = message.useridto == userId ? message.useridfrom : message.useridto;
        intent.putExtra("USER_ID_TO", userIdTo);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        intent = new Intent(this, SendMessageActivity.class);
        startActivity(intent);
    }

    private class RunAPI extends AsyncTask<String, Object, RootMessage> {
        @Override
        protected RootMessage doInBackground(String... params) {
            WebAPI web = new WebAPI(preferences.getString(Globals.KEY_USER_TOKEN, null));

            try {
                rootMessage = web.getMessages(userId);

                Message message1, message2;
                for(int i = 0 ; i < rootMessage.messages.size(); i++){
                    message1 = rootMessage.messages.get(i);
                    for (int j = i + 1; j < rootMessage.messages.size(); j++) {
                        message2 = rootMessage.messages.get(j);
                        if ((message1.useridto == message2.useridto && message1.useridfrom == message2.useridfrom) ||
                                (message1.useridto == message2.useridfrom &&  message1.useridfrom == message2.useridto)) {
                            rootMessage.messages.remove(j);
                            j--;
                        }
                    }
                    if(i == rootMessage.messages.size()) break;
                }
            }
            catch(IOException e){ }

            return rootMessage;
        }

        @Override
        protected void onPostExecute(RootMessage message){
            super.onPostExecute(message);
            adapterMessage = new MessageAdapter();
            lvMessage.setAdapter(adapterMessage);

            drawer_txt_name = (TextView)header.findViewById(R.id.drawer_txt_name);
            drawer_txt_name.setText(HomeActivity.userFullName);
            drawer_txt_email = (TextView)header.findViewById(R.id.drawer_txt_email);
            String userName = preferences.getString(Globals.KEY_USER_USERNAME, null);
            drawer_txt_email.setText(userName);
        }
    }

    private class MessageAdapter extends BaseAdapter {
    LayoutInflater inflater;

        public MessageAdapter() { inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE); }

        @Override
        public int getCount() {
            return rootMessage.messages.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null)
                v = inflater.inflate(R.layout.listview_message, parent, false);

            TextView person = (TextView)v.findViewById(R.id.txt_message_person);
            TextView time = (TextView)v.findViewById(R.id.txt_message_time);
            TextView description = (TextView)v.findViewById(R.id.txt_message_description);

            Message message = rootMessage.messages.get(position);
            person.setText(message.useridfrom == userId ? message.usertofullname : message.userfromfullname);
            description.setText(rootMessage.messages.get(position).text);
            time.setText(Globals.ConvertDate(message.timecreated));
            if(message.timeread == 0 && message.useridfrom != userId){
                description.setTextColor(Color.BLUE);
                time.setTextColor(Color.DKGRAY);
                time.setTypeface(null, Typeface.BOLD);
            }

            return v;
        }
    }
}
