package com.telematica.travelmate.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.telematica.travelmate.utilities.Constants;
import com.telematica.travelmate.utilities.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Manages database creation
 * Methods:
 * onCreate
 * onUpgrade
 * backup
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    public static final String LOG_CAT = DatabaseHelper.class.getSimpleName();

    private static DatabaseHelper mDatabaseInstance = null;
    private Context mContext;



    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;


    public static DatabaseHelper newInstance(Context context) {
        //first check to see if the database helper
        //member data is null
        //create a new one if it is null

        if (mDatabaseInstance == null) {
            mDatabaseInstance = new DatabaseHelper(context.getApplicationContext());
        }

        //either way we have to always return an instance of
        //our database class each time this method is called
        return mDatabaseInstance;
    }


    //make the constructor private so it cannot be
    //instantiated outside of this class
    private DatabaseHelper(Context context) {
        super(context, Constants.SQLITE_DATABASE, null, DATABASE_VERSION);
        this.mContext = context;
        mPref = mContext.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        mEditor = mPref.edit();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_TABLE_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


    private static final String CREATE_TABLE_ENTRY = "create table "
            + Constants.ENTRIES_TABLE
            + "("
            + Constants.COLUMN_ID + " integer primary key autoincrement, "
            + Constants.COLUMN_TITLE + " text not null, "
            + Constants.COLUMN_CONTENT + " text not null, "
            + Constants.COLUMN_COLOR + " integer not null, "
            + Constants.COLUMNS_CATEGORY_ID + " INTEGER not null,"
            + Constants.COLUMN_CATEGORY_NAME + " TEXT NOT NULL, "
            + Constants.COLUMN_MODIFIED_TIME + " integer not null, "
            + Constants.COLUMN_CREATED_TIME + " integer not null, "
            + "FOREIGN KEY(category_id) REFERENCES category(_id)" + ")";

    //String to create a category table
    private static final String CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + Constants.CATEGORY_TABLE + "("
                    + Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Constants.COLUMN_NAME + " TEXT NOT NULL, "
                    + Constants.COLUMN_CREATED_TIME + " BIGINT, "
                    + Constants.COLUMN_MODIFIED_TIME + " BIGINT " + ")";








    /**
     * Save database to file.
     *
     * @param newDatabase Path to the database file.
     * @return Success.
     */
    public boolean backup(String newDatabase) {

        // Get database files.
        File newDb = new File(newDatabase);
        File localDb = new File(String.valueOf(mContext.getDatabasePath(Constants.SQLITE_DATABASE)));

        // Attempt to backup the database.
        try {

            // Create the backup database file if it doesn't already exist.
            newDb.createNewFile();

            // Ensure both files exist.
            if (localDb.exists() && newDb.exists()) {

                // Copy the local database file to the backup database file.
                FileUtils.copyFile(new FileInputStream(localDb), new FileOutputStream(newDb));

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }






}
