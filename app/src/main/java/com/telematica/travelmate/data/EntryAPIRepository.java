package com.telematica.travelmate.data;

import android.content.Context;

import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.entrylist.EntryListAdapter;
import com.telematica.travelmate.userinterface.entrylist.EntryListContract;

import java.util.List;

/**
 * Created by Erlend on 27.11.2016.
 */

public class EntryAPIRepository implements EntryListContract.Repository {

    private final Context mContext;

    public EntryAPIRepository(Context context) {
        mContext = context;
    }

    @Override
    public void addAsync(Entry entry, AsyncQueryListener listener) {
        EntryService.addEntry(entry, listener);
    }

    @Override
    public void updateAsync(Entry entry, AsyncQueryListener listener) {
        EntryService.updateEntry(entry, listener);
        System.out.println("Update entry");
    }

    @Override
    public void deleteAsync(Entry entry, AsyncQueryListener listener) {
        EntryService.deleteEntry(entry, listener);
        System.out.println("Delete entry");
    }

    @Override
    public List<Entry> getAllEntries() {
        return EntryListAdapter.getInstance().getAllEntries();
    }

    @Override
    public Entry getEntryById(long id) {
        return EntryListAdapter.getInstance().getEntryById(id);
    }
}
