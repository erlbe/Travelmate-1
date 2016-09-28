package com.telematica.travelmate.userinterface.entryadd;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.telematica.travelmate.R;
import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.listeners.OnCategorySelectedListener;
import com.telematica.travelmate.model.Category;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.category.CategoryDialogFragment;
//import com.telematica.travelmate.userinterface.color.ColorPickerDialogFragment;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;
import com.telematica.travelmate.utilities.Constants;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EntryEditorFragment extends Fragment
    implements AddEntryContract.View{

    private int mColor = 0;
    private long categoryId;
    private Category mSelectedCategory = null;
    private CategoryDialogFragment selectCategoryDialog;

    private final static String LOG_TAG = AddEntryActivity.class.getSimpleName();





    @BindView(R.id.edit_text_category) EditText mCategory;
    @BindView(R.id.edit_text_title) EditText mTitle;
    @BindView(R.id.edit_text_entry) EditText mContent;


    @Inject Bus mBus;
    @Inject Context mContext;

    private AddEntryPresenter mPresenter;




    private View mRootView;
    private boolean showLinedEditor = false;
    private static final String LOG_CAT = EntryEditorFragment.class.getSimpleName();


    public EntryEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        showLinedEditor = PreferenceManager
                .getDefaultSharedPreferences(getContext()).getBoolean("default_editor", true);
        Log.d(LOG_CAT, "Line Editor Enabled ?: " + showLinedEditor);
    }

    public static EntryEditorFragment newInstance(long entryId){

        EntryEditorFragment fragment = new EntryEditorFragment();

        if (entryId > 0){
            Bundle args = new Bundle();
            args.putLong(Constants.ENTRY_ID, entryId);
            fragment.setArguments(args);

        }

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (showLinedEditor){
            mRootView = inflater.inflate(R.layout.fragment_lined_editor, container, false);
        }else {
            mRootView = inflater.inflate(R.layout.fragment_plain_editor, container, false);
        }
        ButterKnife.bind(this, mRootView);
        TravelMateApplication.getInstance().getAppComponent().inject(this);
        try {
            mBus.register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mRootView;
    }

    @OnClick(R.id.edit_text_category)
    public void showSelectCategory(){
        mPresenter.onSelectCategory();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null && getArguments().containsKey(Constants.ENTRY_ID)) {
            long entryId = getArguments().getLong(Constants.ENTRY_ID, 0);
            mPresenter = new AddEntryPresenter(this, entryId);
        }else {
            mPresenter = new AddEntryPresenter(this, 0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.checkStatus();
       
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_entry, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                validateAndSaveContent();
                break;
            case android.R.id.home:
                mPresenter.saveOnExit(mTitle.getText().toString(),mCategory.getText().toString(),
                        mContent.getText().toString(), mColor);
                break;
            case R.id.action_delete:
                mPresenter.onDeleteEntryButtonClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void populateEntry(Entry entry) {

        mTitle.setText(entry.getTitle());
        mTitle.setHint(R.string.edit_title);
        mCategory.setText(entry.getCategoryName());
        mContent.setText(entry.getContent());
        mContent.setHint(R.string.edit_entry);
        categoryId = entry.getCategoryId();
    }



    @Override
    public void displayCategory(String category) {
        mCategory.setText(category);
    }


    @Override
    public void displayDeleteConfirmation(Entry entry) {
        boolean shouldPromptForDelete = PreferenceManager
                .getDefaultSharedPreferences(getContext()).getBoolean("prompt_for_delete", true);
        if (shouldPromptForDelete) {
            promptForDelete(entry);
        } else {
            mPresenter.deleteEntry();
            displayPreviousActivity();
        }
    }

    @Override
    public void displayDiscardConfirmation() {
        promptForDiscard();
    }

    @Override
    public void displayPreviousActivity() {
        startActivity(new Intent(getActivity(), EntryListActivity.class));
    }

    @Override
    public void displayMessage(String message) {
        makeToast(message);
    }



    @Override
    public void displayShareIntent() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, mTitle.getText().toString());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, mContent.getText().toString());
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));

    }

    @Override
    public void showChooseCategoryDialog(List<Category> categories) {
        selectCategoryDialog = CategoryDialogFragment.newInstance();
        selectCategoryDialog.setCategories(categories);

        selectCategoryDialog.setCategorySelectedListener(new OnCategorySelectedListener() {
            @Override
            public void onCategorySelected(Category selectedCategory) {
                selectCategoryDialog.dismiss();
                displayCategory(selectedCategory.getCategoryName());
                categoryId = selectedCategory.getId();
            }

            @Override
            public void onEditCategoryButtonClicked(Category selectedCategory) {

            }

            @Override
            public void onDeleteCategoryButtonClicked(Category selectedCategory) {

            }
        });
        selectCategoryDialog.show(getActivity().getFragmentManager(), "Dialog");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBus != null){
            mBus.unregister(this);
        }
    }

 


    private void promptForDelete(Entry entry){
        String title = "Delete " + entry.getTitle();
        String message =  "Are you sure you want to delete entry " + entry.getTitle() + "?";


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        titleText.setText(title);
        alertDialog.setCustomTitle(titleView);

        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.deleteEntry();
                displayPreviousActivity();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void promptForDiscard(){
        String title = "Discard Entry";
        String message =  "Are you sure you want to discard entry ";


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        titleText.setText(title);
        alertDialog.setCustomTitle(titleView);

        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetFields();
                displayPreviousActivity();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void resetFields() {
        mCategory.setText("");
        mTitle.setText("");
        mContent.setText("");
    }


    private void makeToast(String message){
        Snackbar snackbar = Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG);

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
        TextView tv = (TextView)snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    private void validateAndSaveContent(){
        String category = mCategory.getText().toString();
        if (TextUtils.isEmpty(category)){
            mCategory.setText(Constants.DEFAULT_CATEGORY);
            category = mCategory.getText().toString();
        }

        String title = mTitle.getText().toString();
        if (TextUtils.isEmpty(title)){
            mTitle.setError(getString(R.string.title_is_required));
            return;
        }

        String content = mContent.getText().toString();
        if (TextUtils.isEmpty(content)){
            mContent.setError(getString(R.string.entry_is_required));
            return;
        }

        mPresenter.onAddClick(title, category, content, mColor);


    }


}
