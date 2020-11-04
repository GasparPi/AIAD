package behaviours;

import agents.Scheduler;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class GetEmployeeIDsBehaviour extends Behaviour {

    private final ArrayList<AID> employeeAIDs;
    private Scheduler scheduler;

    public GetEmployeeIDsBehaviour(Scheduler scheduler, ArrayList<AID> employeeAIDs) {
        super(scheduler);
        this.scheduler = scheduler;
        this.employeeAIDs = employeeAIDs;
    }

    @Override
    public void action() {
        for (AID aid : this.employeeAIDs) {
            if (!this.scheduler.hasEmployeeID(aid)) {
                ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
                aclMessage.addReceiver(aid);
                aclMessage.setContent("GET ID");
                this.myAgent.send(aclMessage);
                System.out.println("[Scheduler] sent REQUEST message to " + aid.toString());
            }
        }

        ACLMessage msg = this.scheduler.receive();
        if (msg != null) {
            System.out.println("[Scheduler] received INFORM msg: " + msg.getContent());
            this.scheduler.addEmployeeID(parseEmployeeId(msg.getContent()), msg.getSender());
        }
    }

    @Override
    public boolean done() {
        return this.scheduler.hasAllEmployeeIDs();
    }

    private static int parseEmployeeId(String messageContent) {
        return Integer.parseInt(messageContent.split(":")[1]);
    }
}
