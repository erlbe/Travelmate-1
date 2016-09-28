package com.telematica.travelmate.userinterface.category;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telematica.travelmate.R;
import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.events.AddCategoryEvent;
import com.telematica.travelmate.model.Category;
import com.telematica.travelmate.utilities.Constants;
import com.squareup.otto.Bus;

import javax.inject.Inject;


public class AddEditCategoryDialogFragment extends DialogFragment {
    private EditText mCategoryEditText;
    private Category mCategory;
    private boolean mInEditMode = false;

    @Inject
    Bus mBus;


    public AddEditCategoryDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TravelMateApplication.getInstance().getAppComponent().inject(this);
        mBus.register(this);
    }

    public static AddEditCategoryDialogFragment newInstance(String content){
        AddEditCategoryDialogFragment dialogFragment = new AddEditCategoryDialogFragment();
        Bundle args = new Bundle();

        if (!content.isEmpty()){
            args.putString(Constants.SERIALIZED_CATEGORY, content);
            dialogFragment.setArguments(args);
        }

        return dialogFragment;
    }

    /**
     * The method gets the Category that was passed in, in the form of serialized String
     * if nothing was passed in then it will create a new Category
     *
     */
    public void getCurrentCategory(){
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.SERIALIZED_CATEGORY)){
            String serializedCategory = args.getString(Constants.SERIALIZED_CATEGORY, "");
            if (!serializedCategory.isEmpty()){
                Gson gson = new Gson();
                mCategory = gson.fromJson(serializedCategory, new TypeToken<Category>(){}.getType());
                if (mCategory != null & mCategory.getId() > 0){
                    mInEditMode = true;
                }
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder addCategoryDialog = new AlertDialog.Builder(getActivity());


        if (savedInstanceState == null){


            LayoutInflater inflater = getActivity().getLayoutInflater();

            View convertView = inflater.inflate(R.layout.fragment_add_edit_category_dialog, null);
            addCategoryDialog.setView(convertView);

            getCurrentCategory();

            View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
            TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
            titleText.setText(mInEditMode == true ? getString(R.string.edit_category) : getString(R.string.add_category));
            addCategoryDialog.setCustomTitle(titleView);

            mCategoryEditText = (EditText)convertView.findViewById(R.id.edit_text_add_category);


            addCategoryDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            addCategoryDialog.setPositiveButton(mInEditMode == true ? "Update" : "Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });



            if (mInEditMode){
                populateFields(mCategory);
                addCategoryDialog.setTitle(mCategory.getCategoryName());
            }


        }

        return addCategoryDialog.create();
    }

    private void populateFields(Category category) {
        mCategoryEditText.setText(category.getCategoryName());
    }

    private boolean requiredFieldCompleted(){
        if (mCategoryEditText.getText().toString().isEmpty())
        {
            mCategoryEditText.setError(getString(R.string.name_is_required));
            mCategoryEditText.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();


        if (d != null){
            Button positiveButton = (Button)d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean readyToCloseDialog = false;
                    if (requiredFieldCompleted()) {
                        saveCategory();
                        readyToCloseDialog = true;
                    }
                    if (readyToCloseDialog)
                        dismiss();
                }
            });
        }
    }

    private void saveCategory() {
        if (mInEditMode){
            if (mCategory != null){
                mCategory.setCategoryName(mCategoryEditText.getText().toString().trim());
                mBus.post(new AddCategoryEvent(mCategory));
            }
        }else {
            Category selectedCategory = new Category();
            selectedCategory.setCategoryName(mCategoryEditText.getText().toString().trim());
            mBus.post(new AddCategoryEvent(selectedCategory));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }


}
