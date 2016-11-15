package com.telematica.travelmate.data;

import android.content.Intent;
import android.os.AsyncTask;

import com.telematica.travelmate.connection.HttpServerConnection;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.model.User;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;
import com.telematica.travelmate.userinterface.entrylist.EntryListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
                if(result != null){
                    System.out.println("JSON:");
                    System.out.println(result);

                    try {
                        JSONObject jObject = new JSONObject(result);
                        Long jId = jObject.getLong("_id");

                        entry.setId(jId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        task.execute();
    }

    public static void loadAllEntries(final EntryListAdapter adapter){
        final List<Entry> entries = new ArrayList<Entry>();

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = new HttpServerConnection().connectToServer(SERVER_LINK + "/entries/" + User.getInstance().getId(), 15000, "GET", null);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result != null){
                    System.out.println("JSON:");
                    System.out.println(result);

                    try {
                        JSONArray jEntries = new JSONArray(result);
                        for (int i = 0; i < jEntries.length(); i++) {
                            Entry newEntry = new Entry();

                            JSONObject jEntry = jEntries.getJSONObject(i);

                            Long id = jEntry.getLong("_id");
                            String title = jEntry.getString("title");
                            String content = jEntry.getString("content");

                            //FIXME: Implement date modified backend
                            Long dateModified = GregorianCalendar.getInstance().getTimeInMillis();
                            //FIXME: Implement categories backend
                            String categoryName = "General";

                            newEntry.setId(id);
                            newEntry.setTitle(title);
                            newEntry.setContent(content);
                            newEntry.setDateModified(dateModified);
                            newEntry.setCategoryName(categoryName);
                            entries.add(newEntry);
                        }
                        adapter.setList(entries);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        task.execute();
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 565b42ad4413d2bc6035d9cc001adb9224c80ce7
