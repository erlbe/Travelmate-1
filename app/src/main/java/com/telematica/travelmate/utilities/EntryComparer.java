package com.telematica.travelmate.utilities;

import android.annotation.TargetApi;
import android.os.Build;

import com.telematica.travelmate.model.Entry;

import java.util.Comparator;


public class EntryComparer {
    public static class TitleComparer implements Comparator<Entry> {

        //Singleton instance of this comparator
        private static TitleComparer sInstance = null;

        //Empty constructor
        protected TitleComparer(){}

        public static TitleComparer getsInstance(){
            //create an instance if one does not exist
            if (sInstance == null){
                sInstance = new TitleComparer();
            }
            return sInstance;
        }


        @Override
        public int compare(Entry lhs, Entry rhs) {

            //ensure both Notes have titles
            //Ensure both Services have names
            if (lhs.getTitle() != null && rhs.getTitle() != null){

                //Return a string comparison of the names
                return lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());
            }
            return 0;
        }
    }


    public static class LastUpdateComparer implements Comparator<Entry> {

        //Singleton instance of this comparator
        private static LastUpdateComparer sInstance = null;

        //Empty constructor
        protected LastUpdateComparer(){}

        public static LastUpdateComparer getsInstance(){
            //create an instance if one does not exist
            if (sInstance == null){
                sInstance = new LastUpdateComparer();
            }
            return sInstance;
        }


        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(Entry lhs, Entry rhs) {

            //ensure both Notes have titles
            //Ensure both Services have names
            if (lhs.getDateModified() > -1 && rhs.getDateModified() > -1){

                //Return a string comparison of the names
                return Long.compare(lhs.getDateModified(), rhs.getDateModified());
            }
            return 0;
        }
    }





    public static class CategoryComparer implements Comparator<Entry> {

        //Singleton instance of this comparator
        private static CategoryComparer sInstance = null;

        //Empty constructor
        protected CategoryComparer(){}

        public static CategoryComparer getsInstance(){
            //create an instance if one does not exist
            if (sInstance == null){
                sInstance = new CategoryComparer();
            }
            return sInstance;
        }


        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(Entry lhs, Entry rhs) {

             if (lhs.getCategoryId() > -1  && rhs.getCategoryId() > -1){

                //Return a string comparison of the names
                return Long.compare(lhs.getCategoryId(), rhs.getCategoryId());
            }
            return 0;
        }
    }


}
