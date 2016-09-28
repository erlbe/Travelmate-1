package com.telematica.travelmate.userinterface.category;


import com.telematica.travelmate.listeners.AsyncQueryListener;
import com.telematica.travelmate.listeners.OnDatabaseOperationCompleteListener;
import com.telematica.travelmate.model.Category;

import java.util.List;
import java.util.Map;


public interface CategoryListContract {
    interface View{
        void showAddNewCategoryDialog();
        void displayCategory(String category);
        void showEmptyText();
        void hideEmptyText();
        void displayMessage(String message);
        void showCategories(List<Category> categories, Map<Long,Integer> entryCount);
        void showConfirmDeleteCategoryPrompt(Category category);
        void showEditCategoryForm(Category category);
    }


    interface Action{
        void onSelectCategory();
        void addNewCategory(Category category);
        void loadCategories();
        void onDeleteCategoryButtonClicked(Category category);
        void onEditCategoryButtonClicked(Category category);
        void deleteCategory(Category category);
    }


    interface Repository {
        void addAsync(String name, AsyncQueryListener listener);
        void updateAsync(Category category, AsyncQueryListener listener);
        void deleteAsync(Category category, AsyncQueryListener listener);
        List<Category> getAllCategories();
        int getEntryCount(long categoryId);
        void removeAllAsync(AsyncQueryListener listener);
        Category getCategoryById(long id);
        public long createOrGetCategoryId(final String category, OnDatabaseOperationCompleteListener listener);
    }

}
