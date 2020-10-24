import agents.Employee;

import java.util.HashMap;

public class Group {

    private final int id;
    private final String name;

    HashMap<Integer, Employee> employees;
    HashMap<Integer, Meeting> meetings;

    public Group(int id, String name) {
        this.id = id;
        this.name = name;

        this.employees = new HashMap<>();
        this.meetings = new HashMap<>();
    }

    public void addEmployee(Employee employee) {
        this.employees.put(employee.getId(), employee);
    }

    public void addMeeting(Meeting meeting) {
        this.meetings.put(meeting.getId(), meeting);
    }

}
