package generators;

import agents.Employee;
import data.Timeslot;
import jade.wrapper.StaleProxyException;
import sajas.wrapper.ContainerController;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeesGenerator {

    public static ArrayList<Employee> generate(Integer numberOfEmployees, Integer occupancyRate, ContainerController container) throws StaleProxyException {
        ArrayList<Employee> employees = new ArrayList<>();

        for(int i = 1; i <= numberOfEmployees; i++) {
            Employee e = new Employee(i, generateAgenda(occupancyRate));

            employees.add(e);

            container.acceptNewAgent(e.getStringId(), e).start();
        }

        return employees;
    }

    private static HashMap<String, ArrayList<Timeslot>> generateAgenda(Integer occupancyRate) {
        HashMap<String, ArrayList<Timeslot>> agenda = new HashMap<>();

        agenda.put("monday", generateTimeslots(occupancyRate));
        agenda.put("tuesday", generateTimeslots(occupancyRate));
        agenda.put("wednesday", generateTimeslots(occupancyRate));
        agenda.put("thursday", generateTimeslots(occupancyRate));
        agenda.put("friday", generateTimeslots(occupancyRate));

        return agenda;
    }

    private static ArrayList<Timeslot> generateTimeslots(Integer occupancyRate) {
        ArrayList<Timeslot> timeslots = new ArrayList<>();

        for (int i=1; i <= 10; i++){
            int usedTimeslot = 1 + (int) (Math.random() * 100);
            if (usedTimeslot <= (100 - occupancyRate)) {
                int priority = 1 + (int) (Math.random() * 3);
                Timeslot ts = new Timeslot(i, priority);
                timeslots.add(ts);
            }
        }

        return timeslots;
    }
}
