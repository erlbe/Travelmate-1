package com.telematica.travelmate.userinterface.category;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.telematica.travelmate.R;
import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.listeners.OnCategorySelectedListener;
import com.telematica.travelmate.model.Category;

import java.util.List;


public class CategoryDialogFragment extends DialogFragment {

    private List<Category> mCategories;
    private CategoryAdapter mCategoryAdapter;
    private OnCategorySelectedListener mCategorySelectedListener;
   // @Inject Bus mBus;


    public void setCategorySelectedListener(OnCategorySelectedListener categorySelectedListener) {
        mCategorySelectedListener = categorySelectedListener;
    }

    public CategoryDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TravelMateApplication.getInstance().getAppComponent().inject(this);

    }

    public static CategoryDialogFragment newInstance(){
        return new CategoryDialogFragment();
    }

    public void setCategories(List<Category> categories) {
        mCategories = categories;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.category_dialog_list, null);
        builder.setView(convertView);
        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        builder.setCustomTitle(titleView);
        builder.setTitle("Choose Category");

        ListView dialogList = (ListView) convertView.findViewById(R.id.dialog_listview);
        TextView emptyText = (TextView) convertView.findViewById(R.id.category_list_empty);
        dialogList.setEmptyView(emptyText);

        mCategoryAdapter = new CategoryAdapter(getActivity(), mCategories);
        dialogList.setAdapter(mCategoryAdapter);

        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Category mSelectedCategory = mCategories.get(position);
                if (mSelectedCategory != null){
                    mCategorySelectedListener.onCategorySelected(mSelectedCategory);
                }
            }
        });

        return builder.create();

    }


}
