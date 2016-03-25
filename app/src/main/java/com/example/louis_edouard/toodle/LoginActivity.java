package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis_edouard.toodle.moodle.UserProfile;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //ui items
    EditText txtUserKey;
    Button btnLogin;
    TextView txtErrorMsg;
    //variables
    String userKey;
    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUserKey = (EditText)findViewById(R.id.txt_UserKey);
        btnLogin = (Button)findViewById(R.id.btn_CreateProfil);
        txtErrorMsg = (TextView)findViewById(R.id.txt_errorMsg);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        userKey = txtUserKey.getText().toString();
        RunAPI runAPI = new RunAPI();
        runAPI.execute();
    }

    public class RunAPI extends AsyncTask<String, Object, UserProfile>{
        @Override
        protected void onPostExecute(UserProfile userProfile) {
            super.onPostExecute(userProfile);

            if(userProfile.username == null)
                txtErrorMsg.setVisibility(View.VISIBLE);
            else {
                txtErrorMsg.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("USER_TOKEN", userKey);
                intent.putExtra("USER_ID", userProfile.userid);
                intent.putExtra("USER_NAME", userProfile.fullname);
                startActivity(intent);
            }
        }

        @Override
        protected UserProfile doInBackground(String... params) {
            WebAPI webAPI = new WebAPI(userKey);
            try {
                userProfile = webAPI.run();
            }
            catch(IOException e){ }

            return userProfile;
        }
    }
}
