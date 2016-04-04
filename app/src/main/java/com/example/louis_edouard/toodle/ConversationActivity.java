package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.Globals;
import com.example.louis_edouard.toodle.moodle.Message;
import com.example.louis_edouard.toodle.moodle.RootMessage;

import java.io.IOException;

public class ConversationActivity extends AppCompatActivity {

    ListView listViewConversation;
    EditText editText;
    RootMessage rootMessage;
    int userId;
    ConversationAdaptor conversationAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Intent intent = getIntent();
        int useridFrom = intent.getIntExtra("USER_ID_FROM", 0);
        String userfullnameFrom = intent.getStringExtra("USER_FROM_FULLNAME");
        int useridTo = intent.getIntExtra("USER_ID_TO", 0);
        String userfullnameTo = intent.getStringExtra("USER_TO_FULLNAME");
        //Log.d("USER_FROM_FULLNAME", userfullnameFrom);

        listViewConversation = (ListView)findViewById(R.id.listView_Conversation);
        editText = (EditText)findViewById(R.id.editText_Message);
    }

    private class ConversationAdaptor extends BaseAdapter {
        LayoutInflater inflater;
        public ConversationAdaptor() {
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
            //pour changer le color au fond des photos

            TextView person = (TextView)v.findViewById(R.id.txt_message_person);
            TextView time = (TextView)v.findViewById(R.id.txt_message_time);
            TextView description = (TextView)v.findViewById(R.id.txt_message_description);


            //String title = fullname.LineupItems.get(position).Title;
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
                //Comparator<Message> comparator = new Comparator<Message>() {
                //    @Override
                //    public int compare(Message lhs, Message rhs) {
                //        return lhs.useridfrom - rhs.useridfrom;
                //    }
                //};
                //Collections.sort(rootMessage.messages, comparator);

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
            conversationAdaptor = new ConversationAdaptor();
            listViewConversation.setAdapter(conversationAdaptor);
        }
    }
}
