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
            System.out.println("Scheduler sent request message with aid " + aid.toString());
        }

        ACLMessage msg = this.myAgent.receive();
        if (msg != null) {
            System.out.println("[Scheduler] received an inform msg: " + msg.getContent());
            ((Scheduler) myAgent).addEmployeeID(parseEmployeeId(msg.getContent()), msg.getSender());
        }

    }

    @Override
    public boolean done() {
        return ((Scheduler) this.myAgent).hasAllEmployeeIDs();
    }

    private int parseEmployeeId(String messageContent) {
        return Integer.parseInt(messageContent.split(":")[1]);
    }
}
