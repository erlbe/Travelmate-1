package com.telematica.travelmate.userinterface.entryadd;


import com.telematica.travelmate.model.Category;
import com.telematica.travelmate.model.Entry;

import java.util.List;


public interface AddEntryContract {

    interface View{
        void populateEntry(Entry entry);
        void displayCategory(String category);
        void displayDeleteConfirmation(Entry entry);
        void displayDiscardConfirmation();
        void displayPreviousActivity();
        void displayMessage(String message);
        void showChooseCategoryDialog(List<Category> categories);
        void displayShareIntent();

    }

    interface Action{
        void onAddClick(String title, String category, String content, int color);
        void checkStatus();
        void onDeleteEntryButtonClicked();
        void deleteEntry();
        void saveOnExit(String title, String category, String content, int color);
        void onSelectCategory();


    }




}
