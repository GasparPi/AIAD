package generators;

import data.Group;
import java.util.ArrayList;

public class GroupsGenerator {

    public static ArrayList<Group> generate(int numberOfEmployees, int numberOfGroups) {
        ArrayList<Group> groups = new ArrayList<>();

        int maxNumGroupEmployees = numberOfEmployees / numberOfGroups;

        for (int i = 0; i < numberOfGroups; i++) {
            Group group = new Group(i);

            int numGroupEmployees = 2 + (int) (Math.random() * (maxNumGroupEmployees - 1));
            for (int j = 0; j < numGroupEmployees; j++) {

                int employeeId;
                do {
                    employeeId = (int) (Math.random() * (numberOfEmployees - 1));
                } while (group.hasEmployee(employeeId));

                group.addEmployee(employeeId);
            }

            groups.add(group);
        }

        // Ensure that every employee belongs to at least one group
        for (int i = 0; i < numberOfEmployees; i++) {
            int groupId ;
            do {
                groupId = (int) (Math.random() * (numberOfGroups - 1));
            } while (groups.get(groupId).hasEmployee(i));

            groups.get(groupId).addEmployee(i);
        }

        return groups;
    }
}