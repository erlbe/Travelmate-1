package com.telematica.travelmate.userinterface.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.telematica.travelmate.R;
import com.telematica.travelmate.listeners.OnCategorySelectedListener;
import com.telematica.travelmate.model.Category;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder>{
    private final static String LOG_CAT = CategoryListAdapter.class.getSimpleName();
    private final static boolean DEBUG = false;

    private List<Category> mCategories;
    private final Map<Long, Integer> entryCount;
    private final OnCategorySelectedListener mListener;
    private final Context mContext;

    public CategoryListAdapter(Context mContext, List<Category> mCategories,
                                       Map<Long, Integer> entryCount, OnCategorySelectedListener mListener) {
        this.mCategories = mCategories;
        this.entryCount = entryCount;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_category_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Category category = mCategories.get(position);
        holder.categoryName.setText(category.getCategoryName());
        int numEntry;

        try {
            numEntry = entryCount.get(category.getId());
        } catch (Exception e) {
            numEntry = 0;
        }
        if (DEBUG){
            Log.d(LOG_CAT, entryCount.toString());
        }

        String entries = numEntry > 1 ? "Entries" : "Entry";
        holder.entryCountTextView.setText(numEntry + " " + entries);
    }

    public void replaceData(List<Category> categories){
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_button_edit_category) ImageButton editCategory;
        @BindView(R.id.image_button_delete_category) ImageButton deleteCategory;
        @BindView(R.id.text_view_category_name)  TextView categoryName;
        @BindView(R.id.text_view_entry_count) TextView entryCountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            editCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category categoryToBeEdited = mCategories.get(getLayoutPosition());
                    mListener.onEditCategoryButtonClicked(categoryToBeEdited);
                }
            });
            deleteCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category categoryToBeEdited = mCategories.get(getLayoutPosition());
                    mListener.onDeleteCategoryButtonClicked(categoryToBeEdited);
                }
            });

        }
    }
}
