package com.etti.classroomsbooking;

public class EventDetails {
    private String timeInterval;
    private String userName;
    private boolean checkBox;

    public EventDetails(String timeInterval, String userName, boolean checkBox) {
        this.timeInterval = timeInterval;
        this.userName = userName;
        this.checkBox = checkBox;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }
}
