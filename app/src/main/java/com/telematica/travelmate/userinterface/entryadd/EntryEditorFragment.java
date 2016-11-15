package com.telematica.travelmate.userinterface.entryadd;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.telematica.travelmate.R;
import com.telematica.travelmate.application.TravelMateApplication;
import com.telematica.travelmate.listeners.OnCategorySelectedListener;
import com.telematica.travelmate.model.Category;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.userinterface.category.CategoryDialogFragment;
import com.telematica.travelmate.userinterface.entrylist.EntryListActivity;
import com.telematica.travelmate.utilities.Constants;
import com.squareup.otto.Bus;
import com.telematica.travelmate.utilities.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.telematica.travelmate.utilities.Constants.SELECT_PICTURE_REQUEST_CODE;


public class EntryEditorFragment extends Fragment
    implements AddEntryContract.View{

    private long categoryId;
    private Category mSelectedCategory = null;
    private CategoryDialogFragment selectCategoryDialog;
    private Uri outputFileUri;

    private final static String LOG_TAG = AddEntryActivity.class.getSimpleName();





    @BindView(R.id.edit_text_category) EditText mCategory;
    @BindView(R.id.edit_text_title) EditText mTitle;
    @BindView(R.id.edit_text_entry) EditText mContent;
    @BindView(R.id.image_view_entry) ImageView mImage;


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


        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageIntent();
            }
        });
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
        mPresenter.initiateImage();

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
                Bitmap bitmap = ((BitmapDrawable)mImage.getDrawable()).getBitmap();
                mPresenter.saveOnExit(mTitle.getText().toString(),mCategory.getText().toString(),
                        mContent.getText().toString(), bitmap);
                break;
            case R.id.action_delete:
                mPresenter.onDeleteEntryButtonClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void populateImage(Entry entry){
        if (entry.getImage() != null) {
            mImage.setImageBitmap(FileUtils.getImageFromBytes(entry.getImage()));
        }
    }



    @Override
    public void populateEntry(Entry entry) {

        mTitle.setText(entry.getTitle());
        mTitle.setHint(R.string.edit_title);
        mCategory.setText(entry.getCategoryName());
        mContent.setText(entry.getContent());
        mContent.setHint(R.string.edit_entry);
        categoryId = entry.getCategoryId();

        /*if (entry.getImage() != null) {
            mImage.setImageBitmap(FileUtils.getImageFromBytes(entry.getImage()));
        } else {
            mFab.setVisibility(View.VISIBLE);
        }*/
    }


    @Override
    public void deleteImage(Entry entry){
        mImage.setImageBitmap(null);
        entry.setImage(null);
    }

    @Override
    public void addImage(Entry entry, Bitmap bitmap){
        entry.setImage(FileUtils.getBytesFromImage(bitmap));
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

        Bitmap bitmap = ((BitmapDrawable)mImage.getDrawable()).getBitmap();
        mPresenter.onAddClick(title, category, content, bitmap);

    }

    private void openImageIntent() {
    // Determine Uri of camera image to save.
    final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
    root.mkdirs();
    final String fname = FileUtils.getUniqueImageFilename();
    final File sdImageMainDirectory = new File(root, fname);
    outputFileUri = Uri.fromFile(sdImageMainDirectory);

    // Camera.
    final List<Intent> cameraIntents = new ArrayList<Intent>();
    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    final PackageManager packageManager = mContext.getPackageManager();
    final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
    for(ResolveInfo res : listCam) {
        final String packageName = res.activityInfo.packageName;
        final Intent intent = new Intent(captureIntent);
        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
        intent.setPackage(packageName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        cameraIntents.add(intent);
    }

    // Filesystem.
    final Intent galleryIntent = new Intent();
    galleryIntent.setType("image/*");
    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

    // Chooser of filesystem options.
    final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

    // Add the camera options.
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

    startActivityForResult(chooserIntent, SELECT_PICTURE_REQUEST_CODE);
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), selectedImageUri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    //mPresenter.killImage();
                    //mPresenter.putImage(bitmap);
                    mImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
