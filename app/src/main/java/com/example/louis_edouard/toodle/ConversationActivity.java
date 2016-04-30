package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.Message;
import com.example.louis_edouard.toodle.moodle.RootMessage;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listViewConversation;
    EditText editText;
    RootMessage rootMessage;
    Button btnSend;
    int userid,  useridTo;
    ConversationAdaptor conversationAdaptor;
    SharedPreferences pref;
    boolean canGoBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setTitle("Conversation");
        pref= getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        userid = pref.getInt(Globals.KEY_USER_ID, 0);

        Intent intent = getIntent();
        useridTo  = intent.getIntExtra("USER_ID_TO", 0);
        canGoBack = intent.getBooleanExtra("BACK", true);
        listViewConversation = (ListView)findViewById(R.id.listView_Conversation);
        editText = (EditText)findViewById(R.id.editText_Message);
        btnSend = (Button)findViewById(R.id.btn_send);

        btnSend.setOnClickListener(this);

        RunAPI runAPI = new RunAPI();

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                if(Globals.IsConnected(ConversationActivity.this))
                    new UpdateTask().execute();

                handler.postDelayed(this, Globals.CONVERSATION_REFRESH_TIME);
            }
        };

        if(Globals.IsConnected(this)) {
            runAPI.execute();
            handler.postDelayed(r, Globals.CONVERSATION_REFRESH_TIME);
        }

    }

    @Override
    public void onClick(View v) {
        WebAPI web = new WebAPI(pref.getString(Globals.KEY_USER_TOKEN, null));
        try {
            web.postMessage(useridTo, editText.getText().toString());
            editText.setText("");
            UpdateTask updateTask = new UpdateTask();
            updateTask.execute();
        }
        catch(IOException e){ e.printStackTrace(); }
    }

    private class ConversationAdaptor extends BaseAdapter {
        LayoutInflater inflater;
        public ConversationAdaptor() {
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

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
                v = inflater.inflate(R.layout.listitem_conv,parent,false);
            }
            //pour changer le color au fond des photos

            //String title = fullname.LineupItems.get(position).Title;
            Message message = rootMessage.messages.get(position);
            TextView txtMessage = (TextView)v.findViewById(R.id.textView_message);
            TextView timeSent = (TextView)v.findViewById(R.id.textView_timeSent);
            LinearLayout layout = (LinearLayout)v.findViewById(R.id.chatbubble_container);
            LinearLayout chatbuble = (LinearLayout)v.findViewById(R.id.chatbubble);
            timeSent.setText(Globals.ConvertDate(message.timecreated));
            txtMessage.setText(message.text);

            if(message.useridfrom == userid) {
                chatbuble.setBackgroundDrawable(getResources().getDrawable(R.drawable.chatbubble_outgoing));
                layout.setPadding(40, 0, 0, 0);
            }
            else{
                chatbuble.setBackgroundDrawable(getResources().getDrawable(R.drawable.chatbubble_incoming));
                layout.setPadding(0, 0, 40, 0);
            }

            return v;
        }
    }

    @Override
    public void onBackPressed() {
        if(canGoBack)
            super.onBackPressed();
        else {
            Intent i= new Intent(this, MessageActivity.class);
            startActivity(i);
        }
    }

    private class UpdateTask extends AsyncTask<String, Object, RootMessage> {

        @Override
        protected void onPostExecute(RootMessage message) {
            super.onPostExecute(message);
            conversationAdaptor.notifyDataSetChanged();
            listViewConversation.setSelection(conversationAdaptor.getCount() - 1);
        }

        @Override
        protected RootMessage doInBackground(String... params) {
            WebAPI web = new WebAPI(pref.getString(Globals.KEY_USER_TOKEN, null));

            try {
                rootMessage = web.getMessages(userid, useridTo);
                Comparator<Message> comparator = new Comparator<Message>() {
                    @Override
                    public int compare(Message lhs, Message rhs) {
                        return lhs.timecreated - rhs.timecreated;
                    }
                };
                Collections.sort(rootMessage.messages, comparator);
            }
            catch(IOException e){ }

            return rootMessage;
        }
    }

    private class RunAPI extends AsyncTask<String, Object, RootMessage> {
        @Override
        protected RootMessage doInBackground(String... params) {

            WebAPI web = new WebAPI(pref.getString(Globals.KEY_USER_TOKEN, null));

            try {
                rootMessage = web.getMessages(userid, useridTo);
                Comparator<Message> comparator = new Comparator<Message>() {
                    @Override
                    public int compare(Message lhs, Message rhs) {
                        return lhs.timecreated - rhs.timecreated;
                    }
                };
                Collections.sort(rootMessage.messages, comparator);
            }
            catch(IOException e){ }

            return rootMessage;
        }

        @Override
        protected void onPostExecute(RootMessage message){
            super.onPostExecute(message);
            conversationAdaptor = new ConversationAdaptor();
            listViewConversation.setAdapter(conversationAdaptor);
            listViewConversation.setSelection(conversationAdaptor.getCount() - 1);
        }
    }
}
