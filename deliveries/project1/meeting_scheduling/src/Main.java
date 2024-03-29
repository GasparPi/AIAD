import agents.Employee;
import agents.Scheduler;
import data.Group;
import data.Meeting;
import jade.core.Profile;
import jade.tools.sniffer.Sniffer;
import jade.wrapper.StaleProxyException;
import org.json.simple.parser.ParseException;
import parsers.*;

import java.io.File;
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

    final static String LOGS_DIR = "logs/";

    HashMap<Integer, Employee> employees;
    HashMap<Integer, Meeting> meetings;
    HashMap<Integer, Group> groups;
    Scheduler scheduler;

    Runtime runtime;
    ProfileImpl profile;
    ContainerController container;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: Main <groups_filename> <meetings_filename> <employees_filename>\n" +
                    "Note: this files should be located in their respective directory under meeting_scheduling/vars/");
        }

        Main main = new Main();
        main.setupData(args[0], args[1]);

        try {
            main.setupAgents(args[2]);
        } catch (StaleProxyException e) {
            System.err.println("Failed to setup Employee Agents");
            e.printStackTrace();
            return;
        }

        main.deletePreviousLogs();
        main.printInfo();
    }

    public void setupData(String groupsFile, String meetingsFile) {
        //Setup Groups
        try {
            groups = GroupParser.parse(GROUPS_DIR + groupsFile);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse groups file: " + GROUPS_DIR + groupsFile +
                    ". Make sure the file is located at meeting_scheduling/vars/groups");
            e.printStackTrace();
        }

        //Setup Meetings
        try {
            meetings = MeetingParser.parse(MEETINGS_DIR + meetingsFile);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse meetings file: " + MEETINGS_DIR + meetingsFile +
                    ". Make sure the file is located at meeting_scheduling/vars/meetings");
            e.printStackTrace();
            return;
        }

        //Get total meetings for each group
        for (Meeting m : meetings.values()){
            groups.get(m.getGroupId()).incMeetings();
        }
    }

    public void setupAgents(String employeesFile) throws StaleProxyException {
        this.runtime = Runtime.instance();
        this.profile = new ProfileImpl();
        this.profile.setParameter(Profile.GUI, "true");
        this.container = this.runtime.createMainContainer(this.profile);

        this.setupDebugMode();

        //Setup employees
        try {
            employees = EmployeeParser.parse(EMPLOYEES_DIR + employeesFile, LOGS_DIR);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse employees file: " + EMPLOYEES_DIR + employeesFile +
                    ". Make sure the file is located at meeting_scheduling/vars/employees");
            e.printStackTrace();
        }

        //Get total meetings for each employee
        for (Group g : groups.values()){
            for (int emp : g.getEmployees()){
                employees.get(emp).addMeetings(g.getMeetings());
            }
        }

        //Add employee agents to container
        for (Employee e : employees.values()) {
            AgentController agentController = this.container.acceptNewAgent(e.getStringId(), e);
            agentController.start();
            try {
                Thread.sleep(1);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        // Setup Scheduler
        this.scheduler = new Scheduler(this.groups, this.meetings, LOGS_DIR);
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

    private void setupDebugMode() {
        Sniffer sniffer = new Sniffer();

        try {
            AgentController agentController = this.container.acceptNewAgent("sniffer", sniffer);
            agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    private void deletePreviousLogs() {
        File logDir = new File(LOGS_DIR);
        if (logDir.exists())
            deleteDirRecursively(logDir);
    }

    private void deleteDirRecursively(File dir) {
        File[] allContents = dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirRecursively(file);
            }
        }
        dir.delete();
    }
}
