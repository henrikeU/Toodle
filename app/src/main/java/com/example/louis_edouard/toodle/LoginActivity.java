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
import com.example.louis_edouard.toodle.moodle.Token;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //ui items
    EditText txtUserName, txtPassword;
    Button btnLogin;
    TextView txtErrorMsg;
    //variables
    private Token token;
    private String username;
    private String password;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        txtUserName = (EditText)findViewById(R.id.txt_UserKey);
        txtPassword = (EditText)findViewById(R.id.txt_Password);
        btnLogin = (Button)findViewById(R.id.btn_CreateProfil);
        txtErrorMsg = (TextView)findViewById(R.id.txt_errorMsg);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //userToken = txtUserKey.getText().toString();
        username = txtUserName.getText().toString();
        password = txtPassword.getText().toString();
        RunAPI runAPI = new RunAPI();
        runAPI.execute();
    }

    public class RunAPI extends AsyncTask<String, Object, Token>{
        @Override
        protected void onPostExecute(Token token) {
            super.onPostExecute(token);

            if(token.token == null) {
                txtErrorMsg.setVisibility(View.VISIBLE);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }else {
                txtErrorMsg.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                // saving user's data to shared preferences file
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Globals.KEY_USER_TOKEN, token.token);
                editor.apply();

                startActivity(intent);
            }
        }

        @Override
        protected Token doInBackground(String... params) {
            WebAPI webAPI = new WebAPI();
            try {
                token = webAPI.getToken(username, password);
            }
            catch(IOException e){ }

            return token;
        }
    }
}
