package generators;

import data.Group;
import java.util.ArrayList;

public class GroupsGenerator {

    private static ArrayList<Group> generateGroups(int numberOfEmployees, int numberOfGroups) {
        return new ArrayList<>();
    }

    private static ArrayList<Integer> generateEmployees(ArrayList<Integer> employeeArray) {
        ArrayList<Integer> employees = new ArrayList<>();

        ArrayList<Integer> aux = new ArrayList<>();
        aux.addAll(employeeArray);
        int numberOfEmployees = 2 + (int) (Math.random() * (aux.size() - 1));

        if (numberOfEmployees == aux.size())
            return aux;

        for (int i = 0; i < numberOfEmployees; i++) {
            int j = (int) (Math.random() * aux.size());
            employees.add(aux.get(j));
            aux.remove(j);
        }

        return employees;
    }

}