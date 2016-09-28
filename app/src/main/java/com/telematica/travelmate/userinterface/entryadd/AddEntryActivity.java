package com.telematica.travelmate.userinterface.entryadd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.telematica.travelmate.R;
import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.userinterface.entrylist.EntryListContract;
import com.telematica.travelmate.utilities.Constants;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class AddEntryActivity extends AppCompatActivity {

    @Inject
    EntryListContract.Repository entryRepository;

    @Inject
    Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_goleft);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TravelMateApplication.getInstance().getAppComponent().inject(this);

        if (getIntent().hasExtra(Constants.ENTRY_ID)){
            long entryId = getIntent().getLongExtra(Constants.ENTRY_ID, 0);
            String screenTitle = entryRepository.getEntryById(entryId).getTitle();
            openFragment(EntryEditorFragment.newInstance(entryId), screenTitle);
        }else {
            openFragment(EntryEditorFragment.newInstance(0), "Entry Editor");
        }


    }


    private void openFragment(Fragment fragment, String screenTitle){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(screenTitle);
    }

}
