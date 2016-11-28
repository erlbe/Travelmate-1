package com.telematica.travelmate.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;

import com.telematica.travelmate.connection.HttpServerConnection;
import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.model.User;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;
import com.telematica.travelmate.userinterface.entrylist.EntryListAdapter;
import com.telematica.travelmate.utilities.Constants;

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

    public static void addEntry(final Entry entry, final AsyncQueryListener listener){

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
                    jsonParam.put("category", entry.getCategoryName());
                    // Handle if there is no image
                    try{
                        jsonParam.put("image", Base64.encodeToString(entry.getImage(), Base64.DEFAULT));
                    }
                    catch (JSONException e) {
                        // No image
                    }
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
                    listener.onInsertComplete(Constants.INSERT_ENTRY, null, result.length());
                }
                listener.onInsertComplete(Constants.INSERT_ENTRY, null, 0);
            }
        };
        task.execute();
    }

    public static void updateEntry(final Entry entry, final AsyncQueryListener listener){

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
                    jsonParam.put("category", entry.getCategoryName());
                    try{
                        jsonParam.put("image", Base64.encodeToString(entry.getImage(), Base64.DEFAULT));
                    }
                    catch (JSONException e) {
                        // No image
                    }
                    System.out.println(jsonParam);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String result = new HttpServerConnection().connectToServer(SERVER_LINK + "/entry/" + entry.getId(), 15000, "PUT", jsonParam);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result != null){
                    System.out.println("JSON:");
                    System.out.println(result);
                    listener.onUpdateComplete(0, null, result.length());
                }
                else{
                    listener.onUpdateComplete(0, null, 0);
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
                            String categoryName = jEntry.getString("category");
                            try{
                                byte[] image = Base64.decode(jEntry.getString("image"), Base64.DEFAULT);
                                newEntry.setImage(image);
                            }
                            catch (JSONException e) {
                                // No image
                            }

                            //FIXME: Implement date modified backend
                            Long dateModified = GregorianCalendar.getInstance().getTimeInMillis();

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

    public static void deleteEntry(final Entry entry, final AsyncQueryListener listener) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = new HttpServerConnection().connectToServer(SERVER_LINK + "/entry/" + entry.getId(), 15000, "DELETE", null);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result != null){
                    System.out.println("JSON:");
                    System.out.println(result);
                    listener.onDeleteComplete(Constants.DELETE_ENTRY, null, result.length());
                }
                else {
                    listener.onDeleteComplete(Constants.DELETE_ENTRY, null, 0);
                }
            }
        };
        task.execute();
    }
}