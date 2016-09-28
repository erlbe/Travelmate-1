package com.telematica.travelmate.data;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.listeners.OnDatabaseOperationCompleteListener;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;

import java.util.ArrayList;
import java.util.List;

//TODO: EVERYTHING

public class AddSampleDateIntentService extends IntentService {
    private final static String LOG_TAG = AddSampleDateIntentService.class.getSimpleName();


    public AddSampleDateIntentService() {
        super("AddSampleDateIntentService");

    }

    //Dummy implementation of the OnDatabaseOperation failed
    private OnDatabaseOperationCompleteListener mListener = new OnDatabaseOperationCompleteListener() {
        @Override
        public void onSQLOperationFailed(String error) {
            Log.d(LOG_TAG, error);
        }

        @Override
        public void onSQLOperationSucceded(String message) {
            Log.d(LOG_TAG, message);
        }
    };

    //Dummy implementation of AsyncQueryHandler
    AsyncQueryListener asyncQueryListener = new AsyncQueryListener() {
        @Override
        public void onInsertComplete(int token, Object cookie, Uri uri) {
            Log.d(LOG_TAG, "Entry added " + uri.getLastPathSegment());
        }

        @Override
        public void onQueryComplete(int token, Object cookie, Cursor cursor) {

        }

        @Override
        public void onUpdateComplete(int token, Object cookie, int result) {

        }

        @Override
        public void onDeleteComplete(int token, Object cookie, int result) {

        }
    };

    @Override
    protected void onHandleIntent(Intent intent) {


        CategorySQLiteRepository categoryRepository = new CategorySQLiteRepository(getApplicationContext());
        EntrySQLiteRepository entrySQLiteRepository = new EntrySQLiteRepository(getApplicationContext());


        List<String> categories = new ArrayList<>();
        categories.add("General");
        categories.add("Germany");
        categories.add("Norway");


        for (String cat : categories) {
              categoryRepository.add(cat);
        }

        //Get sample Entries
        List<Entry> entries = SampleData.getSampleEntries();

        for (Entry entry : entries) {
            entry.setCategoryId(categoryRepository.createOrGetCategoryId(entry.getCategoryName(), mListener));
            entrySQLiteRepository.addAsync(entry, asyncQueryListener );

        }



        //Once the data is added, simply restart the Entrylist
        //Activity so the default data will be displayed
        Intent restartIntent = new Intent(this, EntryListActivity.class);
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(restartIntent);


    }


}
