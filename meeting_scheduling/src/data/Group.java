package data;

import java.util.ArrayList;

public class Group {

    private final int id;

    ArrayList<Integer> employees;
    private int meetings;

    public Group(int id) {
        this.id = id;
        this.employees = new ArrayList<>();
        this.meetings = 0;
    }

    public void addEmployee(int employeeId) {
        this.employees.add(employeeId);
    }

    public Integer getId() {
        return this.id;
    }

    public ArrayList<Integer> getEmployees() {
        return this.employees;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Group id: ").append(this.id).append("\n");

        for (Integer id : this.employees)
            stringBuilder.append("\tEmployee id:").append(id).append("\n");

        return stringBuilder.toString();
    }

    public void incMeetings() { meetings++; }

    public int getMeetings() {
        return meetings;
    }
}
