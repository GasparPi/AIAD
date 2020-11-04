package behaviours.scheduler;

import agents.Scheduler;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class RequestEmployeeIDsBehaviour extends Behaviour {

    private final ArrayList<AID> employeeAIDs;
    private final Scheduler scheduler;
    private boolean done = false;

    public RequestEmployeeIDsBehaviour(Scheduler scheduler) {
        super(scheduler);
        this.scheduler = scheduler;
        this.employeeAIDs = scheduler.getAgentsAIDs();
    }

    @Override
    public void action() {
        for (AID aid : this.employeeAIDs) {
            ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
            aclMessage.addReceiver(aid);
            aclMessage.setContent("GET ID");
            this.myAgent.send(aclMessage);
            System.out.println("[Scheduler] sent REQUEST message to " + aid.toString());
        }

        this.done = true;
    }

    @Override
    public boolean done() {
        return this.done;
    }
}
