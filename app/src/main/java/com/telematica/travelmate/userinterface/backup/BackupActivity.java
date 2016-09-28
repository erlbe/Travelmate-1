package com.telematica.travelmate.userinterface.backup;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.telematica.travelmate.R;
import com.telematica.travelmate.data.DatabaseHelper;
import com.telematica.travelmate.utilities.Constants;
import com.telematica.travelmate.utilities.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BackupActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private File backupFolder;
    private File appPath;
    private Activity mActivity;
    private View mRootView;
    
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private final static String LOG_TAG = BackupActivity.class.getSimpleName();
    private final static int EXTERNAL_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_goleft);
        mRootView = findViewById(R.id.container).getRootView();
        mActivity = this;

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        appPath = new File(Environment.getExternalStoragePublicDirectory(Constants.APP_FOLDER), "");
        backupFolder = new File(Environment.getExternalStoragePublicDirectory(Constants.BACKUP_FOLDER), "");
        if (!backupFolder.exists()) {
            backupFolder.mkdirs();
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        if (isStoragePermissionGranted()){
            displayBackupOptions();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    //Checks whether the user has granted the app permission to
    //access external storage
    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG,"Permission is granted");
                return true;
            } else {
                Log.v(LOG_TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_PERMISSION_REQUEST);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOG_TAG,"Permission is granted  API < 23");
            return true;
        }

    }

    //Called after a user grants permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case EXTERNAL_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted perform backup
                    displayBackupOptions();
                } else {
                    //permission was denied, disable backup
                    makeToast("Backup Access Denied");
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void backup(){

        // Get database files.
        File backupDb = new File(backupFolder, Constants.SQLITE_DATABASE);
        File localDb = new File(String.valueOf(getDatabasePath(Constants.SQLITE_DATABASE)));

        try {

            // Create the backup database file if it doesn't already exist.
            backupDb.createNewFile();

            //Ensure both the local database file and the backup file exists
            if (backupDb.exists() && localDb.exists()){
                // Copy the local database file to the backup database file.
                FileUtils.copyFile(new FileInputStream(localDb), new FileOutputStream(backupDb));

                //Save the path of the backup to SharedPreference
                String exportPath = backupDb.getAbsolutePath();
                editor.putString(Constants.REALM_EXPORT_FILE_PATH, exportPath ).commit();
                makeToast("backup completed");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void restore(){
        File backedUpDatabase = new File(sharedPreferences.getString(Constants.REALM_EXPORT_FILE_PATH, ""));
        File localDb = new File(String.valueOf(getDatabasePath(Constants.SQLITE_DATABASE)));

        // Attempt to restore the database.
        try {

            // Ensure the database we're restoring exists.
            if (backedUpDatabase.exists()) {

                // Restore the new database.
                FileUtils.copyFile(new FileInputStream(backedUpDatabase), new FileOutputStream(localDb));

                // Access the copied database so SQLiteHelper will cache it and mark it as created.
                DatabaseHelper databaseHelper = DatabaseHelper.newInstance(this);
                if (databaseHelper.getWritableDatabase() != null) {
                    databaseHelper.getWritableDatabase().close();
                }
                makeToast("Database restored");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    //shows Toast messages using Snackbar
    private void makeToast(String message){
        Snackbar snackbar = Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG);

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.primary));
        TextView tv = (TextView)snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void displayBackupOptions() {
        final  String[] sortOptions = { getString(R.string.backup), getString(R.string.restore)};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = getLayoutInflater();

        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);

        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        titleText.setText(R.string.backup_options);
        alertDialog.setCustomTitle(titleView);

        ListView dialogList = (ListView) convertView.findViewById(R.id.dialog_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (mActivity,android.R.layout.simple_list_item_1, sortOptions);
        dialogList.setAdapter(adapter);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final Dialog dialog = alertDialog.create();
        dialog.show();
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        backup();
                        dialog.dismiss();
                        break;
                    case 1:
                        restore();
                        dialog.dismiss();
                        break;

                }
            }
        });
    }

}
