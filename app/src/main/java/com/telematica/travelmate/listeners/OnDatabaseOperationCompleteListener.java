package com.telematica.travelmate.listeners;

/**
 * Interface defines the methods which are used
 * to surface synchronous data operations back to the calling class
 */
public interface OnDatabaseOperationCompleteListener {
    void onSQLOperationFailed(String error);
    void onSQLOperationSucceded(String message);
}
