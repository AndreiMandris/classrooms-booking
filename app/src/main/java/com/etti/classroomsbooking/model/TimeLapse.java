package com.etti.classroomsbooking.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class TimeLapse implements Serializable, Comparable<TimeLapse> {
    private String userName;
    private int idClassroom;
    private double startTime;
    private double endTime;
    private String date;

    public TimeLapse() {
    }

    public TimeLapse(String userName, int idClassroom, double startTime, double endTime, String date) {
        this.userName = userName;
        this.idClassroom = idClassroom;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public TimeLapse(String userName, double startTime, double endTime, String date) {
        this.userName = userName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeLapse timeLapse = (TimeLapse) o;
        return idClassroom == timeLapse.idClassroom &&
                Double.compare(timeLapse.startTime, startTime) == 0 &&
                Double.compare(timeLapse.endTime, endTime) == 0 &&
                Objects.equals(userName, timeLapse.userName) &&
                Objects.equals(date, timeLapse.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, idClassroom, startTime, endTime, date);
    }

    @Override
    public int compareTo(@NonNull TimeLapse timeLapse) {
        if (this.startTime < timeLapse.startTime) {
            return 1;
        } else if (this.startTime > timeLapse.startTime) {
            return -1;
        } else {
            return 0;
        }
    }
}
