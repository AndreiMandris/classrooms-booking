package com.etti.classroomsbooking.util;

import com.etti.classroomsbooking.model.Classroom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Constant {
    public static final String DATE_IN_MILLIS = "DATE_IN_MILLIS";
    public static final String ROOM = "ROOM";
    public static final String ROOM_POSITION = "ROOM_POSITION";
    public static final String USER = "USER";
    public static final String EVENT_CHECK_BOX = "EVENT_CHECK_BOX";
    public static final String TIME_INTERVAL = "TIME_INTERVAL";
    public static final int REQUEST_CODE_QR = 2984;

    static {
        HashMap<Integer, Classroom> classrooms = new HashMap<>();
        classrooms.put(0, new Classroom(0, "A01"));
        classrooms.put(1, new Classroom(1, "A02"));
        classrooms.put(2, new Classroom(2, "A03"));
        classrooms.put(4, new Classroom(3, "B01"));
        classrooms.put(5, new Classroom(4, "B02"));
        classrooms.put(6, new Classroom(5, "B03"));
    }

    public static String getStringDateFromTimeMillis(long currentDateInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDateInMillis);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String date = "" + day + "-" + month + "-" + year;
        return date;
    }

    public static String formatTime(int x){
        if ((x > 9 && x < 100) || (x < -9 && x > -100)){
            return "" + x;
        }else {
            return "0" + x;
        }
    }
}
