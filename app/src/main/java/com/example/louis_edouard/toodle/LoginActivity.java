package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.Globals;
import com.example.louis_edouard.toodle.moodle.UserProfile;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //ui items
    EditText txtUserKey;
    Button btnLogin;
    TextView txtErrorMsg;
    //variables
    public static String userToken;
    UserProfile userProfile;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        String token = preferences.getString(Globals.KEY_USER_TOKEN, null);
        txtUserKey = (EditText)findViewById(R.id.txt_UserKey);
        txtUserKey.setText(token);
        btnLogin = (Button)findViewById(R.id.btn_CreateProfil);
        txtErrorMsg = (TextView)findViewById(R.id.txt_errorMsg);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        userToken = txtUserKey.getText().toString();
        RunAPI runAPI = new RunAPI();
        runAPI.execute();
    }

    public class RunAPI extends AsyncTask<String, Object, UserProfile>{
        @Override
        protected void onPostExecute(UserProfile userProfile) {
            super.onPostExecute(userProfile);

            if(userProfile.username == null) {
                txtErrorMsg.setVisibility(View.VISIBLE);

            }else {
                txtErrorMsg.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                // saving user's data to shared preferences file
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Globals.KEY_USER_TOKEN, userToken);
                editor.putInt(Globals.KEY_USER_ID, userProfile.userid);
                editor.putString(Globals.KEY_USER_NAME, userProfile.fullname);
                editor.apply();

                startActivity(intent);
            }
        }

        @Override
        protected UserProfile doInBackground(String... params) {
            WebAPI webAPI = new WebAPI(userToken);
            try {
                userProfile = webAPI.run();
            }
            catch(IOException e){ }

            return userProfile;
        }
    }
}
