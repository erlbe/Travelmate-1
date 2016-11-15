package com.telematica.travelmate.userinterface.entrylist;

import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.model.Entry;

import java.util.List;


public interface EntryListContract {

    interface View {

        void showEntries(List<Entry> entries);
        void showAddEntry();
        void displaySortOptions();
        void showSingleDetailUi(long entryId);
        void showDualDetailUi(Entry entry);
        void showEmptyText(boolean showText);
        void showDeleteConfirmation(Entry entry);
        void showMessage(String message);
        void applySortPreference(int sortPreference);
    }

    interface Actions {
        void loadEntries();
        void onAddNewEntryButtonClicked();
        void showSortOptions();
        void openEntryDetails(long entryId);
        List<Entry> getEntries();
        void onDeleteEntryButtonClicked(Entry entry);
        void deleteEntry(Entry entry);
        void setLayoutMode(boolean dualScreen);
        void checkSortPreference();

    }

    interface Repository{
        void addAsync(Entry entry, AsyncQueryListener listener);
        void updateAsync(Entry entry, AsyncQueryListener listener);
        void deleteAsync(Entry entry, AsyncQueryListener listener);
        List<Entry> getAllEntries();
        Entry getEntryById(long id);
    }
}
