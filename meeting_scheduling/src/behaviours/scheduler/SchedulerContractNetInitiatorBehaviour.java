package behaviours.scheduler;

import agents.Scheduler;
import behaviours.SchedulingState;
import data.MessageContent;
import data.TSPair;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class SchedulerContractNetInitiatorBehaviour extends ContractNetInitiator {

    private final ArrayList<TSPair> suggestions;
    private TSPair currentSuggestion;
    private SchedulingState state; //1 when asking for suggestions, 2 when deciding timeslot
    private final Scheduler schedulerAgent;
    private int currentMeeting;

    public SchedulerContractNetInitiatorBehaviour(Scheduler scheduler, int currentMeeting) {
        super(scheduler, new ACLMessage(ACLMessage.CFP));

        this.suggestions = new ArrayList<>();
        this.state = SchedulingState.REQUEST_TIMESLOTS;
        this.schedulerAgent = scheduler;
        this.currentMeeting = currentMeeting;
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {

        cfp.clearAllReceiver();

        switch (state) {
            case REQUEST_TIMESLOTS -> prepState1CFP(cfp);
            case DECIDE_TIMESLOTS -> prepState2CFP(cfp);
            default -> {
                System.out.println("Something went terribly wrong.");
                schedulerAgent.doDelete();
            }
        }

        return super.prepareCfps(cfp);
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

        Vector v = new Vector();
        ACLMessage cfp = (ACLMessage) this.getDataStore().get(this.CFP_KEY);

        switch (state) {
            case REQUEST_TIMESLOTS -> {
                for (Object obj : responses) {
                    MessageContent content;
                    try {
                        content = (MessageContent) ((ACLMessage) obj).getContentObject();
                        suggestions.add(new TSPair(content.getDay(), content.getTimeslot()));
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }


                }
                state = SchedulingState.DECIDE_TIMESLOTS;
                prepState2CFP(cfp);
                v.add(cfp);
                newIteration(v);
            }
            case DECIDE_TIMESLOTS -> {
                boolean acceptedByAll = true;
                for (Object obj : responses) {
                    ACLMessage message = (ACLMessage) obj;
                    int id = 0;
                    boolean accept = false;
                    try {
                        id = ((MessageContent) message.getContentObject()).getEmployeeId();
                        accept = ((MessageContent) message.getContentObject()).getAcceptance();
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                    if (!accept && schedulerAgent.getMeetings().get(currentMeeting).getObligatoryEmployees().contains(id)) {
                        acceptedByAll = false;
                    }
                }
                if (acceptedByAll) {
                    ACLMessage acceptanceMessage = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);

                    MessageContent content = new MessageContent();
                    content.setState(SchedulingState.DECIDE_TIMESLOTS);
                    content.setDay(suggestions.get(0).getDay());
                    content.setTimeslot(suggestions.get(0).getTimeslot());
                    try {
                        acceptanceMessage.setContentObject(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (Object obj : responses) {
                        ACLMessage message = (ACLMessage) obj;
                        acceptanceMessage.addReceiver(message.getSender());
                    }
                    acceptanceMessage.setSender(schedulerAgent.getAID());
                    acceptances.add(acceptanceMessage);
                } else {
                    suggestions.remove(0);
                    if (suggestions.isEmpty()) {
                        state = SchedulingState.REQUEST_TIMESLOTS;
                        prepState1CFP(cfp);
                    } else {
                        prepState2CFP(cfp);
                    }
                    v.add(cfp);
                    newIteration(v);
                }
            }
            default -> {
                System.out.println("Something went horribly wrong.");
                schedulerAgent.doDelete();
            }
        }

    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        ArrayList<Integer> acceptors = new ArrayList<>();
        for(Object obj : resultNotifications){
            ACLMessage message = (ACLMessage) obj;
            if(message.getPerformative() == ACLMessage.INFORM) {
                try {
                    acceptors.add(((MessageContent)message.getContentObject()).getEmployeeId());
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }
        boolean scheduledSuccessfully = true;
        for( int i : schedulerAgent.getMeetings().get(currentMeeting).getObligatoryEmployees()){
            if(acceptors.contains(i)){
                System.out.println("Fucked up big time. yay.");
                scheduledSuccessfully = false;
            }
        }
        if(scheduledSuccessfully){
            schedulerAgent.getMeetings().get(currentMeeting).schedule(suggestions.get(0).getDay(), suggestions.get(0).getTimeslot(), suggestions.get(0).getTimeslot() + schedulerAgent.getMeetings().get(currentMeeting).getDuration() - 1);
        }
    }

    private void prepState1CFP(ACLMessage cfp) {
        for (int id : schedulerAgent.getGroups().get(schedulerAgent.getMeetings().get(currentMeeting).getGroupId()).getEmployees()) {
            cfp.addReceiver(schedulerAgent.getEmployeeAIDs().get(id));
        }
        cfp.setSender(schedulerAgent.getAID());
        MessageContent content = new MessageContent();
        content.setState(SchedulingState.REQUEST_TIMESLOTS);
        content.setMeetingDuration(schedulerAgent.getMeetings().get(currentMeeting).getDuration());
        try {
            cfp.setContentObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepState2CFP(ACLMessage cfp) {
        for (int id : schedulerAgent.getGroups().get(schedulerAgent.getMeetings().get(currentMeeting).getGroupId()).getEmployees()) {
            cfp.addReceiver(schedulerAgent.getEmployeeAIDs().get(id));
        }
        cfp.setSender(schedulerAgent.getAID());
        MessageContent content = new MessageContent();
        content.setState(SchedulingState.DECIDE_TIMESLOTS);
        content.setDay(suggestions.get(0).getDay());
        content.setTimeslot(suggestions.get(0).getTimeslot());
        try {
            cfp.setContentObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
