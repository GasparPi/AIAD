package behaviours.scheduler;

import agents.Scheduler;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveEmployeeIDsBehaviour extends Behaviour {

    private final Scheduler scheduler;

    public ReceiveEmployeeIDsBehaviour(Scheduler scheduler) {
        super(scheduler);
        this.scheduler = scheduler;
    }

    @Override
    public void action() {
        ACLMessage msg = this.scheduler.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
        if (msg != null) {

            // Logger
            this.scheduler.getLogger().logMessageContent("RECEIVED INFORM", msg.getContent(),"FROM", msg.getSender());

            this.scheduler.addEmployeeID(parseEmployeeId(msg.getContent()), msg.getSender());
        }

    }

    private static int parseEmployeeId(String messageContent) {
        return Integer.parseInt(messageContent.split(":")[1]);
    }

    @Override
    public boolean done() {
        return this.scheduler.hasAllEmployeeIDs();
    }
}
