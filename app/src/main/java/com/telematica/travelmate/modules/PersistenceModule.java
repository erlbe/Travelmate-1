package com.telematica.travelmate.modules;

import android.content.Context;

import com.telematica.travelmate.data.CategorySQLiteRepository;
import com.telematica.travelmate.data.EntryAPIRepository;
import com.telematica.travelmate.data.EntrySQLiteRepository;
import com.telematica.travelmate.userinterface.category.CategoryListContract;
import com.telematica.travelmate.userinterface.entrylist.EntryListContract;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides repositories for entries and categories
 */
@Module
public class PersistenceModule {
    @Provides
    @Singleton
    public EntryListContract.Repository providesEntryRepository(Context context){
        return new EntryAPIRepository(context);
    }


    @Provides
    @Singleton
    public CategoryListContract.Repository providesCategoryManager(Context context){
        return new CategorySQLiteRepository(context);
    }





}
