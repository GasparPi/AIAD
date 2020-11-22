package behaviours.scheduler;

import agents.Scheduler;
import sajas.core.behaviours.SequentialBehaviour;

public class SchedulerBehaviour extends SequentialBehaviour {

    public SchedulerBehaviour(Scheduler scheduler) {
        super(scheduler);

        this.addSubBehaviour(new RequestEmployeeIDsBehaviour(scheduler));
        this.addSubBehaviour(new ReceiveEmployeeIDsBehaviour(scheduler));
        for (int i : scheduler.getMeetings().keySet()) {
            this.addSubBehaviour(new SchedulerContractNetInitiatorBehaviour(scheduler, i));
        }

        this.addSubBehaviour(new SaveResultsBehaviour(scheduler));
    }
}
