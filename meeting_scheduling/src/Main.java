import agents.Employee;
import agents.Scheduler;
import data.Group;
import data.Meeting;
import jade.wrapper.StaleProxyException;
import org.json.simple.parser.ParseException;
import parsers.*;

import java.io.IOException;
import java.util.HashMap;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Main {
    final static String EMPLOYEES_DIR = "meeting_scheduling/vars/employees/";
    final static String GROUPS_DIR = "meeting_scheduling/vars/groups/";
    final static String MEETINGS_DIR = "meeting_scheduling/vars/meetings/";

    final static String EMPLOYEES_FILE = "e1.json";
    final static String GROUPS_FILE = "g1.json";
    final static String MEETINGS_FILE = "m1.json";

    HashMap<Integer, Employee> employees;
    HashMap<Integer, Meeting> meetings;
    HashMap<Integer, Group> groups;
    Scheduler scheduler;

    Runtime runtime;
    ProfileImpl profile;
    ContainerController container;

    public static void main(String[] args) {
        Main main = new Main();

        main.setupData();

//        try {
//            main.setupAgents();
//        } catch (StaleProxyException e) {
//            System.err.println("Failed to setup Employee Agents");
//            e.printStackTrace();
//            return;
//        }

        main.printInfo();
    }

    public void setupData() {
        //Setup Meetings
        try {
            meetings = MeetingParser.parse(MEETINGS_DIR + MEETINGS_FILE);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse meetings file: " + MEETINGS_DIR + MEETINGS_FILE);
            e.printStackTrace();
            return;
        }

        //Setup Groups
        try {
            groups = GroupParser.parse(GROUPS_DIR + GROUPS_FILE);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse groups file: " + GROUPS_DIR + GROUPS_FILE);
            e.printStackTrace();
        }
    }

    public void setupAgents() throws StaleProxyException {
        this.runtime = Runtime.instance();
        this.profile = new ProfileImpl();
        // TODO: this.profile.setParameter();
        this.container = this.runtime.createMainContainer(this.profile);

        //Setup employees
        try {
            employees = EmployeeParser.parse(EMPLOYEES_DIR + EMPLOYEES_FILE);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse employees file: " + EMPLOYEES_DIR + EMPLOYEES_FILE);
            e.printStackTrace();
        }

        for (Employee e : employees.values()) {
            AgentController agentController = this.container.acceptNewAgent(e.getStringId(), e);
            agentController.start();
        }

        // Setup Scheduler
        this.scheduler = new Scheduler(this.groups, this.meetings);
        AgentController schedulerController = this.container.acceptNewAgent(scheduler.getId(), scheduler);
        schedulerController.start();
    }

    public void printInfo() {
        for (Employee e : employees.values()) {
            System.out.println(e.toString());
        }

        for (Meeting m : meetings.values()) {
            System.out.println(m.toString());
        }

        for (Group g : groups.values()) {
            System.out.println(g.toString());
        }
    }
}
