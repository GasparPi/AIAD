package behaviours.employee;

import agents.Employee;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EmployeeSendIDBehaviour extends Behaviour {

    private boolean receivedRequest = false;
    private Employee employee;

    public EmployeeSendIDBehaviour(Employee e) {
        super(e);
        this.employee = e;
    }

    @Override
    public void action() {
        ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        if (msg != null) {
            this.receivedRequest = true;
            int employeeId = employee.getId();

            // Logger
            this.employee.getLogger().logMessageContent("RECEIVED REQUEST", msg.getContent(),  "FROM", msg.getSender());

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("ID:" + employeeId);
            this.employee.send(reply);

            // Logger
            this.employee.getLogger().logMessageContent("SENT INFORM", reply.getContent(),"TO", msg.getSender());
        }
    }

    @Override
    public boolean done() {
        return receivedRequest;
    }
}
