package model;
import agents.Employee;
import agents.Scheduler;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.space.Object2DGrid;

import java.util.ArrayList;

public class SchedulingModel extends SimModelImpl{
    private Schedule schedule;
    private static final boolean BATCH_MODE = true;
    private ArrayList<Employee> employees;
    private Scheduler schedulerAgent;
    private Object2DGrid space;
    private DisplaySurface dsurf;
    private OpenSequenceGraph plot;

    private int numberOfEmployees, spaceSize;

    public SchedulingModel(){
        numberOfEmployees = 20;
        spaceSize = 20;
    }

    @Override
    public String[] getInitParam() {
        return new String[0];
        // variaveis independentes
    }

    @Override
    public Schedule getSchedule() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setup() {
        schedule = new Schedule();
        if(dsurf != null)
            dsurf.dispose();
        dsurf = new DisplaySurface(this, "Meeting Scheduling Display");
        registerDisplaySurface("Meeting Scheduling Display", dsurf);

        //property descriptors ?
    }

    @Override
    public void begin() {
        buildModel();
        buildDisplay();
        buildSchedule();
    }

    private void buildModel() {

    }

    private void buildDisplay() {
    }

    private void buildSchedule() {
        schedule.scheduleActionBeginning(0, new MainAction());
        schedule.scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
        schedule.scheduleActionAtInterval(1, plot, "step", Schedule.LAST);
    }
}

class MainAction extends BasicAction {

    public void execute() {
        // prepare agent hashtable

        // shuffle agents
        //SimUtilities.shuffle(agentList);

        // iterate through all agents
//        for(int i = 0; i < agentList.size(); i++) {
//            ColorPickingAgent agent = (ColorPickingAgent) agentList.get(i);
//            if(movingMode == MovingMode.Walk) {
//                agent.walk();
//            } else {
//                agent.jump();
//            }
//            Color c = agent.recolor();
//            int nAgentsWithColor = (agentColors.get(c) == null ? 1 : agentColors.get(c)+1);
//            agentColors.put(c, nAgentsWithColor);
//        }
    }

}