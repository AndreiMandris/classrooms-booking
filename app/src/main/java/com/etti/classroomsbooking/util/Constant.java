package com.etti.classroomsbooking.util;

import com.etti.classroomsbooking.model.Classroom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constant {
    public static final String DATE_IN_MILLIS = "DATE_IN_MILLIS";
    public static final String ROOM = "ROOM";

    static {
        HashMap<Integer, Classroom> classrooms = new HashMap<>();
        classrooms.put(0, new Classroom(new BigDecimal(0), "A01"));
        classrooms.put(1, new Classroom(new BigDecimal(1), "A02"));
        classrooms.put(2, new Classroom(new BigDecimal(2), "A03"));
        classrooms.put(4, new Classroom(new BigDecimal(3), "A05"));
        classrooms.put(5, new Classroom(new BigDecimal(4), "A06"));
    }

    static {
        List<Classroom> classRoomsList = new ArrayList<>();
        classRoomsList.add(new Classroom(new BigDecimal(0), "A01"));
        classRoomsList.add(new Classroom(new BigDecimal(1), "A02"));
        classRoomsList.add(new Classroom(new BigDecimal(2), "A03"));
    }
}
