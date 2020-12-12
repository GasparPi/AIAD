package generators;

import agents.Employee;
import data.Timeslot;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeesGenerator {
    //final static String GENERATED_DIR = "problem_generator/generated/";

    /*
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
     */

    public static ArrayList<Employee> generate(Integer numberOfEmployees, String logDir) {
        ArrayList<Employee> employees = new ArrayList<>();

        for(int i = 1; i <= numberOfEmployees; i++){
            Employee e = new Employee(i, generateAgenda(), logDir);

            employees.add(e);
        }

        return employees;
    }

    private static HashMap<String, ArrayList<Timeslot>> generateAgenda() {
        HashMap<String, ArrayList<Timeslot>> agenda = new HashMap<>();

        agenda.put("monday", generateTimeslots());
        agenda.put("tuesday", generateTimeslots());
        agenda.put("wednesday", generateTimeslots());
        agenda.put("thursday", generateTimeslots());
        agenda.put("friday", generateTimeslots());

        return agenda;
    }

    private static ArrayList<Timeslot> generateTimeslots() {
        ArrayList<Timeslot> timeslots = new ArrayList<>();

        for(int i=1; i < 10; i++){
            int usedTimeslot = 1 + (int) (Math.random() * 10);
            if(usedTimeslot <= 5){
                int priority = 1 + (int) (Math.random() * 3);
                Timeslot ts = new Timeslot(i, priority);
                timeslots.add(ts);
            }
        }

        return timeslots;
    }
}
