package com.telematica.travelmate.model;

import android.database.Cursor;

import com.telematica.travelmate.utilities.Constants;



/**
 * Defines content of an entry
 * Methods:
 * getDateModified
 * setDateModified
 * getId
 * setId
 * getTitle
 * setTitle
 * getContent
 * setContent
 * getColor TODO: Remove Color
 * setColor
 */
public class Entry {


    private long id;

    private String title;
    private String content;
    private int color;
    private long dateCreated;
    private long dateModified;
    private String categoryName;
    private long categoryId;


    public Entry(){}

    public static Entry fromCursor(Cursor cursor){
        Entry entry = new Entry();
        entry.setId(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID)));
        entry.setTitle(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TITLE)));
        entry.setContent(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CONTENT)));
        entry.setColor(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_COLOR)));
        entry.setCategoryId(cursor.getLong(cursor.getColumnIndex(Constants.COLUMNS_CATEGORY_ID)));
        entry.setCategoryName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CATEGORY_NAME)));
        entry.setDateCreated(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_CREATED_TIME)));
        entry.setDateModified(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_MODIFIED_TIME)));
        return entry;
    }





    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
