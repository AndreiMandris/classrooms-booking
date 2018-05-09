package com.etti.classroomsbooking.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class TimeLapse {
    public BigDecimal id;
    public BigDecimal idUser;
    public BigDecimal idClassroom;
    public int timeStart;
    public int numberOfPeriods;
    public Date date;

    public TimeLapse(BigDecimal idUser, BigDecimal idClassroom, int timeStart, int numberOfPeriods, Date date) {
        this.idUser = idUser;
        this.idClassroom = idClassroom;
        this.timeStart = timeStart;
        this.numberOfPeriods = numberOfPeriods;
        this.date = date;
    }

    public BigDecimal getId() {
        return id;
    }

    
}
