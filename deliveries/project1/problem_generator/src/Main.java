import generators.EmployeesGenerator;
import generators.GroupsGenerator;
import generators.MeetingsGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args){
        if(args.length < 2 || args.length > 4){
            System.err.println("Usage: Main <file name> <number of employees> <number of groups> <number of meetings>");
            return;
        }

        String fileCode = args[0];
        Integer numberOfEmployees = Integer.parseInt(args[1]);
        Integer numberOfGroups = args.length >= 3 ? Integer.parseInt(args[2]) : 0;
        Integer numberOfMeetings = args.length >= 4 ? Integer.parseInt(args[3]) : 0;

        EmployeesGenerator.generate(fileCode, numberOfEmployees);

        if(numberOfGroups > 0) {
            HashMap<Integer, ArrayList<Integer>> groups = GroupsGenerator.generate(fileCode, numberOfEmployees, numberOfGroups);
            if(numberOfMeetings > 0 && groups != null)
                MeetingsGenerator.generate(fileCode, numberOfMeetings, groups, numberOfGroups);
        }
        else return;
    }
}
