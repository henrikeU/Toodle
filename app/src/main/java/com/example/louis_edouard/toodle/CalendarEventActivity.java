package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CalendarEventActivity extends AppCompatActivity {

    public static final String ARG_NAME = "name";
    public static final String ARG_DESCRIPTION = "description";
    public static final String ARG_DATE = "date";
    public static final String ARG_TIMESTART = "timeStart";
    public static final String ARG_TIMEEND = "timeEnd";
    public static final String ARG_LOCATION = "location";
    TextView txtEventTitle, txtEventDescription, txtEventDate, txtEventTime, txtEventLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_event);

        Intent intent = getIntent();

        String eventTitle =  intent.getStringExtra(ARG_NAME);
        String eventDescription = intent.getStringExtra(ARG_DESCRIPTION);
        String strDate = intent.getStringExtra(ARG_DATE);
        String eventDate = Globals.toLongDateString(this, strDate);
        String strTimeStart = intent.getStringExtra(ARG_TIMESTART);
        String strTimeEnd = intent.getStringExtra(ARG_TIMEEND);
        String eventTime = strTimeStart.equals(strTimeEnd) ? strTimeStart : strTimeStart + " - " + strTimeEnd;
        txtEventTitle = (TextView)findViewById(R.id.event_title);
        txtEventDescription = (TextView)findViewById(R.id.event_description);
        txtEventDate = (TextView)findViewById(R.id.event_date);
        txtEventTime = (TextView)findViewById(R.id.event_time);
        txtEventLocation = (TextView)findViewById(R.id.event_location);
        txtEventTitle.setText(eventTitle);
        if(eventDescription.length() > 0)
            txtEventDescription.setText(Globals.HtmlToText(eventDescription));
        else
            txtEventDescription.setText("N/A");
        txtEventDate.setText(eventDate);
        txtEventTime.setText(eventTime);

        setTitle("Évènement: " + eventTitle);

    }
}
