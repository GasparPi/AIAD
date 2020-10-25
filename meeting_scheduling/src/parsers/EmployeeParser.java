package parsers;

import agents.Employee;
import data.Macros;
import data.Timeslot;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeParser {
    public static HashMap<Integer, Employee> parse(String pathname) throws IOException, ParseException {
        HashMap<Integer, Employee> employees = new HashMap<>();

        JSONParser parser = new JSONParser();
        JSONArray employees_json = (JSONArray) parser.parse(new FileReader(pathname));

        for (Object object : employees_json)
        {
            JSONObject employee_raw = (JSONObject) object;

            int id = Integer.parseInt(employee_raw.get("id").toString());

            HashMap<String, ArrayList<Timeslot>> agenda = new HashMap<>();

            JSONObject agenda_raw = (JSONObject) employee_raw.get("agenda");

            parseAgenda(Macros.MONDAY, agenda_raw, agenda);
            parseAgenda(Macros.TUESDAY, agenda_raw, agenda);
            parseAgenda(Macros.WEDNESDAY, agenda_raw, agenda);
            parseAgenda(Macros.THURSDAY, agenda_raw, agenda);
            parseAgenda(Macros.FRIDAY, agenda_raw, agenda);

            employees.put(id, new Employee(id, agenda));
        }

        return employees;
    }

    private static void parseAgenda(String dayOfWeek, JSONObject agenda_raw, HashMap<String, ArrayList<Timeslot>> agenda) {
        JSONArray dayOfWeekTimeslots = (JSONArray) agenda_raw.get(dayOfWeek);
        ArrayList<Timeslot> timeslots = new ArrayList<>();

        for (Object timeslot : dayOfWeekTimeslots)
        {
            JSONArray aux_timeslots = (JSONArray) timeslot;
            int id = Integer.parseInt(aux_timeslots.get(0).toString());
            int priority = Integer.parseInt(aux_timeslots.get(1).toString());
            timeslots.add(new Timeslot(id, priority));
        }

        agenda.put(dayOfWeek, timeslots);
    }
}
