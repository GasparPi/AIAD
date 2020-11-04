package behaviours;

import agents.Scheduler;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class GetEmployeeIDsBehaviour extends Behaviour {

    private final ArrayList<AID> employeeAIDs;

    public GetEmployeeIDsBehaviour(Agent scheduler, ArrayList<AID> employeeAIDs) {
        super(scheduler);
        this.employeeAIDs = employeeAIDs;
    }

    @Override
    public void action() {
        for (AID aid : this.employeeAIDs) {
            ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
            aclMessage.addReceiver(aid);
            aclMessage.setContent("GET ID");
            this.myAgent.send(aclMessage);
        }


    }

    @Override
    public boolean done() {
        return ((Scheduler) this.myAgent).hasAllEmployeeIDs();
    }
}
