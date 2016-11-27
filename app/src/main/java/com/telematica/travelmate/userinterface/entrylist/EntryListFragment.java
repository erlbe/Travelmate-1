package com.telematica.travelmate.userinterface.entrylist;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.telematica.travelmate.R;
import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.listeners.EntryItemListener;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.entryadd.AddEntryActivity;
import com.telematica.travelmate.userinterface.entrydetail.EntryDetailActivity;
import com.telematica.travelmate.utilities.Constants;
import com.squareup.otto.Bus;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EntryListFragment extends Fragment implements EntryListContract.View {

    private EntryListContract.Actions mPresenter;
    private EntryListAdapter mListAdapter;


    @Inject Bus mBus;
    @Inject SharedPreferences mSharedPreference;

    @BindView(R.id.entry_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.empty_text) TextView mEmptyText;

    private View mRootView;
    private FloatingActionButton mFab;


    public EntryListFragment() {
        // Required empty public constructor
    }

    public static EntryListFragment newInstance(boolean dualScreen){
        EntryListFragment fragment = new EntryListFragment();

        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_DUAL_SCREEN, dualScreen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mListAdapter = EntryListAdapter.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_entry_list, container, false);

        ButterKnife.bind(this, mRootView);
        TravelMateApplication.getInstance().getAppComponent().inject(this);
        mBus.register(this);

        mPresenter = new EntryListPresenter(this);

        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .color(ContextCompat.getColor(getContext(), R.color.secondary_text))
                        .sizeResId(R.dimen.dividerHeight)
                        .build());

        mListAdapter.setEntryItemListener(new EntryItemListener() {
            @Override
            public void onEntryClick(Entry clickedEntry) {
                mPresenter.openEntryDetails(clickedEntry.getId());
            }

            @Override
            public void onDeleteButtonClicked(Entry clickedEntry) {
                mPresenter.onDeleteEntryButtonClicked(clickedEntry);
            }
        });

        mFab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onAddNewEntryButtonClicked();
            }
        });
        return mRootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args  != null && args.containsKey(Constants.IS_DUAL_SCREEN)){
            mPresenter.setLayoutMode(args.getBoolean(Constants.IS_DUAL_SCREEN));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadEntries();

    }



    @Override
    public void showEntries(List<Entry> entries) {
        mListAdapter.replaceData(entries);
    }

    @Override
    public void showAddEntry() {
        startActivity(new Intent(getActivity(), AddEntryActivity.class));
    }

    @Override
    public void displaySortOptions() {
        final  String[] sortOptions = { getString(R.string.title), getString(R.string.sort_by_category), getString(R.string.date_last_modified)};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
        alertDialog.setView(convertView);

        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        titleText.setText(R.string.sort_title);
        alertDialog.setCustomTitle(titleView);

        ListView dialogList = (ListView) convertView.findViewById(R.id.dialog_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(),android.R.layout.simple_list_item_1, sortOptions);
        dialogList.setAdapter(adapter);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final Dialog dialog = alertDialog.create();
        dialog.show();
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = mSharedPreference.edit();
                switch (position){
                    case 0:
                        mListAdapter.Sort(Constants.SORT_TITLE);
                        editor.putInt(Constants.SORT_PREFERENCE, Constants.SORT_TITLE).commit();
                        dialog.dismiss();
                        break;
                    case 1:
                        mListAdapter.Sort(Constants.SORT_CATEGORY);
                        editor.putInt(Constants.SORT_PREFERENCE, Constants.SORT_CATEGORY).commit();
                        dialog.dismiss();
                        break;
                    case 2:
                        mListAdapter.Sort(Constants.SORT_DATE);
                        editor.putInt(Constants.SORT_PREFERENCE, Constants.SORT_DATE).commit();
                        dialog.dismiss();
                        break;
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_entry_list, menu);
         super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort){
            //inform the Presenter that Entry sort is requested
            mPresenter.showSortOptions();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSingleDetailUi(long entryId) {
        startActivity(EntryDetailActivity.getStartIntent(getContext(), entryId));
    }

    @Override
    public void showDualDetailUi(Entry entry) {
        EntryListActivity activity = (EntryListActivity)getActivity();
        activity.showTwoPane(entry);

    }

    @Override
    public void showEmptyText(boolean showText) {
        if (showText){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyText.setVisibility(View.VISIBLE);
        }else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public void showDeleteConfirmation(Entry entry) {
        boolean shouldPromptForDelete = PreferenceManager
                .getDefaultSharedPreferences(getContext()).getBoolean("prompt_for_delete", true);
        if (shouldPromptForDelete) {
            promptForDelete(entry);
        } else {
            mPresenter.deleteEntry(entry);
        }
    }

    /**
     * Displays any message passed from the Presenter
     * @param message
     */
    @Override
    public void showMessage(String message) {
        makeToast(message);
    }

    @Override
    public void applySortPreference(int sortPreference) {
        mListAdapter.Sort(sortPreference);
    }

    private void makeToast(String message){
        Snackbar snackbar = Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG);

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
        TextView tv = (TextView)snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public void promptForDelete(final Entry entry){
        String title = "Delete " + entry.getTitle();
        String content = entry.getContent();
        String message =  "Delete " + content.substring(0, Math.min(content.length(), 50)) + "  ... ?";


        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View titleView = (View)inflater.inflate(R.layout.dialog_title, null);
        TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
        titleText.setText(title);
        alertDialog.setCustomTitle(titleView);

        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.deleteEntry(entry);
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
}
