package com.telematica.travelmate.userinterface.entrydetail;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.telematica.travelmate.R;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.entryadd.AddEntryActivity;
import com.telematica.travelmate.utilities.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EntryDetailFragment extends Fragment implements EntryDetailContract.View{

    private View mRootView;
    @BindView(R.id.edit_text_title) EditText mTitle;
    @BindView(R.id.edit_text_entry) EditText mContent;
    @BindView(R.id.edit_text_category) EditText mCategory;

    private EntryDetailPresenter mPresenter;


    private boolean showLinedEditor = false;




    public EntryDetailFragment() {
        // Required empty public constructor
    }

    public static EntryDetailFragment newInstance(long entryId){
        EntryDetailFragment fragment = new EntryDetailFragment();

        Bundle args = new Bundle();
        args.putLong(Constants.ENTRY_ID, entryId);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        showLinedEditor = PreferenceManager
                .getDefaultSharedPreferences(getContext()).getBoolean("default_editor", true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (showLinedEditor){
            mRootView = inflater.inflate(R.layout.fragment_lined_editor, container, false);
        }else {
            mRootView = inflater.inflate(R.layout.fragment_plain_editor, container, false);
        }

        ButterKnife.bind(this, mRootView);

        displayReadOnlyViews();
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null && getArguments().containsKey(Constants.ENTRY_ID)) {
            long entryId = getArguments().getLong(Constants.ENTRY_ID, 0);
            mPresenter = new EntryDetailPresenter(this, entryId);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
       getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        mPresenter.showEntryDetails();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_entry_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_edit:
                mPresenter.onEditEntryClick();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayEntry(Entry entry) {
        mCategory.setText(entry.getCategoryName());
        mContent.setText(entry.getContent());
        mTitle.setText(entry.getTitle());
        mCategory.setTextColor(Color.BLACK);
        mContent.setTextColor(Color.BLACK);
        if (entry.getColor() != 0){
            mCategory.setBackgroundColor(entry.getColor());
            mCategory.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void showEditEntryScreen(long entryId) {
        Intent startEditEntryIntent = new Intent(getActivity(), AddEntryActivity.class);
        startEditEntryIntent.putExtra(Constants.ENTRY_ID, entryId);
        startActivity(startEditEntryIntent);
    }

    @Override
    public void showDeleteConfirmation(Entry entry) {
        promptForDelete(entry);
    }

    @Override
    public void displayBackgroundColor() {

    }

    @Override
    public void displayPreviousActivity() {
        getActivity().onBackPressed();
    }

    @Override
    public void displayMessage(String message) {
        makeToast(message);
    }

    @Override
    public void displayReadOnlyViews() {
        mCategory.setEnabled(false);
        mTitle.setFocusable(false);
        mContent.setEnabled(false);
    }

    public void promptForDelete(Entry entry){
        final String titleOfEntryTobeDeleted = entry.getTitle();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        //alertDialog.setTitle("Delete " + titleOfEntryTobeDeleted + " ?");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        titleText.setText("Delete " + titleOfEntryTobeDeleted + " ?");
        alertDialog.setCustomTitle(titleView);


        alertDialog.setMessage("Are you sure you want to delete the entry " + titleOfEntryTobeDeleted + "?");
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

    private void makeToast(String message){
        Snackbar snackbar = Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG);

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
        TextView tv = (TextView)snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
