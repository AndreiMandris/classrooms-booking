package com.etti.classroomsbooking.model;

import java.math.BigDecimal;

public class Classroom {
    public BigDecimal id;
    public String name;

    public Classroom(BigDecimal id, String name) {
        this.id = id;
        this.name = name;
    }
}
