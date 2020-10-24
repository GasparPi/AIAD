package parsers;

import data.Meeting;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MeetingParser {

    public static HashMap<Integer, Meeting> parse(String pathname) throws IOException, ParseException {
        HashMap<Integer, Meeting> meetings = new HashMap<>();

        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(pathname));
        JSONArray jsonArray = (JSONArray) object;

        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;

            int id = Integer.parseInt(jsonObject.get("id").toString());
            int duration = Integer.parseInt(jsonObject.get("duration").toString());
            int groupId = Integer.parseInt(jsonObject.get("group").toString());

            Meeting meeting = new Meeting(id, duration, groupId);
            meetings.put(meeting.getId(), meeting);

            // Obligatory Employees
            JSONArray employeesArray = (JSONArray) jsonObject.get("obligatory-employees");
            for (Object employee : employeesArray) {
                meeting.addObligatoryEmployee(Integer.parseInt(employee.toString()));
            }
        }

        return meetings;
    }
}
