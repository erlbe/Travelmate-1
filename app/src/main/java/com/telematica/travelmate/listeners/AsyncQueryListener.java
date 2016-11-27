package com.telematica.travelmate.listeners;

import android.database.Cursor;
import android.net.Uri;

/**
 * Interface defines the methods which are used
 * to surface async data operations back to the calling class
 */
public interface AsyncQueryListener {
    void onInsertComplete(int token, Object cookie, int result);
    void onQueryComplete(int token, Object cookie, Cursor cursor);
    void onUpdateComplete(int token, Object cookie, int result);
    void onDeleteComplete(int token, Object cookie, int result);
}
