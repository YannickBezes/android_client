package com.example.smartcity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Utility {

    private static ArrayList<CalendarEvent> events = new ArrayList<>();

    public static ArrayList<CalendarEvent> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, null,
                        null, null);
        events.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if(Long.parseLong(cursor.getString(3)) >= new Date().getTime() && Long.parseLong(cursor.getString(3)) <= new Date().getTime() + 14 * 24 * 60 * 60 * 1000) {
                    events.add(new CalendarEvent(cursor.getString(1), getDate(Long.parseLong(cursor.getString(3))), getDate(Long.parseLong(cursor.getString(4))), cursor.getString(2)));
                }
            } while (cursor.moveToNext());
        }

        return sortByDate(events);
    }

    private static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    private static ArrayList<CalendarEvent> sortByDate(ArrayList<CalendarEvent> events) {
        for (int i=0; i < events.size(); i++) {
            CalendarEvent cursor = events.get(i);
            int pos = i;

            while (pos > 0 && new Date(events.get(i - 1).startDate).getTime() > new Date(cursor.startDate).getTime()) {
                // Swap
                events.set(i, events.get(i - 1));
                pos -= 1;
            }
            events.set(pos, cursor);
        }

        return events;
    }
}
