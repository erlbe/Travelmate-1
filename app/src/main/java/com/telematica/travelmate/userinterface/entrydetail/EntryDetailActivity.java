package com.telematica.travelmate.userinterface.entrydetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.telematica.travelmate.R;
import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.userinterface.entrylist.EntryListContract;
import com.telematica.travelmate.utilities.Constants;

import javax.inject.Inject;

public class EntryDetailActivity extends AppCompatActivity {

    @Inject
    EntryListContract.Repository entryRepository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_goleft);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        TravelMateApplication.getInstance().getAppComponent().inject(this);

        long entryId = getIntent().getLongExtra(Constants.ENTRY_ID, 0);
        String screenTitle = entryRepository.getEntryById(entryId).getTitle();

        openFragment(EntryDetailFragment.newInstance(entryId), screenTitle);


    }

    public static Intent getStartIntent(final Context context, final long entryId) {
        Intent intent = new Intent(context, EntryDetailActivity.class);
        intent.putExtra(Constants.ENTRY_ID, entryId);
        return intent;
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
