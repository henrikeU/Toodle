package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.CalendarEvent;
import com.example.louis_edouard.toodle.moodle.Globals;
import com.example.louis_edouard.toodle.moodle.UserProfile;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView listViewHome;
    Button btnCoursHome,btnMessHome,btnCalendHome,btnTousHome;
    SharedPreferences preferences;
    HomeAdapter homeAdapter;
    private UserProfile userProfile;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        RunAPI runAPI = new RunAPI();
        runAPI.execute();
        listViewHome = (ListView)findViewById(R.id.listViewHome);

        btnCoursHome = (Button)findViewById(R.id.btnCoursHome);
        btnMessHome = (Button)findViewById(R.id.btnMessHome);
        btnCalendHome = (Button)findViewById(R.id.btnCalendHome);
        btnTousHome = (Button)findViewById(R.id.btnTousHome);
        btnCoursHome.setOnClickListener(this);
        btnMessHome.setOnClickListener(this);
        btnCalendHome.setOnClickListener(this);
        btnTousHome.setOnClickListener(this);



        listViewHome.setOnItemClickListener(this);
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
            case R.id.btnTousHome:
                //intent = new Intent(this,AllActivity.class);
                //startActivity(intent);
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
            Log.d("time_test", calendar.events.get(position).timestart+"");
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
