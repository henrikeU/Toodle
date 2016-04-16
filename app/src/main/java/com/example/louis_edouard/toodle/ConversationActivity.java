package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.Globals;
import com.example.louis_edouard.toodle.moodle.Message;
import com.example.louis_edouard.toodle.moodle.RootMessage;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

public class ConversationActivity extends AppCompatActivity {

    ListView listViewConversation;
    EditText editText;
    RootMessage rootMessage;
    int userid,  useridFrom, useridTo;
    ConversationAdaptor conversationAdaptor;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setTitle("Conversation");
        pref= getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        userid = pref.getInt(Globals.KEY_USER_ID, 0);

        Intent intent = getIntent();
        useridFrom  = intent.getIntExtra("USER_ID_FROM", 0);
        useridTo  = intent.getIntExtra("USER_ID_TO", 0);

        //Log.d("USER_FROM_FULLNAME", userfullnameFrom);

        listViewConversation = (ListView)findViewById(R.id.listView_Conversation);
        editText = (EditText)findViewById(R.id.editText_Message);

        RunAPI runApi = new RunAPI();
        runApi.execute();

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
            //layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            person.setText(message.useridfrom == userId ? message.usertofullname : message.userfromfullname);
//            description.setText(rootMessage.messages.get(position).text);
//            time.setText(Globals.ConvertDate(message.timecreated));
//            if(message.timeread == 0 && message.useridfrom != userId){
//                description.setTextColor(Color.BLUE);
//                time.setTextColor(Color.DKGRAY);
//                time.setTypeface(null, Typeface.BOLD);
//            }

            return v;
        }
    }

    private class RunAPI extends AsyncTask<String, Object, RootMessage> {
        @Override
        protected RootMessage doInBackground(String... params) {

            WebAPI web = new WebAPI(pref.getString(Globals.KEY_USER_TOKEN, null));

            try {
                rootMessage = web.getMessages(useridFrom, useridTo);
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
        }
    }
}
