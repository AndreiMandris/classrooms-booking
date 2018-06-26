package com.etti.classroomsbooking.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class TimeFrame implements Serializable, Comparable<TimeFrame>{
    private String userName;
    private int idClassroom;
    private double startTime;
    private double endTime;
    private String date;

    public TimeFrame() {
    }

    public TimeFrame(String userName, int idClassroom, double startTime, double endTime, String date) {
        this.userName = userName;
        this.idClassroom = idClassroom;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public TimeFrame(String userName, double startTime, double endTime, String date) {
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
        TimeFrame timeFrame = (TimeFrame) o;
        return idClassroom == timeFrame.idClassroom &&
                Double.compare(timeFrame.startTime, startTime) == 0 &&
                Double.compare(timeFrame.endTime, endTime) == 0 &&
                Objects.equals(userName, timeFrame.userName) &&
                Objects.equals(date, timeFrame.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, idClassroom, startTime, endTime, date);
    }

    @Override
    public int compareTo(@NonNull TimeFrame timeFrame) {
        if (this.startTime < timeFrame.startTime){
            return 1;
        } else if(this.startTime > timeFrame.startTime){
            return -1;
        } else{
            return 0;
        }
    }
}
