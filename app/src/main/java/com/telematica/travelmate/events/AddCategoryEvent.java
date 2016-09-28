package com.telematica.travelmate.events;


import com.telematica.travelmate.model.Category;

/**
 * Event in case a category is added
 */
public class AddCategoryEvent{
    private final Category mCategory;

    public AddCategoryEvent(Category category) {
        mCategory = category;
    }

    public Category getCategory() {
        return mCategory;
    }
}
