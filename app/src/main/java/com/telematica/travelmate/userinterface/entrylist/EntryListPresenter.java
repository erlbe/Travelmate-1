package com.telematica.travelmate.userinterface.entrylist;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.data.EntryService;
import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;


public class EntryListPresenter implements EntryListContract.Actions, AsyncQueryListener {

    private final EntryListContract.View mView;
    private boolean isDualScreen = false;

    @Inject Context mContext;
    @Inject EntryListContract.Repository mRepository;
    @Inject SharedPreferences mSharedPreference;



    public EntryListPresenter(EntryListContract.View entriesView){
        mView = checkNotNull(entriesView, "entriesView cannot be null");

        TravelMateApplication.getInstance().getAppComponent().inject(this);

    }

    /**
     * This method is called from the onResume of the Fragment
     * It gets the Entries from the Repository
     * And passes the Entries back to the View
     * The showEntries method in the View will then pass the
     * Returned Entries to the Adapter for display
     */
    @Override
    public void loadEntries() {
        List<Entry> entries = getEntries();

        if (entries != null && entries.size() > 0){
            mView.showEmptyText(false);
            mView.showEntries(entries);
            //Call to check if the user has a saved preference for sorting the Entries
            checkSortPreference();
        }else {
            mView.showEmptyText(true);
            mView.showEntries(new ArrayList<Entry>());
        }

    }


    @Override
    public void onAddNewEntryButtonClicked() {
        mView.showAddEntry();

    }

    @Override
    public void showSortOptions() {
        mView.displaySortOptions();
    }

    @Override
    public void openEntryDetails(@NonNull long entryId) {
        checkNotNull(entryId, "requested Entry cannot be null");
        if (isDualScreen) {
            mView.showDualDetailUi(mRepository.getEntryById(entryId));
        } else {
            mView.showSingleDetailUi(entryId);
        }
    }

    @Override
    public List<Entry> getEntries() {
        // FIXME: Load entries from internet here instead of from SQLite repository

        // "Old" version, getting it from SQLite
        //return mRepository.getAllEntries();

        return EntryService.getAllEntries();
    }

    @Override
    public void onDeleteEntryButtonClicked(Entry entry) {
        mView.showDeleteConfirmation(entry);
    }

    @Override
    public void deleteEntry(Entry entry) {
        mRepository.deleteAsync(entry, this);
        loadEntries();
    }

    @Override
    public void setLayoutMode(boolean dualScreen) {
        isDualScreen = dualScreen;
    }

    @Override
    public void checkSortPreference() {
        int sortOption = mSharedPreference.getInt(Constants.SORT_PREFERENCE, -1);
        if (sortOption == -1){
            //do nothing
        }else {
            //Apply the saved sort preference
            mView.applySortPreference(sortOption);
        }
    }



    //region Implementation of AsyncQueryHandler Listener
    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {
        if (token == Constants.INSERT_ENTRY){
            long result = Long.parseLong(uri.getLastPathSegment());
            if (result > 0){
                mView.showMessage("Entry added");
                //Refresh screen
                loadEntries();
            }else {
                mView.showMessage("Unable to add Entry");
            }
        }
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {

    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {
        if (result > 0){
            mView.showMessage("Entry updated");
            //Refresh screen
            loadEntries();
        }else {
            mView.showMessage("Unable to update Entry");
        }
    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {
        if (result > 0){
            mView.showMessage("Entry deleted");
        }else {
            mView.showMessage("Unable to delete Entry");
        }
    }
    //endregion
}
