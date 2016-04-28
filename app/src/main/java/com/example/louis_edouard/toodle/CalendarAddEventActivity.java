package com.example.louis_edouard.toodle;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

public class CalendarAddEventActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDatePicker, btnTimePicker, btnAddEvent;
    TextView txtDateTime;
    EditText etxtTitle, etxtDescription;
    long timeStart;
    Calendar cal;
    String mDate, mTime;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_add_event);
        preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        btnDatePicker = (Button)findViewById(R.id.btnDatePicker);
        btnTimePicker = (Button)findViewById(R.id.btnTimePicker);
        btnAddEvent = (Button)findViewById(R.id.add_event_btnAdd);
        etxtTitle = (EditText)findViewById(R.id.add_event_title);
        etxtDescription =(EditText)findViewById(R.id.add_event_description);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnAddEvent.setOnClickListener(this);
        txtDateTime = (TextView)findViewById(R.id.event_DateTime);
    }

    @Override
    public void onClick(View v) {
        cal = Calendar.getInstance();
        if(v == btnDatePicker) {
            int dom = cal.get(Calendar.DAY_OF_MONTH);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(CalendarAddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mDate = String.format("%d-%02d-%02d", year, monthOfYear, dayOfMonth);
                    if(mTime == null || mTime.length() < 3)
                        txtDateTime.setText("Veuillez choisir l'heure");
                    else
                        txtDateTime.setText(mDate + " " + mTime);
                }
            }, year, month, dom);
            datePickerDialog.show();
        }
        if(v == btnTimePicker) {
            int min = cal.get(Calendar.MINUTE);
            int hour = cal.get(Calendar.HOUR);
            TimePickerDialog timePickerDialog = new TimePickerDialog(CalendarAddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mTime = String.format("%d:%d", hourOfDay, minute);
                    if(mDate == null || mDate.length() < 10)
                        txtDateTime.setText("Veuillez choisir la date");
                    else
                        txtDateTime.setText(mDate + " " + mTime);
                }
            }, hour, min, true);
            timePickerDialog.show();
        }

        if(v == btnAddEvent){
            WebAPI web = new WebAPI(preferences.getString(Globals.KEY_USER_TOKEN, null));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm", Locale.CANADA);
            Date date;

            try {
                date = dateFormat.parse(mDate + " " + mTime);
                timeStart = date.getTime() / 1000;
                Log.d("TIMESTART", timeStart + "");
                web.postEvent(etxtTitle.getText().toString(), etxtDescription.getText().toString(), timeStart, 0);
                etxtDescription.setText("");
                etxtTitle.setText("");
                Toast.makeText(getApplicationContext(), "Votre evenement a été ajouté", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CalendarAddEventActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
            catch(IOException e){ e.printStackTrace();}
            catch (ParseException e) { e.printStackTrace();}
        }
    }
}
