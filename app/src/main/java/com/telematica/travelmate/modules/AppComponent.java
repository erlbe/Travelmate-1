package com.telematica.travelmate.modules;

import com.telematica.travelmate.userinterface.entryadd.AddEntryActivity;
import com.telematica.travelmate.userinterface.entryadd.AddEntryPresenter;
import com.telematica.travelmate.userinterface.entryadd.EntryEditorFragment;
import com.telematica.travelmate.userinterface.category.AddEditCategoryDialogFragment;
import com.telematica.travelmate.userinterface.category.CategoryDialogFragment;
import com.telematica.travelmate.userinterface.category.CategoryListPresenter;
import com.telematica.travelmate.userinterface.entrydetail.EntryDetailActivity;
import com.telematica.travelmate.userinterface.entrydetail.EntryDetailPresenter;
import com.telematica.travelmate.userinterface.entrylist.EntryListFragment;
import com.telematica.travelmate.userinterface.entrylist.EntryListPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger for injecting fragments, presenter and activies at onCreate of App
 */

@Singleton
@Component(
        modules = {
                AppModule.class,
                BusModule.class,
                PersistenceModule.class
        }
)

public interface AppComponent {
        void inject(AddEditCategoryDialogFragment fragment);
        void inject(CategoryListPresenter presenter);
        void inject(EntryListFragment fragment);
        void inject(EntryListPresenter presenter);
        void inject(AddEntryPresenter presenter);
        void inject(EntryDetailPresenter presenter);
        void inject(EntryDetailActivity activity);
        void inject(AddEntryActivity activity);
        void inject(CategoryDialogFragment fragment);
        void inject(EntryEditorFragment fragment);


}
