package com.etti.classroomsbooking.model;

import java.math.BigDecimal;

public class User {
    private String userName;

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
    }

    private String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
