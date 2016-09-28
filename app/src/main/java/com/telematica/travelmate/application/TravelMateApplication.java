package com.telematica.travelmate.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.telematica.travelmate.data.AddSampleDateIntentService;
import com.telematica.travelmate.modules.AppComponent;
import com.telematica.travelmate.modules.AppModule;
import com.telematica.travelmate.modules.DaggerAppComponent;
import com.telematica.travelmate.utilities.Constants;



/**
 * ---Main Class---
 * It creates the UI by building the modules for it
 * And checks if the App is running the first time or not
 */
public class TravelMateApplication extends Application {
    private static TravelMateApplication instance = new TravelMateApplication();
    private static AppComponent appComponent;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static TravelMateApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
       getAppComponent();
       addDefaultData();

    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }

        return appComponent;
    }


    //Checks if this is the first time TravelMate is running and if so
    // it starts the Intent Services to add some default entries
    private void addDefaultData() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean(Constants.FIRST_RUN, true)) {
            startService(new Intent(this, AddSampleDateIntentService.class));
            editor.putBoolean(Constants.FIRST_RUN, false).commit();
        }

    }






}
