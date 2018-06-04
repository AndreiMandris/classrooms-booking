package com.etti.classroomsbooking.util;

import com.etti.classroomsbooking.model.Classroom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static final List<String> ACCEPTED_QR_CODES = Arrays.asList(new String[]{"classroom-0", "classroom-1", "classroom-2", "classroom-3", "classroom-4", "classroom-5"});

    static {
        HashMap<Integer, Classroom> classrooms = new HashMap<>();
        classrooms.put(0, new Classroom(0, "A01"));
        classrooms.put(1, new Classroom(1, "A02"));
        classrooms.put(2, new Classroom(2, "A03"));
        classrooms.put(4, new Classroom(3, "B01"));
        classrooms.put(5, new Classroom(4, "B02"));
        classrooms.put(6, new Classroom(5, "B03"));
    }
}
