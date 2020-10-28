package behaviours;

import agents.Scheduler;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class GetEmployeeIDsBehaviour extends Behaviour {

    private final Scheduler scheduler;
    private final ArrayList<AID> employeeAIDs;

    public GetEmployeeIDsBehaviour(Scheduler scheduler, ArrayList<AID> employeeAIDs) {
        this.scheduler = scheduler;
        this.employeeAIDs = employeeAIDs;
    }

    @Override
    public void action() {
        for (AID aid : this.employeeAIDs) {
            ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
            aclMessage.addReceiver(aid);
            aclMessage.setContent("GET ID");
            this.scheduler.send(aclMessage);
        }
    }

    @Override
    public boolean done() {
        return this.scheduler.hasAllEmployeeIDs();
    }
}
