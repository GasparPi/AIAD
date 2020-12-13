package behaviours.scheduler;

import agents.Scheduler;
import data.MessageContent;
import jade.core.AID;
import sajas.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.tools.sniffer.Message;

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

            // Logger
            this.scheduler.getLogger().logMessageContent("SENT REQUEST", aclMessage.getContent(),"TO", aid);
        }

        this.done = true;
    }

    @Override
    public boolean done() {
        return this.done;
    }
}
