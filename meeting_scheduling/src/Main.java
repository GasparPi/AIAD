import data.Group;
import data.Meeting;
import org.json.simple.parser.ParseException;
import parsers.*;

import java.io.File;
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
        HashMap<Integer, Meeting> meetings;
        HashMap<Integer, Group> groups;

        try {
            meetings = MeetingParser.parse(MEETINGS_DIR + MEETINGS_FILE);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse meetings file: " + MEETINGS_DIR + MEETINGS_FILE);
            e.printStackTrace();
            return;
        }

        for (Meeting m : meetings.values()) {
            System.out.println("Meeting id: " + m.getId() + "; duration: " + m.getDuration());
        }

        //Setup employees
        //Setup Meetings
        //Setup Groups
    }
}
