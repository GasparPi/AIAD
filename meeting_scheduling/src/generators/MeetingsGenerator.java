package generators;

import data.Group;
import data.Meeting;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MeetingsGenerator {
    public static ArrayList<Meeting> generate(Integer numberOfMeetings, ArrayList<Group> groups, Integer numberOfGroups) {
        ArrayList<Meeting> meetings = new ArrayList<>();

        for(int i = 1; i <= numberOfMeetings; i++){ //generate one for group and more
            int duration = 1 + (int) (Math.random() * 3);
            int group = (int) (Math.random() * numberOfGroups);

            ArrayList<Integer> obligatoryEmployees = generateObligatoryEmployees(groups.get(group).getEmployees());

            meetings.add(new Meeting(i, duration, group, obligatoryEmployees));
        }

        return meetings;
    }

    private static ArrayList<Integer> generateObligatoryEmployees(ArrayList<Integer> employees) {
        ArrayList<Integer> aux = new ArrayList<>();
        aux.addAll(employees);

        int numberOfObligatoryEmployees = aux.size() > 3 ? aux.size()/2 : 2;

        ArrayList<Integer> obligatoryEmployees = new ArrayList<>();

        for(int i = 0; i < numberOfObligatoryEmployees; i++){
            int j = (int) (Math.random() * aux.size());
            obligatoryEmployees.add(aux.get(j));
            aux.remove(j);
        }

        return obligatoryEmployees;
    }
}
