package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.Message;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
private ListView lvMessage;
private MessageAdaptor messageAdaptor;
    Message fullname;
   public static String people []={"Sebastian Roy", "Rania", "Louis-Eduard", "Janvier",
           "Sebastian Roy", "Rania", "Louis-Eduard", "Janvier",
           "Sebastian Roy", "Rania", "Louis-Eduard", "Janvier",
           "Sebastian Roy", "Rania", "Louis-Eduard", "Janvier"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        lvMessage = (ListView)findViewById(R.id.lvMessage);
        //messageAdaptor = new MessageAdaptor(this); context??!!
        messageAdaptor = new MessageAdaptor();
        lvMessage.setAdapter(messageAdaptor);



    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,SendMessageActivity.class);
        startActivity(intent);
        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    private class MessageAdaptor extends BaseAdapter {
        LayoutInflater inflater;
        public MessageAdaptor() {
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            setTitle("Messagerie");
        }

        @Override
        public int getCount() {
            return people.length;
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

            person.setText(people[position]);

            return v;
        }
    }
}
