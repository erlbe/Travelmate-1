package com.telematica.travelmate.userinterface.entrylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.telematica.travelmate.R;
import com.telematica.travelmate.application.AndroidDatabaseManager;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.account.AccountActivity;
import com.telematica.travelmate.userinterface.settings.SettingsActivity;
import com.telematica.travelmate.userinterface.backup.BackupActivity;
import com.telematica.travelmate.userinterface.category.CategoryListActivity;
import com.telematica.travelmate.userinterface.entrydetail.EntryDetailFragment;
import com.telematica.travelmate.utilities.Constants;

public class EntryListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AccountHeader headerResult;
    private Drawer drawer;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavigationDrawer(savedInstanceState);


        if (findViewById(R.id.entry_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w800dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            findViewById(R.id.entry_detail_container).setVisibility(View.GONE);
        }

        openFragment(EntryListFragment.newInstance(mTwoPane), "Entries");

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

    private void openDetailFragment(Fragment fragment, String screenTitle){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.entry_detail_container, fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(screenTitle);
    }


    private void setupNavigationDrawer(Bundle savedInstanceState) {
        //Create Navigation drawer Account Header
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header3)
                .build();

        drawer = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Entries").withIcon(FontAwesome.Icon.faw_sticky_note).withIdentifier(Constants.ENTRY),
                        new PrimaryDrawerItem().withName("Categories").withIcon(FontAwesome.Icon.faw_folder).withIdentifier(Constants.CATEGORY),
                        new PrimaryDrawerItem().withName("Profile").withIcon(FontAwesome.Icon.faw_user).withIdentifier(Constants.ACCOUNT),
                        new PrimaryDrawerItem().withName("Save to SD Card").withIcon(FontAwesome.Icon.faw_download).withIdentifier(Constants.BACKUP),
                        new PrimaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(Constants.SETTINGS),
                        new PrimaryDrawerItem().withName("Database").withIcon(FontAwesome.Icon.faw_table).withIdentifier(Constants.TABLE)
                )
                .withOnDrawerItemClickListener(new com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem drawerItem) {
                        if (drawerItem != null && drawerItem instanceof Nameable){
                            String name = ((Nameable)drawerItem).getName().getText(EntryListActivity.this);
                            toolbar.setTitle(name);
                        }

                        if (drawerItem != null){
                            //handle on navigation drawer item
                            onTouchDrawer((int) drawerItem.getIdentifier());
                        }
                        return false;
                    }


                })
                .withOnDrawerListener(new com.mikepenz.materialdrawer.Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {
                        KeyboardUtil.hideKeyboard(EntryListActivity.this);

                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }

                    @Override
                    public void onDrawerSlide(View view, float v) {

                    }
                })
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();



    }

    /**
     * Handles the selection of an item in the Navigation drawer
     * @param position of the selected item in the Navigation drawer
     */
    private void onTouchDrawer(final int position) {
        switch (position){
            case Constants.ENTRY:
                  break;
            case Constants.BACKUP:
                startActivity(new Intent(EntryListActivity.this, BackupActivity.class));
                break;
            case Constants.SETTINGS:
                startActivity(new Intent(EntryListActivity.this, SettingsActivity.class));
                break;
            case Constants.CATEGORY:
                startActivity(new Intent(EntryListActivity.this, CategoryListActivity.class));
                break;
            case Constants.ACCOUNT:
                startActivity(new Intent(EntryListActivity.this, AccountActivity.class));
                break;
            case Constants.TABLE:
                startActivity(new Intent(EntryListActivity.this, AndroidDatabaseManager.class));
                break;

        }

    }

    public void showTwoPane(Entry entry){
        findViewById(R.id.entry_detail_container).setVisibility(View.VISIBLE);
        openDetailFragment(EntryDetailFragment.newInstance(entry.getId()), entry.getTitle());


    }

}
