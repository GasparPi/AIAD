package data;

import java.util.ArrayList;

public class Meeting {

    private final int id;
    private final int duration;
    private final int groupId;

    private int day;
    private int startSlot;
    private int endSlot;

    private ArrayList<Integer> obligatoryEmployees;

    public Meeting(int id, int duration, int groupId) {
        this.id = id;
        this.duration = duration;
        this.groupId = groupId;
        this.obligatoryEmployees = new ArrayList<>();
    }

    public void addObligatoryEmployee(int employeeId) {
        this.obligatoryEmployees.add(employeeId);
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

    public int getGroupId() {
        return this.groupId;
    }

    public ArrayList<Integer> getObligatoryEmployees() {
        return this.obligatoryEmployees;
    }
}
