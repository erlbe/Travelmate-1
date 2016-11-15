package com.telematica.travelmate.userinterface.entrylist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telematica.travelmate.R;
import com.telematica.travelmate.data.EntryService;
import com.telematica.travelmate.listeners.EntryItemListener;
import com.telematica.travelmate.model.Entry;
import com.telematica.travelmate.utilities.EntryComparer;
import com.telematica.travelmate.utilities.Constants;
import com.telematica.travelmate.utilities.TimeUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;




public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.ViewHolder> {
    private List<Entry> mEntries;
    private EntryItemListener mItemListener;
    private View entryView;


    public EntryListAdapter(List<Entry> entries){
        mEntries = entries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        entryView = inflater.inflate(R.layout.custom_row_entry_list, parent, false);
        return new ViewHolder(entryView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Entry entry = mEntries.get(position);

        holder.title.setText(entry.getTitle());
        holder.entryDate.setText(TimeUtils.getTimeAgo(entry.getDateModified()));


        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onEntryClick(entry);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onDeleteButtonClicked(entry);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    public Entry getItem(int position) {
        return mEntries.get(position);
    }

    public void replaceData(List<Entry> entries) {
<<<<<<< HEAD
        setList(entries);
        // ERLENDS VERSION TO LOAD IT FROM ONLINE DATABASE:
        // EntryService.loadAllEntries(this);
=======
        //setList(entries);
        EntryService.loadAllEntries(this);
>>>>>>> 565b42ad4413d2bc6035d9cc001adb9224c80ce7
    }

    public void setList(List<Entry> entries) {
        mEntries = entries;
        notifyDataSetChanged();
    }

    public void setEntryItemListener(EntryItemListener listener){
        mItemListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text_view_entry_title) TextView title;
        @BindView(R.id.text_view_entry_date) TextView entryDate;
        @BindView(R.id.image_view_delete)  ImageView delete;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Entry entry = getItem(position);
            mItemListener.onEntryClick(entry);

        }
    }


    public void Sort(int sortMethod){
        switch (sortMethod){
            case Constants.SORT_TITLE:
                Collections.sort(mEntries, EntryComparer.TitleComparer.getsInstance());
                break;
            case Constants.SORT_CATEGORY:
                Collections.sort(mEntries, EntryComparer.CategoryComparer.getsInstance());
                break;
            case Constants.SORT_DATE:
                Collections.sort(mEntries, EntryComparer.LastUpdateComparer.getsInstance());
                break;
            default:
                Collections.sort(mEntries, EntryComparer.LastUpdateComparer.getsInstance());
                break;
        }
        notifyItemRangeChanged(0, mEntries.size());
    }




}