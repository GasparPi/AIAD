package agents;

import behaviours.SchedulerBehaviour;
import data.Group;
import data.Meeting;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

public class Scheduler extends Agent {

    private final String id = "scheduler";

    public HashMap<Integer, Group> groups; // TODO change to private field and add get method
    public HashMap<Integer, Meeting> meetings; // TODO change to private field and add get method
    public HashMap<Integer, AID> employeeAIDs; // TODO change to private field and add get method

    public Scheduler(HashMap<Integer, Group> groups, HashMap<Integer, Meeting> meetings) {
        this.groups = groups;
        this.meetings = meetings;
        this.employeeAIDs = new HashMap<>();
    }

    @Override
    protected void setup() {
        super.setup();
        /*
          TODO: access DF to get employee agents' AIDs
          TODO: commit suicide
         */
        addBehaviour(new SchedulerBehaviour(this, new ACLMessage(ACLMessage.CFP)));
    }

    public String getId() {
        return id;
    }

}
