package com.telematica.travelmate.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.listeners.OnDatabaseOperationCompleteListener;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.entrylist.EntryListContract;
import com.telematica.travelmate.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates the environment for the SQLite Database for the Entries
 * Methods:
 * addAsync
 * updateAsync
 * update
 * deleteAsync
 * getAllEntries
 * getEntryById
 * getAllEntriesInCategory
 */
public class EntrySQLiteRepository implements EntryListContract.Repository{


    private CategorySQLiteRepository categorySQLiteManager;
    private final Context mContext;
    private final static String LOG_TAG = EntrySQLiteRepository.class.getSimpleName();

    public EntrySQLiteRepository(Context context) {
        mContext = context;

        categorySQLiteManager = new CategorySQLiteRepository(context);
    }

    @Override
    public void addAsync(Entry entry, AsyncQueryListener listener) {
        Long ret = null;
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE, entry.getTitle());
        values.put(Constants.COLUMN_CONTENT, entry.getContent());
        values.put(Constants.COLUMN_COLOR, entry.getColor());
        values.put(Constants.COLUMNS_CATEGORY_ID,
                categorySQLiteManager.createOrGetCategoryId(entry.getCategoryName(), new OnDatabaseOperationCompleteListener() {
            @Override
            public void onSQLOperationFailed(String error) {

            }

            @Override
            public void onSQLOperationSucceded(String message) {

            }
        }));
        values.put(Constants.COLUMN_CATEGORY_NAME, entry.getCategoryName());
        values.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());

        //Make Asynchronous database insert via the Content Provider
        CustomAsyncHandler queryHandler = new CustomAsyncHandler(mContext.getContentResolver(), listener);
        queryHandler.startInsert(Constants.INSERT_ENTRY, null, TravelMateContentProvider.CONTENT_URI, values);


    }

    @Override
    public void updateAsync(Entry entry, AsyncQueryListener listener) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE, entry.getTitle());
        values.put(Constants.COLUMN_CONTENT, entry.getContent());
        values.put(Constants.COLUMN_COLOR, entry.getColor());
        values.put(Constants.COLUMNS_CATEGORY_ID, entry.getCategoryId());
        values.put(Constants.COLUMN_CATEGORY_NAME, entry.getCategoryName());
        values.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());

        CustomAsyncHandler queryHandler = new CustomAsyncHandler(mContext.getContentResolver(), listener);
        queryHandler.startUpdate(Constants.INSERT_ENTRY, null,
                TravelMateContentProvider.CONTENT_URI, values, Constants.COLUMN_ID + "=" + entry.getId(), null);
    }


    //Called from the delete category service
    public void update(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE, entry.getTitle());
        values.put(Constants.COLUMN_CONTENT, entry.getContent());
        values.put(Constants.COLUMN_COLOR, entry.getColor());
        values.put(Constants.COLUMNS_CATEGORY_ID, entry.getCategoryId());
        values.put(Constants.COLUMN_CATEGORY_NAME, entry.getCategoryName());
        values.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());

        mContext.getContentResolver().update(TravelMateContentProvider.CONTENT_URI,
                values, Constants.COLUMN_ID + "=" + entry.getId(), null);
    }


    @Override
    public void deleteAsync(Entry entry, AsyncQueryListener listener) {
        CustomAsyncHandler queryHandler = new CustomAsyncHandler(mContext.getContentResolver(), listener);
        queryHandler.startDelete(Constants.DELETE_ENTRY, null, TravelMateContentProvider.CONTENT_URI,
                Constants.COLUMN_ID + "=" + entry.getId(), null);
    }

    @Override
    public List<Entry> getAllEntries() {
        List<Entry> entries = new ArrayList<Entry>();
        Cursor cursor = mContext.getContentResolver().query(TravelMateContentProvider.CONTENT_URI, Constants.COLUMNS_ENTRY, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                entries.add(Entry.fromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return entries;
    }


    @Override
    public Entry getEntryById(long id) {
        Entry entry;
        Cursor cursor = mContext.getContentResolver().query(TravelMateContentProvider.CONTENT_URI,
                Constants.COLUMNS_ENTRY, Constants.COLUMN_ID + " = " + id, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        entry = Entry.fromCursor(cursor);
        return entry != null ? entry : null;
    }


    public List<Entry> getAllEntriesInCategory(Long categoryId) {
        List<Entry> entries = new ArrayList<Entry>();
        Cursor cursor = mContext.getContentResolver()
                .query(TravelMateContentProvider.CONTENT_URI, Constants.COLUMNS_ENTRY,
                        Constants.COLUMNS_CATEGORY_ID + "=" + categoryId, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                entries.add(Entry.fromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return entries;
    }

}
