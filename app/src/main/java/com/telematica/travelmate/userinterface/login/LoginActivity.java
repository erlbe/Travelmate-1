package com.telematica.travelmate.userinterface.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.telematica.travelmate.R;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;
import com.telematica.travelmate.userinterface.signup.SignupActivity;

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
        // TODO: Handle login
        Intent intent = new Intent(getApplicationContext(), EntryListActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Sign up button
     *  Goes to signup activity */
    public void signUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }
}
