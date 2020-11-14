package generators;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupsGenerator {
    final static String GENERATED_DIR = "problem_generator/generated/";

    public static HashMap<Integer, ArrayList<Integer>> generate(String fileCode, Integer numberOfEmployees, Integer numberOfGroups) {
        try {
            File file = new File(GENERATED_DIR + fileCode + "_groups.json");

            if(!file.createNewFile()){
                System.out.println("File already exists");
                return null;
            }

            FileWriter fileWriter = new FileWriter(file);
            JSONArray finalObject = new JSONArray();

            ArrayList<Integer> employeeArray = prepareEmployeeArray(numberOfEmployees);
            HashMap<Integer, ArrayList<Integer>> groups = new HashMap<>();

            for(int i = 1; i <= numberOfGroups; i++){
                JSONObject groupObject = new JSONObject();

                groupObject.put("id", i);

                ArrayList<Integer> employees = generateEmployees(employeeArray);

                groupObject.put("employees", employees);
                groups.put(i, employees);

                finalObject.add(groupObject);
            }

            fileWriter.write(finalObject.toJSONString());

            fileWriter.close();

            return groups;
        } catch (IOException e) {
            System.out.println("Failed to open file");
            return null;
        }
    }

    private static ArrayList<Integer> generateEmployees(ArrayList<Integer> employeeArray) {
        ArrayList<Integer> aux = new ArrayList<>();
        aux.addAll(employeeArray);
        ArrayList<Integer> employees = new ArrayList<>();
        int numberOfEmployees = 2 + (int) (Math.random() * (aux.size() - 1));

        if(numberOfEmployees == aux.size())
            return aux;

        for(int i = 0; i < numberOfEmployees; i++) {
            int j = (int) (Math.random() * aux.size());
            employees.add(aux.get(j));
            aux.remove(j);
        }

        return employees;
    }

    private static ArrayList<Integer> prepareEmployeeArray(Integer numberOfEmployees) {
        ArrayList<Integer> employeeArray = new ArrayList<>();

        for(int i = 1; i <= numberOfEmployees; i++){
            employeeArray.add(i);
        }

        return employeeArray;
    }
}