package com.example.louis_edouard.toodle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class CalendarEventActivity extends AppCompatActivity {
    TextView date, hours, location,remind, subject, description,disponible, participant;
    Button ls, invite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_event);

        date = (TextView)findViewById(R.id.txt_clndr_public_date);
        hours = (TextView)findViewById(R.id.txt_clndr_public_hours);
        location = (TextView)findViewById(R.id.txt_clndr_public_local);
        remind = (TextView)findViewById(R.id.txt_clndr_public_remind);
        subject = (TextView)findViewById(R.id.txt_clndr_public_subject);
        description= (TextView)findViewById(R.id.txt_clndr_public_description);
        disponible= (TextView)findViewById(R.id.txt_clndr_public_dispo);
        participant= (TextView)findViewById(R.id.txt_clndr_public_participant);
        ls= (Button)findViewById(R.id.btn_clndr_public_list);
        invite= (Button)findViewById(R.id.btn_clndr_public_invite);
    }
}
