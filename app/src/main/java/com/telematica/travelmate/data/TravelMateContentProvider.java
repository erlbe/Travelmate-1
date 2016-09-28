package com.telematica.travelmate.data;

import android.app.backup.BackupManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.telematica.travelmate.utilities.Constants;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Gives Access to the data in the SGLite database
 * Methods:
 * checkColumns
 * onCreate
 * query
 * getType
 * insert
 * delete
 * update
 */
public class TravelMateContentProvider extends ContentProvider {

    private static final int ENTRY = 100;
    private static final int ENTRIES = 101;

    private static final int CATEGORY = 200;
    private static final int CATEGORIES = 201;




    private static final String AUTHORITY = "com.telematica.travelmate.data.provider";
    private BackupManager backupManager;

    private static final String BASE_PATH_ENTRY = "entry";
    private static final String BASE_PATH_CATEGORY = "category";


    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_ENTRY);
    public static final Uri CONTENT_URI_CAT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_CATEGORY);



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private DatabaseHelper dbHelper;

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_ENTRY, ENTRIES);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_ENTRY + "/#", ENTRY);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_CATEGORY, CATEGORIES);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_CATEGORY + "/#", CATEGORY);

    }



    private void checkColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));


            HashSet<String> availableEntryColumns = new HashSet<String>(Arrays.asList(Constants.COLUMNS_ENTRY));
            HashSet<String> availableCategoryColumns = new HashSet<String>(Arrays.asList(Constants.COLUMNS_CATEGORY));


            if (!availableEntryColumns.containsAll(requestedColumns) &&
                    !availableCategoryColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }


    @Override
    public boolean onCreate() {
        dbHelper = DatabaseHelper.newInstance(getContext());
        backupManager = new BackupManager(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);

        int type = URI_MATCHER.match(uri);
        switch (type){
            case ENTRIES:
                queryBuilder.setTables(Constants.ENTRIES_TABLE);
                break;
            case ENTRY:
                queryBuilder.setTables(Constants.ENTRIES_TABLE);
                queryBuilder.appendWhere(Constants.COLUMN_ID + " = " + uri.getLastPathSegment());
                break;
            case CATEGORIES:
                queryBuilder.setTables(Constants.CATEGORY_TABLE);
                break;
            case CATEGORY:
                queryBuilder.setTables(Constants.CATEGORY_TABLE);
                queryBuilder.appendWhere(Constants.COLUMN_ID + " = " + uri.getLastPathSegment());
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long id;
        switch (type){
            case ENTRIES:
                id = db.insert(Constants.ENTRIES_TABLE, null, values);
                break;
            case CATEGORIES:
                id = db.insert(Constants.CATEGORY_TABLE, null, values);
                break;
             default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        backupManager.dataChanged();
        return Uri.parse(BASE_PATH_ENTRY + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type){
            case ENTRIES:
                affectedRows = db.delete(Constants.ENTRIES_TABLE, selection, selectionArgs);
                break;
            case ENTRY:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    affectedRows = db.delete(Constants.ENTRIES_TABLE, Constants.COLUMN_ID + "=" + id, null);
                } else {
                    affectedRows = db.delete(Constants.ENTRIES_TABLE, Constants.COLUMN_ID + "=" + id + " and " + selection , selectionArgs );
                }
                break;
            case CATEGORIES:
                affectedRows = db.delete(Constants.CATEGORY_TABLE, selection, selectionArgs);
                break;
            case CATEGORY:
                String catId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    affectedRows = db.delete(Constants.CATEGORY_TABLE, Constants.COLUMN_ID + "=" + catId, null);
                } else {
                    affectedRows = db.delete(Constants.CATEGORY_TABLE, Constants.COLUMN_ID + "=" + catId + " and " + selection , selectionArgs );
                }
                break;
               default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type) {
            case ENTRIES:
                affectedRows = db.update(Constants.ENTRIES_TABLE, values, selection, selectionArgs);
                break;

            case ENTRY:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.update(Constants.ENTRIES_TABLE, values, Constants.COLUMN_ID + "=" + id, null);
                } else {
                    affectedRows = db.update(Constants.ENTRIES_TABLE, values, Constants.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case CATEGORIES:
                affectedRows = db.update(Constants.CATEGORY_TABLE, values, selection, selectionArgs);
                break;
            case CATEGORY:
                String catId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.update(Constants.CATEGORY_TABLE, values, Constants.COLUMN_ID + "=" + catId, null);
                } else {
                    affectedRows = db.update(Constants.CATEGORY_TABLE, values, Constants.COLUMN_ID + "=" + catId + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        backupManager.dataChanged();
        return affectedRows;
    }


}
