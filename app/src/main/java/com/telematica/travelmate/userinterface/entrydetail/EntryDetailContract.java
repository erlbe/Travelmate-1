package com.telematica.travelmate.userinterface.entrydetail;


import com.telematica.travelmate.model.Entry;


public interface EntryDetailContract {


   interface View{
      void displayEntry(Entry entry);
      void showEditEntryScreen(long entryId);
      void showDeleteConfirmation(Entry entry);
      void displayBackgroundColor();
      void displayPreviousActivity();
      void displayMessage(String message);
      void displayReadOnlyViews();

   }

   interface Action{
      void onEditEntryClick();
      void showEntryDetails();
      void onDeleteEntryButtonClicked();
      void deleteEntry();

   }






}
