package com.example.louis_edouard.toodle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SendMessageActivity extends AppCompatActivity {
    EditText destinataire;
    EditText message;
    CheckBox prive;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        destinataire = (EditText)findViewById(R.id.editTxt_send_message_destinataire);
        message = (EditText)findViewById(R.id.editTxt_send_message);
        prive = (CheckBox)findViewById(R.id.checkb_send_message);
        send = (Button)findViewById(R.id.btn_send_message);
    }
}
