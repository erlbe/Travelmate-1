package com.telematica.travelmate.userinterface.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.telematica.travelmate.R;
import com.telematica.travelmate.connection.HttpServerConnection;
import com.telematica.travelmate.model.User;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;
import com.telematica.travelmate.userinterface.signup.SignupActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.telematica.travelmate.utilities.Constants.SERVER_LINK;

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

        // TODO: Skip log-in if user is already logged in
    }

    /** Called when the user clicks the Log in button */
    public void logIn(View view) {
        final String inputEmail = email.getText().toString();
        final String inputPassword = password.getText().toString();

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
                    System.out.println(jsonParam);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String result = new HttpServerConnection().connectToServer(SERVER_LINK +  "/login", 15000, "POST", jsonParam);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                //TODO: Handle wrong username/password
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

    /** Called when the user clicks the Sign up button
     *  Goes to signup activity */
    public void signUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

    public static User parseUser(String result){
        try {
            JSONObject jObject = new JSONObject(result);
            String jId = jObject.getString("_id");
            String jName = jObject.getString("name");

            JSONObject jLocal = jObject.getJSONObject("local");
            String jEmail = jLocal.getString("email");

            JSONArray jsonEntries = jObject.getJSONArray("entries");
            List<String> jEntries = new ArrayList<String>();
            for (int i=0; i<jsonEntries.length(); i++) {
                jEntries.add( jsonEntries.getString(i) );
            }
            User loggedUser = User.getInstance();
            loggedUser.setId(jId);
            loggedUser.setName(jName);
            loggedUser.setEmail(jEmail);
            loggedUser.setEntries(jEntries);
            return loggedUser;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
