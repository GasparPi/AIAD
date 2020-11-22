package behaviours.employee;

import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sajas.proto.SSResponderDispatcher;

public class EmployeeResponderDispatcherBehaviour extends SSResponderDispatcher {

    public EmployeeResponderDispatcherBehaviour(Agent a, MessageTemplate tpl){
        super(a, tpl);
    }

    @Override
    protected Behaviour createResponder(ACLMessage aclMessage) {
        //if(aclMessage.getOntology().equals("INIT")) {
            return new EmployeeContractNetResponderBehaviour(getAgent(), aclMessage);
        /*}
        else{
            return new SimpleBehaviour() {
                @Override
                public void action() {

                }

                @Override
                public boolean done() {
                    return false;
                }
            };
        }

         */
    }
}
