package com.telematica.travelmate.userinterface.category;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.events.AddCategoryButtonClickEvent;
import com.telematica.travelmate.events.AddCategoryEvent;
import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.listeners.OnDatabaseOperationCompleteListener;
import com.telematica.travelmate.model.Category;
import com.telematica.travelmate.utilities.Constants;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class CategoryListPresenter implements
        CategoryListContract.Action, AsyncQueryListener, OnDatabaseOperationCompleteListener {

    @Inject CategoryListContract.Repository mRepository;
    @Inject Bus mBus;
    private final CategoryListContract.View mView;

    private final static String LOG_CAT = CategoryListPresenter.class.getSimpleName();
    private final static boolean DEBUG = true;


    public CategoryListPresenter(CategoryListContract.View categoryView) {
        TravelMateApplication.getInstance().getAppComponent().inject(this);
        mBus.register(this);
        mView = categoryView;

    }

    @Override
    public void onSelectCategory() {
        Map<Long, Integer> entryCount = new HashMap<Long, Integer>();
        for (Category category : getCategories()){
            entryCount.put(category.getId(), mRepository.getEntryCount(category.getId()));
        }

    }

    @Override
    public void addNewCategory(Category category) {
        mView.displayCategory(category.getCategoryName());

        if (category.getId() > 0){
            mRepository.updateAsync(category, this);
        }else {
            mRepository.addAsync(category.getCategoryName(), this);
        }

    }

    @Override
    public void loadCategories() {
        List<Category> mCategories = getCategories();
        if (mCategories != null && mCategories.size() > 0){
            mView.hideEmptyText();

            Map<Long, Integer> entryCount = new HashMap<Long, Integer>();
            Map<Long, Integer> todoCount = new HashMap<Long, Integer>();
            Map<Long, Integer> sketchCount = new HashMap<Long, Integer>();

            try {
                for (Category category: mCategories){
                    if (DEBUG){
                        Log.d(LOG_CAT, category.getCategoryName() + ": " + category.getId());
                    }
                    entryCount.put(category.getId(), mRepository.getEntryCount(category.getId()));
                    if (DEBUG){
                        Log.d(LOG_CAT, entryCount.get(category.getId()) + ": " + category.getId());
                    }
                }
            } catch (Exception e) {
                mView.displayMessage("Error: " + e.getMessage());
            }
            mView.showCategories(mCategories, entryCount);

        } else {
            mView.showEmptyText();
        }


    }

    @Override
    public void onDeleteCategoryButtonClicked(Category category) {
        mView.showConfirmDeleteCategoryPrompt(category);

    }

    @Override
    public void onEditCategoryButtonClicked(Category category) {
        mView.showEditCategoryForm(category);
    }

    @Override
    public void deleteCategory(Category category) {
        mRepository.deleteAsync(category, this);
    }




    public List<Category> getCategories() {
        return mRepository.getAllCategories();
    }

    @Override
    public void onSQLOperationFailed(String error) {
        mView.displayMessage("Error: " + error);
        loadCategories();
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        mView.displayMessage(message);
        loadCategories();
    }


    @Subscribe
    public void onAddCategoryButtonClick(AddCategoryButtonClickEvent event){
        mView.showAddNewCategoryDialog();
    }


    @Subscribe
    public void onAddNewCategory(AddCategoryEvent event){
        addNewCategory(event.getCategory());
    }

    /**
     * This is the callback method for when the Asynchronous insert completes
     * At this point we check the id of the inserted row and if it is more than zero we report success
     * @param token - this was the token passed in to the insert method - Constants.INSERT_CATEGORY
     * @param cookie - An object that gets passed into onInsertComplete
     * @param uri - This is the URI of the inserted data
     */
    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {
        if (token == Constants.INSERT_CATEGORY){
            long result = Long.parseLong(uri.getLastPathSegment());
            if (result > 0){
                mView.displayMessage("Category added");
            }else {
                mView.displayMessage("Unable to add Category");
            }
        }
    }


    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {

    }

    /**
     *
     * @param token - The token passed in to the startUpdate method
     * @param cookie - An object passed to onUpdateComplete
     * @param result - The number of affected rows
     */
    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {
        if (result > 0){
            mView.displayMessage("Category updated");
        }else {
            mView.displayMessage("Unable to update Category");
        }
    }

    /**
     *
     * @param token - The token we passed into the startDelete method - Constants.DELETE_CATEGORY
     * @param cookie - An object passed to onDeleteComplete
     * @param result - The number affected rows - should be only one row
     */
    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {
        if (result > 0){
            mView.displayMessage("Category deleted");
        }else {
            mView.displayMessage("Unable to delete");
        }
    }
}
