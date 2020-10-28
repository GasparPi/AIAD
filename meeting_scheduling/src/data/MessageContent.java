package data;

import behaviours.SchedulingState;

import java.io.Serializable;


public class MessageContent implements Serializable {
    private SchedulingState state;
    private String day;
    private int timeslot;
    private int employeeId;
    private int meetingDuration;
    private boolean acceptance;

    public MessageContent(){
        state = SchedulingState.NULL;
        day = "";
        timeslot = -1;
        employeeId = -1;
        meetingDuration = -1;
        acceptance = false;
    }

    public void setState(SchedulingState state) {
        this.state = state;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setMeetingDuration(int meetingDuration) {
        this.meetingDuration = meetingDuration;
    }

    public void setAcceptance(boolean acceptance) {
        this.acceptance = acceptance;
    }

    public SchedulingState getState() {
        return state;
    }

    public String getDay() {
        return day;
    }

    public int getTimeslot() {
        return timeslot;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public int getMeetingDuration() {
        return meetingDuration;
    }

    public boolean getAcceptance() {
        return acceptance;
    }
}
