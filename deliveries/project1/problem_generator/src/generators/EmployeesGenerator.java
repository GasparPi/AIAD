package generators;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EmployeesGenerator {
    final static String GENERATED_DIR = "problem_generator/generated/";

    public static boolean generate(String fileCode, Integer numberOfEmployees){
        try {
            File file = new File(GENERATED_DIR + fileCode + "_employees.json");

            if(!file.createNewFile()){
                System.err.println("File already exists");
                return false;
            }

            FileWriter fileWriter = new FileWriter(file);
            JSONArray finalObject = new JSONArray();

            for(int i = 1; i <= numberOfEmployees; i++){
                JSONObject employeeObject = new JSONObject();

                employeeObject.put("id", i);

                employeeObject.put("agenda", generateAgenda());

                finalObject.add(employeeObject);
            }

            fileWriter.write(finalObject.toJSONString());

            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Failed to open file");
            return false;
        }

        return true;
    }

    private static JSONObject generateAgenda() {
        JSONObject agendaObject = new JSONObject();

        agendaObject.put("monday", generateTimeslots());
        agendaObject.put("tuesday", generateTimeslots());
        agendaObject.put("wednesday", generateTimeslots());
        agendaObject.put("thursday", generateTimeslots());
        agendaObject.put("friday", generateTimeslots());

        return agendaObject;
    }

    private static JSONArray generateTimeslots() {
        JSONArray timeslots = new JSONArray();

        for(int i=1; i < 10; i++){
            int usedTimeslot = 1 + (int) (Math.random() * 10);
            if(usedTimeslot <= 5){
                int priority = 1 + (int) (Math.random() * 3);
                JSONArray pair = new JSONArray();
                pair.add(i);
                pair.add(priority);
                timeslots.add(pair);
            }
        }

        return timeslots;
    }
}
