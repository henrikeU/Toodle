package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = (TextView)findViewById(R.id.name);

        Intent intent = getIntent();

        String s =intent.getStringExtra("name");

        name.setText(s);
    }
}
