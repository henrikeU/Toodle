package com.example.louis_edouard.toodle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class CalendarPrivateActivity extends AppCompatActivity {
    TextView date, hours, location, subject, description,disponible, participant;
    Button ls, reminder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_private);

        date = (TextView)findViewById(R.id.txt_clndr_private_date);
        hours = (TextView)findViewById(R.id.txt_clndr_private_hours);
        location = (TextView)findViewById(R.id.txt_clndr_private_local);
        subject = (TextView)findViewById(R.id.txt_clndr_private_subject);
        description= (TextView)findViewById(R.id.txt_clndr_private_description);
        disponible= (TextView)findViewById(R.id.txt_clndr_private_dispo);
        participant= (TextView)findViewById(R.id.txt_clndr_private_participant);
        ls= (Button)findViewById(R.id.btn_clndr_private_list);
        reminder= (Button)findViewById(R.id.btn_clndr_public_invite);
    }
}
