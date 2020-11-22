package behaviours.scheduler;

import agents.Scheduler;
import behaviours.SchedulingState;
import data.Meeting;
import data.MessageContent;
import data.TSPair;
import sajas.core.AID;
import sajas.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import sajas.proto.ContractNetInitiator;
import jade.util.leap.Iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class SchedulerContractNetInitiatorBehaviour extends ContractNetInitiator {

    private final ArrayList<TSPair> suggestions;
    private TSPair currentSuggestion;
    private SchedulingState state; //1 when asking for suggestions, 2 when deciding timeslot
    private final Scheduler schedulerAgent;
    private final int currentMeeting;
    private boolean initiating;

    public SchedulerContractNetInitiatorBehaviour(Scheduler scheduler, int currentMeeting) {
        super(scheduler, new ACLMessage(ACLMessage.CFP));

        this.suggestions = new ArrayList<>();
        this.state = SchedulingState.REQUEST_TIMESLOTS;
        this.schedulerAgent = scheduler;
        this.currentMeeting = currentMeeting;
        this.initiating = true;
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {

        cfp.clearAllReceiver();

        /*
        for (int id : schedulerAgent.getGroups().get(schedulerAgent.getMeetings().get(currentMeeting).getGroupId()).getEmployees()) {
            cfp.addReceiver(schedulerAgent.getEmployeeAIDs().get(id));
        }
         */

        switch (state) {
            case REQUEST_TIMESLOTS -> prepState1CFP(cfp);
            case DECIDE_TIMESLOTS -> prepState2CFP(cfp);
            default -> {
                System.out.println("Something went terribly wrong.");
                schedulerAgent.doDelete();
            }
        }

        //Logger
        MessageContent cfpContent = null;
        try {
            cfpContent = (MessageContent) cfp.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        ArrayList<AID> receivers = new ArrayList<>();
        Iterator receiverIt = cfp.getAllReceiver();
        while (receiverIt.hasNext()) {
            AID receiver = (AID) receiverIt.next();
            receivers.add(receiver);
        }
        this.schedulerAgent.getLogger().logMessageContent("PREPARED CFP", cfpContent, "TO", receivers);

        return super.prepareCfps(cfp);
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

        Vector v = new Vector();
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);

        if(cfp == null)
            System.out.println("CFP IS NULL : " + this.CFP_KEY);

        switch (state) {
            case REQUEST_TIMESLOTS -> {
                for (Object obj : responses) {
                    ACLMessage aclMessage = (ACLMessage) obj;
                    MessageContent content;
                    try {
                        content = (MessageContent) aclMessage.getContentObject();
                        if(!content.getDay().equals("") && content.getTimeslot() != -1) {
                            suggestions.add(new TSPair(content.getDay(), content.getTimeslot()));
                        }

                        // Logger
                        this.schedulerAgent.getLogger().logMessageContent("RECEIVED PROPOSAL", content,"FROM", (AID) aclMessage.getSender());
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }


                }
                if(suggestions.isEmpty()){
                    ACLMessage rejectionMessage = new ACLMessage(ACLMessage.REJECT_PROPOSAL);

                    MessageContent content = new MessageContent();
                    content.setState(SchedulingState.DECIDE_TIMESLOTS);
                    try {
                        rejectionMessage.setContentObject(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ArrayList<AID> receivers = new ArrayList<>();
                    for (Object obj : responses) {
                        ACLMessage message = (ACLMessage) obj;
                        rejectionMessage.addReceiver(message.getSender());
                        receivers.add((AID) message.getSender());
                    }
                    rejectionMessage.setSender(schedulerAgent.getAID());
                    rejectionMessage.setConversationId("MEETING" + currentMeeting);
                    responses.add(rejectionMessage);
                    this.schedulerAgent.getLogger().logMessageContent("PREPARED REJECT_PROPOSAL", content, "TO", receivers);
                    schedulerAgent.getMeetings().get(currentMeeting).schedule("NULL", -1, -1, new ArrayList<>());
                    ((SequentialBehaviour)getParent()).removeSubBehaviour(this);
                }
                else {
                    state = SchedulingState.DECIDE_TIMESLOTS;
                    prepState2CFP(cfp);
                    v.add(cfp);
                    newIteration(v);
                }
            }
            case DECIDE_TIMESLOTS -> {
                boolean acceptedByAll = true;
                for (Object obj : responses) {
                    ACLMessage message = (ACLMessage) obj;

                    int id = 0;
                    boolean accept = false;
                    MessageContent content;
                    try {
                        content = (MessageContent) message.getContentObject();
                        id = content.getEmployeeId();
                        accept = content.getAcceptance();

                        // Logger
                        this.schedulerAgent.getLogger().logMessageContent("RECEIVED PROPOSAL", content, "FROM", (AID) message.getSender());

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

                    ArrayList<AID> receivers = new ArrayList<>();
                    for (Object obj : responses) {
                        ACLMessage message = (ACLMessage) obj;
                        acceptanceMessage.addReceiver(message.getSender());
                        receivers.add((AID) message.getSender());
                    }
                    acceptanceMessage.setSender(schedulerAgent.getAID());
                    acceptanceMessage.setConversationId("MEETING" + currentMeeting);
                    acceptances.add(acceptanceMessage);
                    this.schedulerAgent.getLogger().logMessageContent("PREPARED ACCEPT_PROPOSAL", content, "TO", receivers);
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
        Meeting currentMeetingObject = schedulerAgent.getMeetings().get(currentMeeting);

        ArrayList<Integer> acceptors = new ArrayList<>();
        for (Object obj : resultNotifications){
            ACLMessage message = (ACLMessage) obj;

            try {
                MessageContent content = (MessageContent) message.getContentObject();

                // Logger
                this.schedulerAgent.getLogger().logMessageContent("RECEIVED RESULT NOTIFICATION", content, "FROM", (AID) message.getSender());

                if (message.getPerformative() == ACLMessage.INFORM)
                    acceptors.add(content.getEmployeeId());


            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }

        boolean scheduledSuccessfully = true;
        for ( int i : currentMeetingObject.getObligatoryEmployees()){
            if (!acceptors.contains(i)){
                System.out.println("Something went dreadfully wrong.");
                scheduledSuccessfully = false;
            }
        }
        if (scheduledSuccessfully){
            TSPair firstSuggestion = suggestions.get(0);
            currentMeetingObject.schedule(firstSuggestion.getDay(), firstSuggestion.getTimeslot(), firstSuggestion.getTimeslot() + currentMeetingObject.getDuration() - 1, acceptors);
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
        if(initiating){
            initiating = false;
        }
        else{
        }
        cfp.setConversationId("MEETING" + currentMeeting);
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
        cfp.setConversationId("MEETING" + currentMeeting);
    }
}
