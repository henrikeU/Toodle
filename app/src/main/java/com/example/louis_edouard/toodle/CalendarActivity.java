package com.example.louis_edouard.toodle;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.Globals;

import java.io.IOException;

public class CalendarActivity extends AppCompatActivity {

    ListView listView;
    ListAdapter listAdapter;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.listView_Calendar);
        RunAPI runAPI = new RunAPI();
        runAPI.execute();

    }

    private class ListAdapter extends BaseAdapter {
        LayoutInflater inflaterHome;
        public ListAdapter(){
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
            View view = convertView;
            if(view==null){
                view=inflaterHome.inflate(android.R.layout.simple_list_item_1,parent,false);
            }

            TextView text = (TextView)view.findViewById(android.R.id.text1);
            text.setText(calendar.events.get(position).name);
            return view;
        }
    }

    public class RunAPI extends AsyncTask<String, Object, Calendar> {

        @Override
        protected void onPostExecute(Calendar calendar) {
            super.onPostExecute(calendar);
            listAdapter = new ListAdapter();
            listView.setAdapter(listAdapter);
        }

        @Override
        protected Calendar doInBackground(String... params) {
            // recuperer le token de l'utilisateur
            SharedPreferences preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

            WebAPI webAPI = new WebAPI(preferences.getString(Globals.KEY_USER_TOKEN, null));
            try {
                calendar = webAPI.getEvent();
            }
            catch(IOException e){ }

            return calendar;
        }
    }
}
