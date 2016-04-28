package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CalendarEventActivity extends AppCompatActivity {

    public static final String ARG_TITLE = "title";
    public static final String ARG_DESCRIPTION = "description";
    TextView txtEventTitle, txtEventDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_event);

        Intent intent = getIntent();

        String eventTitle =  intent.getStringExtra(ARG_TITLE);
        String eventDescription = intent.getStringExtra(ARG_DESCRIPTION);
        txtEventTitle = (TextView)findViewById(R.id.event_title);
        txtEventDescription = (TextView)findViewById(R.id.event_description);
        txtEventTitle.setText(eventTitle);
        if(eventDescription.length() > 0)
            txtEventDescription.setText(Globals.HtmlToText(eventDescription));
        else
            txtEventDescription.setText("N/A");


    }
}
