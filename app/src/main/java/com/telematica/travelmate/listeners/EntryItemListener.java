package com.telematica.travelmate.listeners;


import com.telematica.travelmate.model.Entry;

/**
 * Interface defines methods which are used
 * if Entry button or delete buttons are clicked
 */
public interface EntryItemListener {

    void onEntryClick(Entry clickedEntry);

    void onDeleteButtonClicked(Entry clickedEntry);
}
