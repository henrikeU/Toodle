package com.example.louis_edouard.toodle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.Globals;
import com.example.louis_edouard.toodle.moodle.Token;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //widgets
    EditText txtUserName, txtPassword;
    Button btnLogin;
    TextView txtErrorMsg;
    //variables
    private Token token;
    private String username, password, old_username, old_password;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        old_username = preferences.getString(Globals.KEY_USER_USERNAME, null);
        old_password = preferences.getString(Globals.KEY_USER_PASSWORD, null);

        txtUserName = (EditText)findViewById(R.id.txt_UserKey);
        txtPassword = (EditText)findViewById(R.id.txt_Password);
        btnLogin = (Button)findViewById(R.id.btn_CreateProfil);
        txtErrorMsg = (TextView)findViewById(R.id.txt_errorMsg);

        txtUserName.setText(old_username);
        txtPassword.setText(old_password);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        username = txtUserName.getText().toString();
        password = txtPassword.getText().toString();

        if(Globals.IsConnected(this)) {
            RunAPI runAPI = new RunAPI();
            runAPI.execute();
        }
        else if(username == old_username && password == old_password) {
            Intent intent = new Intent(LoginActivity.this, HomeDrawerActivity.class);
            startActivity(intent);
        }
        else {
            txtErrorMsg.setVisibility(View.VISIBLE);
            txtErrorMsg.setText(getResources().getText(R.string.error_connectivity));
        }
    }

    public class RunAPI extends AsyncTask<String, Object, Token>{
        @Override
        protected void onPostExecute(Token token) {
            super.onPostExecute(token);
            if (token == null) {
                txtErrorMsg.setVisibility(View.VISIBLE);
                txtErrorMsg.setText(getResources().getText(R.string.error_api));
            }
            else if(token.token == null) {
                txtErrorMsg.setVisibility(View.VISIBLE);
                txtErrorMsg.setText(getResources().getText(R.string.error_login));
            }
            else {
                txtErrorMsg.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LoginActivity.this, HomeDrawerActivity.class);
                // saving user's data to shared preferences file
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Globals.KEY_USER_TOKEN, token.token);
                editor.putString(Globals.KEY_USER_USERNAME, username);
                editor.putString(Globals.KEY_USER_PASSWORD, password);
                editor.apply();

                startActivity(intent);
            }
        }

        @Override
        protected Token doInBackground(String... params) {
            WebAPI webAPI = new WebAPI();

            try { token = webAPI.getToken(username, password); }
            catch(IOException e){ }

            return token;
        }
    }
}
