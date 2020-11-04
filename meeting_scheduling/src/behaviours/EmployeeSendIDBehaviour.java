package behaviours;
import agents.Employee;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EmployeeSendIDBehaviour extends Behaviour {

    private final boolean receivedRequest = false;

    public EmployeeSendIDBehaviour(Employee e, MessageTemplate mt) {
        super(e);
    }

    @Override
    public void action() {
        ACLMessage msg = this.myAgent.receive();
        if (msg != null) {
            int employeeId = ((Employee) myAgent).getId();
            System.out.println("[Employee " + employeeId + "] received a request msg: " + msg.getContent());
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("ID:" + employeeId);
            this.myAgent.send(reply);
        }

    }

    @Override
    public boolean done() {
        return this.receivedRequest;
    }
}
