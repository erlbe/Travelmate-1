package com.telematica.travelmate.listeners;


import com.telematica.travelmate.model.Category;

/**
 * Interface defines methods for category buttons
 */
public interface OnCategorySelectedListener {
    void onCategorySelected(Category selectedCategory);
    void onEditCategoryButtonClicked(Category selectedCategory);
    void onDeleteCategoryButtonClicked(Category selectedCategory);
}
