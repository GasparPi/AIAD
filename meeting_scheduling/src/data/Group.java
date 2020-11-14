package data;

import java.util.ArrayList;

public class Group {

    private final int id;

    ArrayList<Integer> employees;

    public Group(int id) {
        this.id = id;
        this.employees = new ArrayList<>();
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
}
