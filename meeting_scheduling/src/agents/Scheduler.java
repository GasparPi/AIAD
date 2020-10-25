package agents;

import Behaviours.SchedulerBehaviour;
import data.Group;
import data.Meeting;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

public class Scheduler extends Agent {

    public HashMap<Integer, Meeting> meetings;
    public HashMap<Integer, Group> groups;
    public HashMap<Integer, AID> employeeAIDs;

    @Override
    protected void setup() {
        super.setup();
        /*TODO: parse meetings
          TODO: parse groups
          TODO: access DF to get employee agents' AIDs
          TODO: commit suicide
         */
        addBehaviour(new SchedulerBehaviour(this, new ACLMessage(ACLMessage.CFP)));
    }
}
