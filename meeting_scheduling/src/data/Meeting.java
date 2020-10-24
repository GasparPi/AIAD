package data;

import agents.Employee;

import java.util.HashMap;

public class Meeting {

    private final int id;
    private final int duration;

    private int day;
    private int startSlot;
    private int endSlot;

    private HashMap<Integer, Employee> obligatoryEmployees;

    public Meeting(int id, int duration) {
        this.id = id;
        this.duration = duration;
        this.obligatoryEmployees = new HashMap<>();
    }

    public void schedule(int day, int startSlot, int endSlot) {
        this.day = day;
        this.startSlot = startSlot;
        this.endSlot = endSlot;
    }

    public int getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public int getDay() {
        return day;
    }

    public int getStartSlot() {
        return startSlot;
    }

    public int getEndSlot() {
        return endSlot;
    }

}
