package behaviours.employee;

import agents.Employee;
import sajas.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EmployeeBehaviour extends SequentialBehaviour {

    public EmployeeBehaviour(Employee employee) {
        super(employee);

        addSubBehaviour(new EmployeeSendIDBehaviour(employee));
        /*
        for(int i = 0; i < employee.getMeetings(); i++) {
            addSubBehaviour(new EmployeeContractNetResponderBehaviour(employee));
        }
        */
        addSubBehaviour(new EmployeeResponderDispatcherBehaviour(employee, MessageTemplate.MatchAll()));
    }
}
