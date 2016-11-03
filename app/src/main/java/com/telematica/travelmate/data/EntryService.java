package com.telematica.travelmate.data;

import android.content.Intent;
import android.os.AsyncTask;

import com.telematica.travelmate.connection.HttpServerConnection;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.model.User;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.telematica.travelmate.utilities.Constants.SERVER_LINK;

/**
 * Created by Erlend on 19.10.2016.
 */

public class EntryService {

    public static void addEntry(final Entry entry){

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(Void... voids) {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("title", entry.getTitle());
                    jsonParam.put("content", entry.getContent());
                    jsonParam.put("userId", User.getInstance().getId());
                    System.out.println(jsonParam);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String result = new HttpServerConnection().connectToServer(SERVER_LINK + "/entry", 15000, "POST", jsonParam);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                //TODO: Handle wrong username/password
                if(result != null){
                    System.out.println("JSON:");
                    System.out.println(result);
                }
            }
        };
        task.execute();
    }
}
