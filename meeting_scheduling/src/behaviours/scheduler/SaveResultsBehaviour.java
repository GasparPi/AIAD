package behaviours.scheduler;

import agents.Scheduler;
import data.Group;
import data.Meeting;
import sajas.core.behaviours.Behaviour;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveResultsBehaviour extends Behaviour {

    private static final String RESULTS_DIR = "meeting_scheduling/results/";
    private static final String CURRENT_RESULTS_DIR = System.currentTimeMillis() + "/";
    private static final String MEETINGS_RESULTS_FILE = "meetings.json";

    private final Scheduler scheduler;
    private boolean savedResults = false;

    public SaveResultsBehaviour(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void action() {

        System.out.println("Saving...");

        File resultsDir = new File(RESULTS_DIR);

        if (!resultsDir.exists())
            resultsDir.mkdir();

        File currentResultsDir = new File(RESULTS_DIR + CURRENT_RESULTS_DIR);
        currentResultsDir.mkdir();

        this.saveMeetingsResults();

        this.savedResults = true;
    }

    private void saveMeetingsResults() {
        File resultsFile = new File(RESULTS_DIR + CURRENT_RESULTS_DIR + MEETINGS_RESULTS_FILE);

        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(resultsFile);
        } catch (IOException e) {
            System.err.println("Failed to create file writer!");
            e.printStackTrace();
            return;
        }

        try {
            fileWriter.write(parseMeetingsResults().toJSONString());
        } catch (IOException e) {
            System.err.println("Failed to write results file!");
            e.printStackTrace();
            return;
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Failed to close results file!");
            e.printStackTrace();
        }
    }

    private JSONObject parseGroupObject(Integer groupId) {
        JSONObject groupJson = new JSONObject();

        Group group = this.scheduler.getGroups().get(groupId);
        groupJson.put("id", group.getId());
        groupJson.put("nr_meetings", group.getMeetings());

        JSONArray employees = new JSONArray();
        for (Integer employeeId : group.getEmployees())
            employees.add(employeeId);
        groupJson.put("employees", employees);

        return groupJson;
    }

    private JSONArray parseMeetingsResults() {
        JSONArray resultsArray = new JSONArray();

        for (Meeting meeting : this.scheduler.getMeetings()) {
            JSONObject jsonMeeting = new JSONObject();

            jsonMeeting.put("id", meeting.getId());
            jsonMeeting.put("duration", meeting.getDuration());
            jsonMeeting.put("group", parseGroupObject(meeting.getGroupId()));

            // Obligatory Employees
            JSONArray obligatoryEmployees = new JSONArray();
            for (Integer employeeId : meeting.getObligatoryEmployees())
                obligatoryEmployees.add(employeeId);
            jsonMeeting.put("obligatory_employees", obligatoryEmployees);

            jsonMeeting.put("day", meeting.getDay());
            jsonMeeting.put("start_slot", meeting.getStartSlot());
            jsonMeeting.put("end_slot", meeting.getEndSlot());

            // Attending Employees
            JSONArray attendingEmployees = new JSONArray();
            for (Integer employeeId : meeting.getAttendingEmployees())
                attendingEmployees.add(employeeId);
            jsonMeeting.put("attending_employees", attendingEmployees);

            resultsArray.add(jsonMeeting);
        }

        return resultsArray;
    }

    @Override
    public boolean done() {
        return savedResults;
    }
}
