package parsers;

import data.Group;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class GroupParser {

    public static HashMap<Integer, Group> parse(String pathname) throws IOException, ParseException {
        HashMap<Integer, Group> groups = new HashMap<>();

        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(pathname));
        JSONArray jsonArray = (JSONArray) object;

        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;

            int id = Integer.parseInt(jsonObject.get("id").toString());

            Group group = new Group(id);
            groups.put(group.getId(), group);

            // Employees
            JSONArray employeesArray = (JSONArray) jsonObject.get("employees");
            for (Object employee : employeesArray) {
                group.addEmployee(Integer.parseInt(employee.toString()));
            }
        }

        return groups;
    }


}
