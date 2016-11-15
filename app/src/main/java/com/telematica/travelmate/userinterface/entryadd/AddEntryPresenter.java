package com.telematica.travelmate.userinterface.entryadd;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.model.Category;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.category.CategoryListContract;
//import com.telematica.travelmate.userinterface.color.ColorChangeEvent;
import com.telematica.travelmate.userinterface.entrylist.EntryListContract;
import com.telematica.travelmate.utilities.Constants;
import com.telematica.travelmate.utilities.FileUtils;

import java.util.List;

import javax.inject.Inject;

import static com.telematica.travelmate.data.EntryService.addEntry;


public class AddEntryPresenter implements AddEntryContract.Action, AsyncQueryListener {


    private final AddEntryContract.View mView;
    @Inject CategoryListContract.Repository mCategoryRepository;
    @Inject EntryListContract.Repository mEntryRepository;
    private boolean mEditMode = false;
    private Entry mCurrentEntry = null;


    public AddEntryPresenter(AddEntryContract.View addEntryView, long entryId) {
        TravelMateApplication.getInstance().getAppComponent().inject(this);
        mView = addEntryView;


        if (entryId > 0){
            mCurrentEntry = mEntryRepository.getEntryById(entryId);
            mEditMode = true;
        }


    }


    @Override
    public void onAddClick(String title, String category, String content, Bitmap image) {
        if (mEditMode && mCurrentEntry != null){
            mCurrentEntry.setContent(content);
            mCurrentEntry.setTitle(title);
            mCurrentEntry.setCategoryName(category);
            mCurrentEntry.setImage(FileUtils.getBytesFromImage(image));
            mEntryRepository.updateAsync(mCurrentEntry, this);
        }else {
            Entry entry = new Entry();
            entry.setTitle(title);
            entry.setContent(content);
            entry.setCategoryName(category);
            entry.setImage(FileUtils.getBytesFromImage(image));
            mEntryRepository.addAsync(entry, this);

            addEntry(entry);
        }


    }


    @Override
    public void checkStatus() {
        if (mEditMode && mCurrentEntry != null && mCurrentEntry.getId() > 0){
            mView.populateEntry(mCurrentEntry);
        }
    }

    @Override
    public void initiateImage() {
        if (mEditMode && mCurrentEntry != null && mCurrentEntry.getId() > 0){
            mView.populateImage(mCurrentEntry);
        }
    }

    @Override
    public void onDeleteEntryButtonClicked() {
        if (mEditMode && mCurrentEntry != null){
            mView.displayDeleteConfirmation(mCurrentEntry);
        }else {
            mView.displayDiscardConfirmation();
        }

    }

    @Override
    public void deleteEntry() {
        if (mCurrentEntry != null && mCurrentEntry.getId() > 0) {
            mEntryRepository.deleteAsync(mCurrentEntry, this);
        }
    }


    @Override
    public void saveOnExit(String title, String category, String content, Bitmap image) {
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)){
            return;
        }

        if (TextUtils.isEmpty(title)) {
            title = content;
        }

        if (TextUtils.isEmpty(content)) {
            content = title;
        }

        if (TextUtils.isEmpty(category)){
            category = Constants.DEFAULT_CATEGORY;
        }
        onAddClick(title, category, content, image);
        //mView.displayPreviousActivity();

    }

    @Override
    public void onSelectCategory() {
        List<Category> mCategories = mCategoryRepository.getAllCategories();
        mView.showChooseCategoryDialog(mCategories);
    }


    //region Implementation of AsyncQueryHandler Listener
    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {
        if (token == Constants.INSERT_ENTRY){
            long result = Long.parseLong(uri.getLastPathSegment());
            if (result > 0){
                mView.displayMessage("Entry added");
                //Refresh screen
                mView.displayPreviousActivity();
            }else {
                mView.displayMessage("Unable to add Entry");
            }
        }
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {

    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {
        if (result > 0){
            mView.displayMessage("Entry updated");
        }else {
            mView.displayMessage("Unable to update Entry");
        }
    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {
        if (result > 0){
            mView.displayMessage("Entry deleted");
        }else {
            mView.displayMessage("Unable to delete Entry");
        }
    }
    //endregion


    @Override
    public void killImage(){
        mView.deleteImage(mCurrentEntry);
    }


    @Override
    public void putImage(Bitmap bitmap){
        mCurrentEntry.setImage(FileUtils.getBytesFromImage(bitmap));
        //mView.addImage(mCurrentEntry, bitmap);
    }

}
