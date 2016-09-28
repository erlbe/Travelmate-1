package com.telematica.travelmate.data;

import com.telematica.travelmate.model.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Adds some Sample Entries when the App runs the first time
 * Methods:
 * getSampleEntries
 */
public class SampleData{


    public static List<Entry> getSampleEntries(){

        List<Entry> entries = new ArrayList<>();


        //create the dummy entry
        Entry entry1 = new Entry();
        entry1.setId((long) 1);
        entry1.setTitle("Hi");
        entry1.setContent("Hello friend, I'm your TravelMate.\n" +
                "I accompany you on your travels and save for you all your experiences and impressions. With me you will never forget what you have experienced on your journeys. ");
        Calendar calendar1 = GregorianCalendar.getInstance();
        entry1.setDateModified(calendar1.getTimeInMillis());
        entry1.setCategoryName("General");
        entries.add(entry1);




        //create the dummy entry
        Entry entry2 = new Entry();
        entry2.setId((long) 2);
        entry2.setTitle("Bread and Butter in Berlin");
        entry2.setContent("A text about Berlin");

        //change the date to random time
        Calendar calendar2 = GregorianCalendar.getInstance();
        calendar2.add(Calendar.DAY_OF_WEEK, -1);
        calendar2.add(Calendar.MILLISECOND, 10005623);
        entry2.setDateModified(calendar2.getTimeInMillis());
        entry2.setCategoryName("Germany");
        entries.add(entry2);




        //create the dummy entry
        Entry entry3 = new Entry();
        entry3.setId((long) 3);
        entry3.setTitle("Nobel Price Ceremony in Oslo");
        entry3.setContent("A text about Oslo");


        //change the date to random time
        Calendar calendar3 = GregorianCalendar.getInstance();
        calendar3.add(Calendar.DAY_OF_WEEK, -2);
        calendar3.add(Calendar.MILLISECOND, 8962422);
        entry3.setCategoryName("Norway");
        entry3.setDateModified(calendar3.getTimeInMillis());
        entries.add(entry3);


        return entries;
    }



}
