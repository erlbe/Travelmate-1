package com.telematica.travelmate.userinterface.entrydetail;

import android.database.Cursor;
import android.net.Uri;

import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.entrylist.EntryListContract;

import javax.inject.Inject;


public class EntryDetailPresenter implements EntryDetailContract.Action,
        AsyncQueryListener {

    @Inject EntryListContract.Repository mRepository;
    private EntryDetailContract.View mView;
    private final long entryId;

    public EntryDetailPresenter(EntryDetailContract.View entriesDetailView, long entryId) {
        mView = entriesDetailView;
        this.entryId = entryId;
        TravelMateApplication.getInstance().getAppComponent().inject(this);
    }


    @Override
    public void onEditEntryClick() {
        mView.showEditEntryScreen(entryId);
    }

    @Override
    public void showEntryDetails() {
        Entry selectedEntry = mRepository.getEntryById(entryId);
        mView.displayEntry(selectedEntry);
    }

    @Override
    public void onDeleteEntryButtonClicked() {
        mView.showDeleteConfirmation(mRepository.getEntryById(entryId));
    }

    @Override
    public void deleteEntry() {
        mRepository.deleteAsync(mRepository.getEntryById(entryId), this);
    }


    @Override
    public void onInsertComplete(int token, Object cookie, int result) {
        
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {

    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {

    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {

    }
}
