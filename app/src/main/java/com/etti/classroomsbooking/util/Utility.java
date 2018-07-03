package com.etti.classroomsbooking.util;

import android.support.annotation.NonNull;

import com.etti.classroomsbooking.model.TimeLapse;

import java.util.Calendar;

public class Utility {
    public static String getStringDateFromTimeMillis(long currentDateInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDateInMillis);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String date = "" + formatTime(day) + "-" + formatTime(month) + "-" + year;
        return date;
    }

    public static String formatTime(int x) {
        if ((x < -9 && x > -100) || (x < 100 && x > 9)) {
            return "" + x;
        } else {
            return "0" + x;
        }
    }

    @NonNull
    public static String getFormattedTimeInterval(TimeLapse interval) {
        double startTime = interval.getStartTime();
        double endTime = interval.getEndTime();
        String startTimeFormatted = formatTime((int) startTime) + ":" + (startTime % 1 == 0.0 ? "00" : "30");
        String endTimeFormatted = formatTime((int) endTime) + ":" + (endTime % 1 == 0.0 ? "00" : "30");
        return "" + startTimeFormatted + " - " + endTimeFormatted;
    }
}
