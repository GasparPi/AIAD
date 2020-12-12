import agents.Employee;
import agents.Scheduler;
import data.Group;
import data.Meeting;

import jade.core.Profile;
import jade.wrapper.StaleProxyException;
import org.json.simple.parser.ParseException;
import parsers.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import jade.core.ProfileImpl;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.AgentController;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.ScheduleBase;
import uchicago.src.sim.engine.SimInit;


public class Main extends Repast3Launcher {
    final static String EMPLOYEES_DIR = "meeting_scheduling/vars/employees/";
    final static String GROUPS_DIR = "meeting_scheduling/vars/groups/";
    final static String MEETINGS_DIR = "meeting_scheduling/vars/meetings/";

    final static String LOGS_DIR = "logs/";

    ArrayList<Employee> employees;
    HashMap<Integer, Meeting> meetings;
    HashMap<Integer, Group> groups;
    Scheduler scheduler;

    Runtime runtime;
    Profile profile;
    ContainerController container;

    private OpenSequenceGraph plot;

    private int numberOfEmployees, numberOfMeetings;
    public Main(){
        super();
    }

    @Override
    protected void launchJADE() {
        this.runtime = Runtime.instance();
        this.profile = new ProfileImpl();
        this.profile.setParameter(Profile.GUI, "true");
        this.container = this.runtime.createMainContainer(this.profile);
    }

    public static void main(String[] args) {
        SimInit init = new SimInit();
        init.loadModel(new Main(), null, false);
    }

    @Override
    public String[] getInitParam() {
        return new String[]{
            "numberOfEmployees",
            "numberOfGroups",
            "numberOfMeetings",
        };
    }

    @Override
    public String getName() {
        return "Meeting Scheduling Model";
    }

    @Override
    public void setup() {
        super.setup();

        setNumberOfEmployees(100);
        setNumberOfMeetings(200);
    }

    @Override
    public void begin() {
        super.begin();

        this.employees = new ArrayList<>();

        if(plot != null)
            plot.dispose();
        plot = new OpenSequenceGraph("Employees", this);
        plot.setAxisTitles("Employees", "Meetings");
//      ADD SEQUENCE
        plot.display();

        getSchedule().scheduleActionAtInterval(100, plot, "step", Schedule.LAST);
        getSchedule().execute();

        try{
            //Add employee agents to container

            // generate EMPLOYEES
            // DO THIS FOR EACH EMPLOYEE this.container.acceptNewAgent(e.getStringId(), e).start();
            // AND THROW EXCEPTION

            // generate GROUPS
            // generate MEETINGS

            // Setup Scheduler
            this.scheduler = new Scheduler(this.groups, this.meetings, LOGS_DIR);
            scheduler.setEmployeeNumber(employees.size());
            this.container.acceptNewAgent(scheduler.getId(), scheduler).start();
        } catch (StaleProxyException staleProxyException) {
            staleProxyException.printStackTrace();
        }
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public int getNumberOfMeetings() {
        return numberOfMeetings;
    }

    public void setNumberOfMeetings(int numberOfMeetings) {
        this.numberOfMeetings = numberOfMeetings;
    }

    /* private final String groupsFile;
    private final String meetingsFile;
    private final String employeesFile;

    public Main(String groupsFile, String meetingsFile, String employeesFile) {
        super();

        this.groupsFile = groupsFile;
        this.meetingsFile = meetingsFile;
        this.employeesFile = employeesFile;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: Main <groups_filename> <meetings_filename> <employees_filename>\n" +
                    "Note: this files should be located in their respective directory under meeting_scheduling/vars/");
        }

        SimInit init = new SimInit();
        init.setNumRuns(1);   // works only in batch mode
        init.loadModel(new Main(args[0], args[1], args[2]), null, true);
    }

    public void setupData(String groupsFile, String meetingsFile) {
        //Setup Groups
        try {
            groups = GroupParser.parse(GROUPS_DIR + groupsFile);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse groups file: " + GROUPS_DIR + groupsFile +
                    ". Make sure the file is located at meeting_scheduling/vars/groups");
            e.printStackTrace();
            return;
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

        //Setup employees
        try {
            employees = EmployeeParser.parse(EMPLOYEES_DIR + employeesFile, LOGS_DIR);
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse employees file: " + EMPLOYEES_DIR + employeesFile +
                    ". Make sure the file is located at meeting_scheduling/vars/employees");
            e.printStackTrace();
        }

        //Add employee agents to container
        for (Employee e : employees.values()) {
            AgentController agentController = this.container.acceptNewAgent(e.getStringId(), e);
            agentController.start();
        }

        this.setupData(groupsFile, meetingsFile);


        // Setup Scheduler
        this.scheduler = new Scheduler(this.groups, this.meetings, LOGS_DIR);
        scheduler.setEmployeeNumber(employees.size());
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

    @Override
    protected void launchJADE() {
        //this.setupData(groupsFile, meetingsFile);

        try {
            this.setupAgents(employeesFile);
        } catch (StaleProxyException e) {
            System.err.println("Failed to setup Employee Agents");
            e.printStackTrace();
            return;
        }

        this.deletePreviousLogs();
        this.printInfo();
    }

    @Override
    public String[] getInitParam() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "Meeting Scheduling";
    }*/
}
