package com.telematica.travelmate.events;

import com.telematica.travelmate.model.Entry;

/**
 * Event in case two panels are shown on a tablet
 */
public class ShowEntryDualPaneEvent {
    private final Entry entry;

    public ShowEntryDualPaneEvent(Entry entry) {
        this.entry = entry;
    }

    public Entry getEntry() {
        return entry;
    }
}
