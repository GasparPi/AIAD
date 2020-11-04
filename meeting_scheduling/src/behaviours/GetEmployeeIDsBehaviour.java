package behaviours;

import agents.Scheduler;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;

public class GetEmployeeIDsBehaviour extends Behaviour {

    private final ArrayList<AID> employeeAIDs;
    private Scheduler scheduler;
    private boolean sentMessages = false;

    public GetEmployeeIDsBehaviour(Scheduler scheduler, ArrayList<AID> employeeAIDs) {
        super(scheduler);
        this.scheduler = scheduler;
        this.employeeAIDs = employeeAIDs;
    }

    @Override
    public void action() {
        if (!this.sentMessages) {

            for (AID aid : this.employeeAIDs) {
                ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
                aclMessage.addReceiver(aid);
                aclMessage.setContent("GET ID");
                this.myAgent.send(aclMessage);
                System.out.println("[Scheduler] sent REQUEST message to " + aid.toString());
            }
        }

        this.sentMessages = true;

        ACLMessage msg = this.scheduler.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
        if (msg != null) {
            System.out.println("[Scheduler] received INFORM msg: " + msg.getContent());
            this.scheduler.addEmployeeID(parseEmployeeId(msg.getContent()), msg.getSender());
        }
    }

    @Override
    public boolean done() {
        return this.sentMessages;
    }

    private static int parseEmployeeId(String messageContent) {
        return Integer.parseInt(messageContent.split(":")[1]);
    }
}
