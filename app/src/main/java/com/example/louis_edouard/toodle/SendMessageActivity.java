package com.example.louis_edouard.toodle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.louis_edouard.toodle.moodle.EnrolledUser;
import com.example.louis_edouard.toodle.moodle.UserProfile;
import com.example.louis_edouard.toodle.moodle.UserProfileSearch;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ARG_NAME = "name";
    AutoCompleteTextView destinataire;
    EditText message;
    Button btnSend;
    DBHelper dbHelper;
    SharedPreferences preferences;
    int userIdTo;
    String userFullNameTo;
    WebAPI webAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Intent intent = getIntent();
        String mName = intent.getStringExtra(ARG_NAME);

        userIdTo = 0;
        userFullNameTo = "";
        destinataire = (AutoCompleteTextView)findViewById(R.id.sendMessage_recipient);
        message = (EditText)findViewById(R.id.sendMessage_message);
        btnSend = (Button)findViewById(R.id.sendMessage_btnSend);

        destinataire.setText(mName);
        preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        webAPI = new WebAPI(this, preferences.getString(Globals.KEY_USER_TOKEN, null));
        dbHelper = new DBHelper(this);
        Cursor c  = dbHelper.getAllContacts();
        String[] from = {DBHelper.KEY_ID, DBHelper.CONTACT_FULLNAME};
        int[] to = { 0, android.R.id.text1 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, from, to, 0);
        destinataire.setAdapter(adapter);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return getCursor(constraint);
            }
        });
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(cursor.getColumnIndex(DBHelper.CONTACT_FULLNAME));
            }
        });

        destinataire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userIdTo = (int) id;
                userFullNameTo = destinataire.getText().toString();
            }
        });

        destinataire.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String strDestinataire = destinataire.getText().toString();
                    boolean isUserName = strDestinataire.split(" ").length == 1;
                    try {
                        UserProfileSearch userProfile;
                        if (strDestinataire.contains("@")) {
                            userProfile = webAPI.getUserByEmail(strDestinataire);
                            if (userProfile != null)
                                userIdTo = userProfile.id;
                        } else if (isUserName) {
                            userProfile = webAPI.getUserByUserName(strDestinataire);
                            if (userProfile != null)
                                userIdTo = userProfile.id;
                        } else if (!userFullNameTo.equalsIgnoreCase(strDestinataire)) {
                            List<EnrolledUser> users = webAPI.getUserByCourse(preferences.getInt(Globals.KEY_USER_ID, 0));
                            for (EnrolledUser user:users) {
                                if(user.fullname.equalsIgnoreCase(strDestinataire))
                                {
                                    userIdTo = user.id;
                                    userFullNameTo = user.fullname;
                                    break;
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btnSend.setOnClickListener(this);
    }



    public Cursor getCursor(CharSequence str){
        Cursor c = dbHelper.filterContact(str);

        return c;
    }

    @Override
    public void onClick(View v) {
        if(userIdTo == 0)
        {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(getResources().getString(R.string.alert_title_contact_invalid));
            dialog.setMessage(getResources().getString(R.string.alert_message_contact_invalid));
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        else {
            try {
                webAPI.postMessage(userIdTo, message.getText().toString());
                message.setText("");
                destinataire.setText("");
                Intent intent = new Intent(SendMessageActivity.this, ConversationActivity.class);
                intent.putExtra("USER_ID_TO", userIdTo);
                intent.putExtra("BACK", false);
                startActivity(intent);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
