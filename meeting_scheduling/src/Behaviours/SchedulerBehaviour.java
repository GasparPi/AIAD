package Behaviours;

import agents.Scheduler;
import data.Timeslot;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.ArrayList;
import java.util.Vector;

public class SchedulerBehaviour extends ContractNetInitiator {

    private ArrayList<Integer> suggestions;
    private Timeslot currentSuggestion;
    private int state; //1 when asking for suggestions, 2 when deciding timeslot
    private Scheduler schedulerAgent;
    private int currentMeeting;

    public SchedulerBehaviour(Scheduler a, ACLMessage cfp) {
        super(a, cfp);
        suggestions = new ArrayList<>();
        state = 1;
        schedulerAgent = a;
        currentMeeting = 1;
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {

        cfp.clearAllReceiver();

        switch (state) {
            case 1:
                for (int id : schedulerAgent.groups.get(schedulerAgent.meetings.get(currentMeeting).getGroupId()).getEmployees()) {
                    cfp.addReceiver(schedulerAgent.employeeAIDs.get(id));
                }
                cfp.setOntology("Timeslot_Request_Ontology");
                cfp.setContent("" + schedulerAgent.meetings.get(currentMeeting).getDuration());
                break;

            case 2:
                cfp = prepState2CFP(cfp);
                break;

            default:
                System.out.println("Something went terribly wrong.");
                schedulerAgent.doDelete();
        }

        return super.prepareCfps(cfp);
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

        switch (state) {
            case 1:
                for( Object obj : responses){
                    ACLMessage message = (ACLMessage) obj;
                    suggestions.add(Integer.parseInt(message.getContent()));
                }
                state = 2;
                ACLMessage cfp = prepState2CFP((ACLMessage)this.getDataStore().get(this.CFP_KEY));
                Vector v = new Vector();
                v.add(cfp);
                newIteration(v);

                break;
            case 2:
                

                break;
            default:
                System.out.println("Something went horribly wrong.");
                schedulerAgent.doDelete();
        }

    }

    private ACLMessage prepState2CFP(ACLMessage cfp) {
        for (int id : schedulerAgent.groups.get(schedulerAgent.meetings.get(currentMeeting).getGroupId()).getEmployees()) {
            cfp.addReceiver(schedulerAgent.employeeAIDs.get(id));
        }
        cfp.setOntology("Timeslot_Decision_Ontology");
        cfp.setContent("" + suggestions.get(0));
        return cfp;
    }
}
