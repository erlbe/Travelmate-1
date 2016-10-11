package com.telematica.travelmate.userinterface.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.telematica.travelmate.R;
import com.telematica.travelmate.connection.HttpServerConnection;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;
import com.telematica.travelmate.userinterface.signup.SignupActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button logIn;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        logIn = (Button) findViewById(R.id.login_button);
        signUp = (Button) findViewById(R.id.signup_button);
    }

    /** Called when the user clicks the Log in button */
    public void logIn(View view) {
        final String inputEmail = email.getText().toString();
        final String inputPassword = password.getText().toString();

        // TODO: Handle login
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(Void... voids) {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("email", inputEmail);
                    jsonParam.put("password", inputPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String result = new HttpServerConnection().connectToServer("http://192.168.10.2:8080/login", 15000, "POST", jsonParam);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result != null){
                    System.out.println(result);

                    // Go to EntryListActivity
                    Intent intent = new Intent(getApplicationContext(), EntryListActivity.class);
                    startActivity(intent);

                }
            }
        };
        task.execute();

    }

    /** Called when the user clicks the Sign up button
     *  Goes to signup activity */
    public void signUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }
}
