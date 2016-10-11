package com.telematica.travelmate.userinterface.signup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.telematica.travelmate.R;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;

public class SignupActivity extends AppCompatActivity {
    EditText newName;
    EditText newEmail;
    EditText newPassword;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        newName = (EditText) findViewById(R.id.new_name);
        newEmail = (EditText) findViewById(R.id.new_email);
        newPassword = (EditText) findViewById(R.id.password);
        signupButton = (Button) findViewById(R.id.signup_button);
    }

    /** Called when the user clicks the Sign up button */
    public void doSignUp(View view) {
        // TODO: Handle signup.
        Intent intent = new Intent(getApplicationContext(), EntryListActivity.class);
        startActivity(intent);
    }
}
