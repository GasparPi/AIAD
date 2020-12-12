import logger.MyLogger;
import model.SchedulingModel;
import uchicago.src.sim.engine.SimInit;

public class Main {
    public static void main(String[] args) {
        SchedulingModel schedulingModel = new SchedulingModel();

        if (args.length == 3) {
            schedulingModel.setNumberOfEmployees(Integer.parseInt(args[0]));
            schedulingModel.setNumberOfGroups(Integer.parseInt(args[1]));
            schedulingModel.setNumberOfEmployees(Integer.parseInt(args[2]));
        }

        MyLogger.deletePreviousLogs();

        SimInit init = new SimInit();
        init.loadModel(schedulingModel, null, false);
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
