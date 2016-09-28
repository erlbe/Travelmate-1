package com.telematica.travelmate.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.listeners.OnDatabaseOperationCompleteListener;
import com.telematica.travelmate.model.Category;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.category.CategoryListContract;
import com.telematica.travelmate.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates the environment for the SQLite Database for the Categories
 * Methods:
 * addAsync
 * updateAsync
 * deleteAsync
 * getAllCategories
 * getEntryCount
 * RemoveAllAsync
 * getCategoryById
 * createOrGetCategoryId
 * addCategory
 * saveCategory
 * getCategory
 * add
 */
public class CategorySQLiteRepository implements CategoryListContract.Repository {

    private final DatabaseHelper mDatabaseHelper;
    private final Context mContext;
    private SQLiteDatabase database;

    public CategorySQLiteRepository(Context context) {
        mContext = context;
        mDatabaseHelper = DatabaseHelper.newInstance(context);
        database = mDatabaseHelper.getWritableDatabase();

    }


    @Override
    public void addAsync(String name, AsyncQueryListener listener) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME, name);

        //Make Asynchronous database insert via the Content Provider
        CustomAsyncHandler queryHandler = new CustomAsyncHandler(mContext.getContentResolver(), listener);
        queryHandler.startInsert(Constants.INSERT_CATEGORY, null, TravelMateContentProvider.CONTENT_URI_CAT, values);
    }

    @Override
    public void updateAsync(Category category, AsyncQueryListener listener) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME, category.getCategoryName());

        //Make Asynchronous database insert via the Content Provider
        CustomAsyncHandler queryHandler = new CustomAsyncHandler(mContext.getContentResolver(), listener);
        queryHandler.startUpdate(Constants.INSERT_CATEGORY, null,
                TravelMateContentProvider.CONTENT_URI_CAT, values,  Constants.COLUMN_ID + "=" + category.getId(), null);
    }

    @Override
    public void deleteAsync(Category category, AsyncQueryListener listener) {
        CustomAsyncHandler queryHandler = new CustomAsyncHandler(mContext.getContentResolver(), listener);
        queryHandler.startDelete(Constants.DELETE_CATEGORY, null, TravelMateContentProvider.CONTENT_URI_CAT,
                Constants.COLUMN_ID + "=" + category.getId(), null);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(TravelMateContentProvider.CONTENT_URI_CAT, Constants.COLUMNS_CATEGORY, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                categories.add(Category.fromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return categories;
    }

    /**
     * This method returns the count of Entries assigned to a Category
     * This is displayed in the Category list Screen
     * @param categoryId - Parent category for the Entry
     * @return number of Entries found
     */
    @Override
    public int getEntryCount(long categoryId) {
        List<Entry> entries = new ArrayList<Entry>();
        Cursor cursor = mContext.getContentResolver().query(TravelMateContentProvider.CONTENT_URI, Constants.COLUMNS_ENTRY,
                Constants.COLUMNS_CATEGORY_ID + " = " + categoryId, null, null);
        if (cursor != null){
            return cursor.getCount();
        }

        return 0;
    }

    @Override
    public void removeAllAsync(AsyncQueryListener listener) {
        //Get all categories
        List<Category> categories = getAllCategories();

        //Ensure the list is not empty
        if (categories != null && categories.size() > 0){
            for (Category category: categories){
                deleteAsync(category, listener);
            }
        }
    }

    @Override
    public Category getCategoryById(long id) {
        Category category;
        Cursor cursor = mContext.getContentResolver().query(TravelMateContentProvider.CONTENT_URI_CAT,
                Constants.COLUMNS_CATEGORY, Constants.COLUMN_ID + " = " + id, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        category = Category.fromCursor(cursor);
        return category != null ? category : null;
    }

    public long createOrGetCategoryId(final String category, OnDatabaseOperationCompleteListener listener) {
        Category foundCategory = getCategory(category);
        if(foundCategory == null) {
            foundCategory = addCategory(category, listener);
        }
        return foundCategory.getId();
    }

    private Category addCategory(final String categoryName, OnDatabaseOperationCompleteListener listener) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        saveCategory(category, listener);
        return category;
    }

    private void saveCategory(Category category, OnDatabaseOperationCompleteListener listener) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME, category.getCategoryName());
        try {
            long id = database.insertOrThrow(Constants.CATEGORY_TABLE, null, values);
        } catch (SQLException e) {
            listener.onSQLOperationFailed("Unable to save Category");

        }

    }

    private Category getCategory(final String categoryName) {
        Category category = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.CATEGORY_TABLE + " " +
                "WHERE " + Constants.COLUMN_NAME + " = '" + categoryName + "'", null);
        if (cursor.moveToFirst()){
            category = Category.fromCursor(cursor);
        }
        return category;
    }


    /***
     * This method is called from the Intent-Service that adds default
     * Categories the first time the app runs
     * @param name
     */
    public void add(String name) {

        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME, name);
        try {
            long result = database.insertOrThrow(Constants.CATEGORY_TABLE, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

