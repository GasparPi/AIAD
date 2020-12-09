package behaviours.scheduler;

import agents.Scheduler;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;

import java.util.ArrayList;

public class TerminateEmployees extends Behaviour {
    private final ArrayList<AID> employeeAIDs;
    private final Scheduler scheduler;
    private boolean done = false;

    public TerminateEmployees(Scheduler scheduler) {
        super(scheduler);
        this.scheduler = scheduler;
        this.employeeAIDs = scheduler.getAgentsAIDs();
    }

    @Override
    public void action() {
        for (AID aid : this.employeeAIDs) {
            ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
            aclMessage.addReceiver(aid);
            aclMessage.setContent("TERMINATE");
            this.myAgent.send(aclMessage);

            // Logger
            this.scheduler.getLogger().logMessageContent("SENT TERMINATE", aclMessage.getContent(),"TO", aid);
            System.out.println("SENT TERMINATE " + aclMessage.getContent() + " TO " + aid);
        }

        this.done = true;
    }

    @Override
    public boolean done() {
        return this.done;
    }
}
