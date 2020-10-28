package behaviours;

import agents.Scheduler;
import data.MessageContent;
import data.TSPair;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class SchedulerBehaviour extends ContractNetInitiator {

    private ArrayList<TSPair> suggestions;
    private TSPair currentSuggestion;
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
                cfp = prepState1CFP(cfp);
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

        Vector v = new Vector();
        ACLMessage cfp = (ACLMessage) this.getDataStore().get(this.CFP_KEY);

        switch (state) {
            case 1:
                for( Object obj : responses){
                    MessageContent content;
                    try {
                         content = (MessageContent) ((ACLMessage) obj).getContentObject();
                        suggestions.add(new TSPair(content.getDay(), content.getTimeslot()));
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }


                }
                state = 2;
                cfp = prepState2CFP(cfp);

                v.add(cfp);
                newIteration(v);
                break;
            case 2:
                boolean acceptedByAll = true;
                for(Object obj : responses){
                    ACLMessage message = (ACLMessage) obj;
                    int id = 0;
                    boolean accept = false;
                    try {
                        id = ((MessageContent)message.getContentObject()).getEmployeeId();
                        accept = ((MessageContent)message.getContentObject()).getAcceptance();
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                    if(!accept && schedulerAgent.meetings.get(currentMeeting).getObligatoryEmployees().contains(id)){
                        acceptedByAll = false;
                    }
                }
                if(acceptedByAll){
                    ACLMessage acceptanceMessage = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);

                    MessageContent content = new MessageContent();
                    content.setState(2);
                    content.setDay(suggestions.get(0).getDay());
                    content.setTimeslot(suggestions.get(0).getTimeslot());
                    try {
                        acceptanceMessage.setContentObject(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for( Object obj : responses){
                        ACLMessage message = (ACLMessage) obj;
                        acceptanceMessage.addReceiver(message.getSender());
                    }
                    acceptanceMessage.setSender(schedulerAgent.getAID());
                    acceptances.add(acceptanceMessage);
                }
                else{
                    suggestions.remove(0);
                    if(suggestions.isEmpty()){
                        state = 1;
                        cfp = prepState1CFP(cfp);
                    }
                    else{
                        cfp = prepState2CFP(cfp);
                    }
                    v.add(cfp);
                    newIteration(v);
                }
                break;
            default:
                System.out.println("Something went horribly wrong.");
                schedulerAgent.doDelete();
        }

    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        ArrayList<Integer> accepters = new ArrayList<>();
        for(Object obj : resultNotifications){
            ACLMessage message = (ACLMessage) obj;
            if(message.getPerformative() == ACLMessage.INFORM) {
                try {
                    accepters.add(((MessageContent)message.getContentObject()).getEmployeeId());
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }
        boolean scheduledSuccessfully = true;
        for( int i : schedulerAgent.meetings.get(currentMeeting).getObligatoryEmployees()){
            if(accepters.contains(i)){
                System.out.println("Fucked up big time. yay.");
                scheduledSuccessfully = false;
            }
        }
        if(scheduledSuccessfully){
            schedulerAgent.meetings.get(currentMeeting).schedule(suggestions.get(0).getDay(), suggestions.get(0).getTimeslot(), suggestions.get(0).getTimeslot() + schedulerAgent.meetings.get(currentMeeting).getDuration() - 1);
        }
    }

    private ACLMessage prepState1CFP(ACLMessage cfp) {
        for (int id : schedulerAgent.groups.get(schedulerAgent.meetings.get(currentMeeting).getGroupId()).getEmployees()) {
            cfp.addReceiver(schedulerAgent.employeeAIDs.get(id));
        }
        cfp.setSender(schedulerAgent.getAID());
        MessageContent content = new MessageContent();
        content.setState(1);
        content.setMeetingDuration(schedulerAgent.meetings.get(currentMeeting).getDuration());
        try {
            cfp.setContentObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfp;
    }

    private ACLMessage prepState2CFP(ACLMessage cfp) {
        for (int id : schedulerAgent.groups.get(schedulerAgent.meetings.get(currentMeeting).getGroupId()).getEmployees()) {
            cfp.addReceiver(schedulerAgent.employeeAIDs.get(id));
        }
        cfp.setSender(schedulerAgent.getAID());
        MessageContent content = new MessageContent();
        content.setState(2);
        content.setDay(suggestions.get(0).getDay());
        content.setTimeslot(suggestions.get(0).getTimeslot());
        try {
            cfp.setContentObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfp;
    }
}
