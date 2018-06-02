package com.etti.classroomsbooking.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.etti.classroomsbooking.util.Constant.getStringDateFromTimeMillis;

public class Classroom implements Serializable{
    private int id;
    private String name;
    private ArrayList<TimeLapse> intervals;

    public Classroom(int id, String name) {
        this.id = id;
        this.name = name;
        intervals = new ArrayList<>();
    }

    public Classroom(int id, String name, ArrayList<TimeLapse> intervals) {
        this.id = id;
        this.name = name;
        this.intervals = intervals;
    }

    public Classroom() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TimeLapse> getIntervals() {
        return intervals;
    }

    public ArrayList<TimeLapse> getIntervalsByDate(long dateInMillis) {

        return intervals;
    }

    public void setIntervals(ArrayList<TimeLapse> intervals) {
        this.intervals = intervals;
    }

    public void bookClassroom(double startTime, double endTime, String user, long currentDateInMillis) {
        this.intervals.add(new TimeLapse(user, this.getId(), startTime, endTime, getStringDateFromTimeMillis(currentDateInMillis)));
    }

    public boolean[] checkAvailability(String selectedDate) {
        if (this.getIntervals() == null){
            this.intervals = new ArrayList<>();
        }
        boolean[] disabledHours = new boolean[48];
        for (TimeLapse interval : this.getIntervals()) {
            if (interval.getDate().equals(selectedDate)) {
                for (double i = 0; i < 24; i += 0.5) {
                    if (interval.getStartTime() <= i && i < interval.getEndTime()) {
                        disabledHours[(int) (i * 2)] = true;
                    }
                }
            }
        }
        return disabledHours;
    }

    public boolean[] checkAvailabilityE(String selectedDate, double startPickedTime) {
        boolean[] disabledHours = new boolean[48];
        for (TimeLapse interval : this.getIntervals()) {
            if (interval.getDate().equals(selectedDate)) {
                for (double i = 0; i <= 24; i += 0.5) {
                    if (i > interval.getStartTime() && interval.getStartTime() > startPickedTime) {
                        disabledHours[(int) (i * 2)] = true;
                    }
                    if (interval.getStartTime() < i && i <= interval.getEndTime()) {
                        disabledHours[(int) (i * 2)] = true;
                    }
                }
            }
        }
        return disabledHours;
    }

    public void sortIntervals(){
        getIntervals().sort((timeLapse1, timeLapse2) -> {
            if (timeLapse1.getStartTime() < timeLapse2.getStartTime()){
                return -1;
            } else if(timeLapse1.getStartTime() > timeLapse2.getStartTime()){
                return 1;
            } else{
                return 0;
            }
        });
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", intervals=" + intervals +
                '}';
    }
}
