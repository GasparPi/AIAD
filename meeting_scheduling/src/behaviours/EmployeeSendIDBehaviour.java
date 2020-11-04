package behaviours;
import agents.Employee;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EmployeeSendIDBehaviour extends Behaviour {

    private boolean receivedRequest = false;

    public EmployeeSendIDBehaviour(Employee e, MessageTemplate mt) {
        super(e);
    }

    @Override
    public void action() {
        ACLMessage msg = this.myAgent.receive();
        if (msg != null) {
            this.receivedRequest = true;
            int employeeId = ((Employee) myAgent).getId();
            System.out.println("[Employee " + employeeId + "] received REQUEST msg: " + msg.getContent());
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("ID:" + employeeId);
            this.myAgent.send(reply);
            System.out.println("[Employee " + employeeId + "] sent INFORM msg: " + reply.getContent());
        }

    }

    @Override
    public boolean done() {
        return this.receivedRequest;
    }
}
