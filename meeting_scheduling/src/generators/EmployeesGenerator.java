package generators;

import agents.Employee;
import data.Timeslot;
import jade.wrapper.StaleProxyException;
import sajas.wrapper.ContainerController;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeesGenerator {
    public static ArrayList<Employee> generate(Integer numberOfEmployees, ContainerController container) throws StaleProxyException {
        ArrayList<Employee> employees = new ArrayList<>();

        for(int i = 1; i <= numberOfEmployees; i++){
            Employee e = new Employee(i, generateAgenda());

            employees.add(e);

            container.acceptNewAgent(e.getStringId(), e).start();
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
