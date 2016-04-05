package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.example.louis_edouard.toodle.moodle.EnrolledCourse;
import com.example.louis_edouard.toodle.moodle.Globals;
import com.example.louis_edouard.toodle.moodle.Message;
import com.example.louis_edouard.toodle.moodle.RootMessage;
import com.example.louis_edouard.toodle.moodle.UserProfile;
import com.example.louis_edouard.toodle.moodle.UserProfileSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lvMessage;
    private MessageAdaptor messageAdaptor;
    private RootMessage rootMessage;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        RunAPI runAPI = new RunAPI();
        runAPI.execute();


        SharedPreferences pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        userId = pref.getInt(Globals.KEY_USER_ID, 0);

        lvMessage = (ListView)findViewById(R.id.lvMessage);
        lvMessage.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,SendMessageActivity.class);
        startActivity(intent);
        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("USER_ID_FROM", rootMessage.messages.get(position).useridfrom);
        intent.putExtra("USER_ID_TO",rootMessage.messages.get(position).useridto);
        startActivity(intent);
    }


    private class MessageAdaptor extends BaseAdapter {
        LayoutInflater inflater;
        public MessageAdaptor() {
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            setTitle("Messagerie");
        }

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
            if (v == null) {
                v = inflater.inflate(R.layout.listview_message, parent, false); // pour recuperer un layout et le mettre dans un view
            }

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
    private class RunAPI extends AsyncTask<String, Object, RootMessage> {
        @Override
        protected RootMessage doInBackground(String... params) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            WebAPI web = new WebAPI(pref.getString(Globals.KEY_USER_TOKEN, null));

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
            messageAdaptor = new MessageAdaptor();
            lvMessage.setAdapter(messageAdaptor);
        }
    }
}
