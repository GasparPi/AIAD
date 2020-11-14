package generators;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MeetingsGenerator {
    final static String GENERATED_DIR = "problem_generator/generated/";

    public static boolean generate(String fileCode, Integer numberOfMeetings, HashMap<Integer, ArrayList<Integer>> groups, Integer numberOfGroups) {
        try {
            File file = new File(GENERATED_DIR + fileCode + "_meetings.json");

            if(!file.createNewFile()){
                System.err.println("File already exists");
                return false;
            }

            FileWriter fileWriter = new FileWriter(file);
            JSONArray finalObject = new JSONArray();

            for(int i = 1; i <= numberOfMeetings; i++){ //generate one for group and more
                JSONObject meetingObject = new JSONObject();

                meetingObject.put("id", i);

                int duration = 1 + (int) (Math.random() * 3);

                meetingObject.put("duration", duration);

                int group = 1 + (int) (Math.random() * numberOfGroups);

                meetingObject.put("group", group);

                meetingObject.put("obligatory-employees", generateObligatoryEmployees(groups.get(group)));

                finalObject.add(meetingObject);
            }

            fileWriter.write(finalObject.toJSONString());

            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Failed to open file");
            return false;
        }

        return true;
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
