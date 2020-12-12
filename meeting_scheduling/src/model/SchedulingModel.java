package model;

import agents.Employee;
import agents.Scheduler;
import data.Group;
import data.Meeting;
import generators.EmployeesGenerator;
import generators.GroupsGenerator;
import generators.MeetingsGenerator;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.Schedule;

import java.util.ArrayList;

public class SchedulingModel extends Repast3Launcher {

    private int numberOfEmployees = 50;
    private int numberOfGroups = 50;
    private int numberOfMeetings = 50;

    ArrayList<Employee> employees;
    ArrayList<Meeting> meetings;
    ArrayList<Group> groups;
    Scheduler scheduler;

    Runtime runtime;
    Profile profile;
    ContainerController container;

    private OpenSequenceGraph plot;

    @Override
    public void begin() {
        super.begin();

        this.beginGraphs();
        this.beginAgents();
    }

    public void beginGraphs() {
        if (plot != null)
            plot.dispose();

        plot = new OpenSequenceGraph("Employees", this);
        plot.setAxisTitles("Employees", "Meetings");
        // ADD SEQUENCE
        plot.display();

        getSchedule().scheduleActionAtInterval(100, plot, "step", Schedule.LAST);
        getSchedule().execute();
    }

    public void beginAgents() {
        try {
            this.employees = EmployeesGenerator.generate(numberOfEmployees, this.container);

            this.groups = GroupsGenerator.generate(numberOfEmployees, numberOfGroups);

            this.meetings = MeetingsGenerator.generate(numberOfEmployees, this.groups, this.groups.size());

            this.scheduler = new Scheduler(this.groups, this.meetings);
            scheduler.setEmployeeNumber(employees.size());
            this.container.acceptNewAgent(scheduler.getId(), scheduler).start();
        } catch (StaleProxyException e) {
            System.err.println("Error starting new scheduler agent");
            e.printStackTrace();
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

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    public void setNumberOfGroups(int numberOfGroups) {
        this.numberOfGroups = numberOfGroups;
    }

    @Override
    protected void launchJADE() {
        this.runtime = Runtime.instance();
        this.profile = new ProfileImpl();
        this.profile.setParameter(Profile.GUI, "false");
        this.container = this.runtime.createMainContainer(this.profile);
    }

    @Override
    public void setup() {
        super.setup();

        setNumberOfEmployees(this.numberOfEmployees);
        setNumberOfGroups(this.numberOfGroups);
        setNumberOfMeetings(this.numberOfMeetings);
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
}
