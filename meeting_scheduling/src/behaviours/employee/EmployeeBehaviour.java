package behaviours.employee;

import agents.Employee;
import jade.core.behaviours.SequentialBehaviour;

public class EmployeeBehaviour extends SequentialBehaviour {

    public EmployeeBehaviour(Employee employee) {
        super(employee);

        addSubBehaviour(new EmployeeSendIDBehaviour(employee));
        for(int i = 0; i < employee.getMeetings(); i++) {
            addSubBehaviour(new EmployeeContractNetResponderBehaviour(employee));
        }
    }
}
