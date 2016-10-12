package com.telematica.travelmate.userinterface.signup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.telematica.travelmate.R;
import com.telematica.travelmate.connection.HttpServerConnection;
import com.telematica.travelmate.model.User;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.telematica.travelmate.userinterface.login.LoginActivity.parseUser;

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
        newPassword = (EditText) findViewById(R.id.new_password);
        signupButton = (Button) findViewById(R.id.doSignUpButton);
    }

    /** Called when the user clicks the Sign up button */
    public void doSignUp(View view) {
        // TODO: Handle signup.

        final String inputName = newName.getText().toString();
        final String inputEmail = newEmail.getText().toString();
        final String inputPassword = newPassword.getText().toString();

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(Void... voids) {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("name", inputName);
                    jsonParam.put("email", inputEmail);
                    jsonParam.put("password", inputPassword);
                    System.out.println(jsonParam);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String result = new HttpServerConnection().connectToServer("http://192.168.10.2:8080/api/signup", 15000, "POST", jsonParam);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO: Handle not available email etc.
                if(result != null){
                    System.out.println("JSON:");
                    System.out.println(result);

                    // TODO: Store this user somewhere to be available across the application
                    User loggedUser = parseUser(result);

                    // Go to EntryListActivity
                    Intent intent = new Intent(getApplicationContext(), EntryListActivity.class);
                    startActivity(intent);
                }
            }
        };
        task.execute();
    }
}
