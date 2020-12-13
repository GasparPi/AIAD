package data;

import java.util.ArrayList;

public class Meeting {

    private final int id;
    private final int duration;
    private final int groupId;
    private final ArrayList<Integer> groupEmployees;

    private String day;
    private int startSlot;
    private int endSlot;
    private ArrayList<Integer> attendingEmployees;


    private final ArrayList<Integer> obligatoryEmployees;
    private boolean scheduled;

    public Meeting(int id, int duration, int groupId, ArrayList<Integer> groupEmployees, ArrayList<Integer> obligatoryEmployees) {
        this.id = id;
        this.duration = duration;
        this.groupId = groupId;
        this.groupEmployees = groupEmployees;
        this.obligatoryEmployees = obligatoryEmployees;
        this.scheduled = false;
    }

    public void schedule(String day, int startSlot, int endSlot, ArrayList<Integer> attendingEmployees) {
        this.scheduled = true;
        this.day = day;
        this.startSlot = startSlot;
        this.endSlot = endSlot;
        this.attendingEmployees = attendingEmployees;
    }

    public int getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public String getDay() {
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

    public int getGroupIfScheduled() {
        if(day != null && !day.equals("NULL")){
            return this.groupId;
        }
        else{
            return 0;
        }
    }

    public ArrayList<Integer> getObligatoryEmployees() {
        return this.obligatoryEmployees;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Meeting id: ").append(this.id).append("; duration: ").append(this.duration).append("; groupId:").append(this.groupId).append("\n");

        for (Integer id : this.obligatoryEmployees)
            stringBuilder.append("\tEmployee id:").append(id).append("\n");

        return stringBuilder.toString();
    }

    public ArrayList<Integer> getAttendingEmployees() {
        return attendingEmployees;
    }

    public ArrayList<Integer> getGroupEmployees() {
        return groupEmployees;
    }

    public boolean isScheduled() {
        return scheduled;
    }
}
