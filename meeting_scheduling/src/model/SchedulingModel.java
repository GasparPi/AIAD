package model;

import agents.Employee;
import agents.Scheduler;
import data.Group;
import data.Meeting;
import drawables.MeetingInfo;
import drawables.Timeslot;
import generators.EmployeesGenerator;
import generators.GroupsGenerator;
import generators.MeetingsGenerator;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.analysis.BinDataSource;
import uchicago.src.sim.analysis.Histogram;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

import java.util.ArrayList;

public class SchedulingModel extends Repast3Launcher {

    private int numberOfEmployees = 50;
    private int numberOfGroups = 10;
    private int numberOfMeetings = 20;
    private int occupancyRate = 50;

    ArrayList<Employee> employees;
    ArrayList<Meeting> meetings;
    ArrayList<Group> groups;
    Scheduler scheduler;

    Runtime runtime;
    Profile profile;
    ContainerController container;

    private Object2DGrid employeesSpace;
    private Object2DGrid meetingsSpace;
    private DisplaySurface displaySurface;
    private DisplaySurface displayMeetingSurface;
    private Histogram bar;

    @Override
    public void setup() {
        super.setup();

        setNumberOfEmployees(this.numberOfEmployees);
        setNumberOfGroups(this.numberOfGroups);
        setNumberOfMeetings(this.numberOfMeetings);
        setOccupancyRate(this.occupancyRate);

        if (this.displaySurface != null)
            this.displaySurface.dispose();
        this.displaySurface = new DisplaySurface(this, "Employee occupation Display");

        if (this.displayMeetingSurface != null)
            this.displayMeetingSurface.dispose();
        this.displayMeetingSurface = new DisplaySurface(this, "Meeting scheduling Display");

        if (bar != null) {
            bar.dispose();
        }
    }

    @Override
    public void begin() {
        super.begin();

        this.buildModel();
        this.buildDisplay();
        this.buildSchedule();

        this.displaySurface.display();
        this.displayMeetingSurface.display();
        this.bar.display();
    }

    private void buildModel() {
        this.buildAgents();
        this.buildEmployeeSpace();
        this.buildMeetingSpace();
    }

    private void buildAgents() {
        try {
            this.employees = EmployeesGenerator.generate(this.numberOfEmployees, this.occupancyRate, this.container);

            this.groups = GroupsGenerator.generate(this.numberOfEmployees, this.numberOfGroups);

            this.meetings = MeetingsGenerator.generate(this.numberOfMeetings, this.groups, this.groups.size());

            this.scheduler = new Scheduler(this.groups, this.meetings);
            this.scheduler.setEmployeeNumber(this.employees.size());
            this.container.acceptNewAgent(this.scheduler.getId(), this.scheduler).start();
        } catch (StaleProxyException e) {
            System.err.println("Error starting new scheduler agent");
            e.printStackTrace();
        }
    }

    private void buildEmployeeSpace() {
        int numTimeSlots = 50;
        this.employeesSpace = new Object2DGrid(this.numberOfEmployees, numTimeSlots);
        for (int employeeIndex = 0; employeeIndex < this.numberOfEmployees; employeeIndex++) {
            Employee employee = this.employees.get(employeeIndex);

            for (int timeSlotNumber = 0; timeSlotNumber < numTimeSlots; timeSlotNumber++) {
                int day = timeSlotNumber / 10;
                int dayTimeslot = timeSlotNumber % 10;

                Timeslot drawable = new Timeslot(employeeIndex, timeSlotNumber, day, dayTimeslot, employee);
                this.employeesSpace.putObjectAt(employeeIndex, timeSlotNumber, drawable);
            }
        }
    }

    private void buildMeetingSpace(){
        int spaceSize = (int) Math.ceil(Math.sqrt(numberOfMeetings));
        this.meetingsSpace = new Object2DGrid(spaceSize, spaceSize);
        int x = 0, y = 0;

        for(int i = 0; i < numberOfMeetings; i++){
            MeetingInfo drawable = new MeetingInfo(x,y,this.meetings.get(i));

            this.meetingsSpace.putObjectAt(x, y, drawable);

            x++;
            if(x == spaceSize) {
                x = 0;
                y++;
            }
        }
    }

    private void buildDisplay() {
        Object2DDisplay employeeDisplay = new Object2DDisplay(this.employeesSpace);
        this.displaySurface.addDisplayable(employeeDisplay, "Employees");
        addSimEventListener(this.displaySurface);

        Object2DDisplay meetingDisplay = new Object2DDisplay(this.meetingsSpace);
        this.displayMeetingSurface.addDisplayable(meetingDisplay, "Meetings");
        addSimEventListener(this.displayMeetingSurface);

        bar = new Histogram("Scheduled Meetings Per Group", numberOfGroups + 1, 0,  numberOfGroups + 1, this);
        bar.setYRange(0, numberOfMeetings / 2);
        bar.setAxisTitles("Groups", "Number of Meetings");

        BinDataSource source = new BinDataSource()  {
            public double getBinValue(Object o) {
                Meeting g = (Meeting) o;
                return g.getGroupIfScheduled();
            }
        };

        bar.createHistogramItem("Meetings", meetings, source);
    }

    private void buildSchedule() {
        getSchedule().scheduleActionAtInterval(100, this.displaySurface, "updateDisplay", Schedule.LAST);
        getSchedule().scheduleActionAtInterval(100, this.displayMeetingSurface, "updateDisplay", Schedule.LAST);
        getSchedule().scheduleActionAtInterval(100, bar, "step", Schedule.LAST);

        getSchedule().execute();
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

    public int getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(int occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    @Override
    protected void launchJADE() {
        this.runtime = Runtime.instance();
        this.profile = new ProfileImpl();
        this.profile.setParameter(Profile.GUI, "false");
        this.container = this.runtime.createMainContainer(this.profile);
    }

    @Override
    public String[] getInitParam() {
        return new String[]{
                "numberOfEmployees",
                "numberOfGroups",
                "numberOfMeetings",
                "occupancyRate"
        };
    }

    @Override
    public String getName() {
        return "Meeting Scheduling Model";
    }
}
