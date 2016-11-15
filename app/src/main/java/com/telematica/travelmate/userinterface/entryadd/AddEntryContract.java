package com.telematica.travelmate.userinterface.entryadd;


import android.graphics.Bitmap;

import com.telematica.travelmate.model.Category;
import com.telematica.travelmate.model.Entry;

import java.util.List;


public interface AddEntryContract {

    interface View{
        void populateEntry(Entry entry);
        void populateImage(Entry entry);
        void displayCategory(String category);
        void displayDeleteConfirmation(Entry entry);
        void displayDiscardConfirmation();
        void displayPreviousActivity();
        void displayMessage(String message);
        void showChooseCategoryDialog(List<Category> categories);
        void displayShareIntent();
        void deleteImage(Entry entry);
        void addImage(Entry entry, Bitmap bitmap);

    }

    interface Action{
        void onAddClick(String title, String category, String content,Bitmap image);
        void checkStatus();
        void initiateImage();
        void onDeleteEntryButtonClicked();
        void deleteEntry();
        void saveOnExit(String title, String category, String content, Bitmap image);
        void onSelectCategory();
        void killImage();
        void putImage(Bitmap bitmap);

    }




}
