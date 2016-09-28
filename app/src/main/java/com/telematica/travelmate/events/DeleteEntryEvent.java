package com.telematica.travelmate.events;


import com.telematica.travelmate.model.Entry;

/**
 * Event in case an entry is deleted
 */
public class DeleteEntryEvent {
    private Entry mEntry;

    public DeleteEntryEvent(Entry entry){
        mEntry = entry;
    }

    public Entry getEntry() {
        return mEntry;
    }

    public void setEntry(Entry entry) {
        mEntry = entry;
    }
}
