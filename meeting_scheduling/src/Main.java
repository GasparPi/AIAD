import agents.Employee;
import data.Group;
import data.Meeting;
import org.json.simple.parser.ParseException;
import parsers.*;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    final static String EMPLOYEES_DIR = "meeting_scheduling/vars/employees/";
    final static String GROUPS_DIR = "meeting_scheduling/vars/groups/";
    final static String MEETINGS_DIR = "meeting_scheduling/vars/meetings/";

    final static String EMPLOYEES_FILE = "e1.json";
    final static String GROUPS_FILE = "g1.json";
    final static String MEETINGS_FILE = "m1.json";

    public static void main(String[] args) {
        HashMap<Integer, Employee> employees;
        HashMap<Integer, Meeting> meetings;
        HashMap<Integer, Group> groups;

        //Setup employees
        try {
            employees = EmployeeParser.parse(EMPLOYEES_DIR + EMPLOYEES_FILE);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse meetings file: " + MEETINGS_DIR + MEETINGS_FILE);
            e.printStackTrace();
            return;
        }

        for (Employee e : employees.values()) {
            System.out.println(e.toString());
        }

        //Setup Meetings
        try {
            meetings = MeetingParser.parse(MEETINGS_DIR + MEETINGS_FILE);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse meetings file: " + MEETINGS_DIR + MEETINGS_FILE);
            e.printStackTrace();
            return;
        }

        //Setup Groups
        try {
            groups = GroupParser.parse(GROUPS_DIR + GROUPS_FILE);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse groups file: " + GROUPS_DIR + GROUPS_FILE);
            e.printStackTrace();
            return;
        }


        // Test: print
        for (Meeting m : meetings.values()) {
            System.out.println("Meeting id: " + m.getId() + "; duration: " + m.getDuration() + "; groupId:" + m.getGroupId());

            for (Integer id : m.getObligatoryEmployees())
                System.out.println("\tEmployee id:" + id);
        }

        System.out.println();

        for (Group g : groups.values()) {
            System.out.println("Group id: " + g.getId() + "; name: " + g.getName());

            for (Integer id : g.getEmployees())
                System.out.println("\tEmployee id:" + id);
        }
    }
}
