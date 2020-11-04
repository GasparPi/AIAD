package behaviours.scheduler;

import agents.Scheduler;
import jade.core.behaviours.SequentialBehaviour;

public class SchedulerBehaviour extends SequentialBehaviour {

    public SchedulerBehaviour(Scheduler scheduler) {
        super(scheduler);

        this.addSubBehaviour(new RequestEmployeeIDsBehaviour(scheduler));
        this.addSubBehaviour(new ReceiveEmployeeIDsBehaviour(scheduler));
        this.addSubBehaviour(new SchedulerContractNetInitiatorBehaviour(scheduler));
    }
}
