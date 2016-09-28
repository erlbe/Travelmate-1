package com.telematica.travelmate.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.telematica.travelmate.application.TravelMateApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides selected Prefences
 */
@Module
public class AppModule {

    private final TravelMateApplication app;

    public AppModule(TravelMateApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return app;
    }

    @Provides @Singleton
    SharedPreferences providesSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
