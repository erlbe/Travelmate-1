package com.telematica.travelmate.events;

import android.support.annotation.Nullable;



/**
 * Event in case entry details are shown
 */
public class ViewEntryDetailsEvent {
    private long mEntryId;

    public ViewEntryDetailsEvent(@Nullable long entryId){
        mEntryId = entryId;
    }


    public long getEntryId() {
        return mEntryId;
    }

    public void setEntry(long entryId) {
        mEntryId = entryId;
    }
}
