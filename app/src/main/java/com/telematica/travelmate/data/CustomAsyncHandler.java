package com.telematica.travelmate.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.telematica.travelmate.listeners.AsyncQueryListener;

import java.lang.ref.WeakReference;

/**
 * Creating the Channel for the Async Task
 * Methods:
 * onInsertComplete
 * onQueryComplete
 * onUpdateComplete
 */
public class CustomAsyncHandler extends AsyncQueryHandler{

    private WeakReference<AsyncQueryListener> mListener;


    public CustomAsyncHandler(ContentResolver cr, AsyncQueryListener listener) {
        super(cr);
        mListener = new WeakReference<AsyncQueryListener>(listener);
    }

    /**
     * Assign the given {@link AsyncQueryListener} to receive query events from
     * asynchronous calls. Will replace any existing listener.
     */
    public void setListener(WeakReference<AsyncQueryListener> listener) {
        mListener = listener;
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        final AsyncQueryListener listener = mListener.get();
        if (listener != null){
            listener.onInsertComplete(token, cookie, uri);
        }
        super.onInsertComplete(token, cookie, uri);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        final AsyncQueryListener listener = mListener.get();
        if (listener != null){
            listener.onQueryComplete(token, cookie, cursor);
        }
        super.onQueryComplete(token, cookie, cursor);
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        final AsyncQueryListener listener = mListener.get();
        if (listener != null){
            listener.onUpdateComplete(token, cookie, result);
        }
        super.onUpdateComplete(token, cookie, result);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        final AsyncQueryListener listener = mListener.get();
        if (listener != null){
            listener.onDeleteComplete(token, cookie, result);
        }
        super.onDeleteComplete(token, cookie, result);
    }
}
