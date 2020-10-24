package data;

import java.util.ArrayList;

public class Group {

    private final int id;
    private final String name;

    ArrayList<Integer> employees;

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
        this.employees = new ArrayList<>();
    }

    public void addEmployee(int employeeId) {
        this.employees.add(employeeId);
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Integer> getEmployees() {
        return this.employees;
    }
}
