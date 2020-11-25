package behaviours.employee;

import agents.Employee;
import behaviours.SchedulingState;
import data.Meeting;
import data.MessageContent;
import data.TSPair;
import jade.core.AID;
import sajas.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import sajas.proto.SSIteratedContractNetResponder;

import java.io.IOException;
import java.util.ArrayList;

public class EmployeeContractNetResponderBehaviour extends SSIteratedContractNetResponder {

    private int currentSuggestion;
    private int meetingDuration;
    private final Employee employeeAgent;
    private final ArrayList<TSPair> timeslotPreference;
    private final String conversationId;

    public EmployeeContractNetResponderBehaviour(Agent employee, ACLMessage cfp) {
        super(employee, cfp);

        this.employeeAgent = (Employee) employee;
        this.timeslotPreference = employeeAgent.sortAgendaByPreference();
        this.currentSuggestion = 0;
        this.meetingDuration = 0;
        conversationId = cfp.getConversationId();
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) {

        ACLMessage response = new ACLMessage(ACLMessage.PROPOSE);
        response.addReceiver(cfp.getSender());
        response.setSender(employeeAgent.getAID());
        response.setConversationId(conversationId);

        MessageContent cfpContent;

        try {
            cfpContent = (MessageContent) cfp.getContentObject();
            MessageContent respContent = new MessageContent();

            //Logger
            this.employeeAgent.getLogger().logMessageContent("RECEIVED CFP", cfpContent,"FROM", cfp.getSender());

            respContent.setEmployeeId(employeeAgent.getId());

            switch (cfpContent.getState()) {
                case REQUEST_TIMESLOTS -> {
                    respContent.setState(SchedulingState.REQUEST_TIMESLOTS);

                    meetingDuration = cfpContent.getMeetingDuration();


                    while (currentSuggestion < timeslotPreference.size()){
                        if(timeslotPreference.get(currentSuggestion).getAvailableDuration() < cfpContent.getMeetingDuration()) {
                            currentSuggestion++;
                        }
                        else {
                            break;
                        }
                    }

                    if (currentSuggestion < timeslotPreference.size()){
                        respContent.setDay(timeslotPreference.get(currentSuggestion).getDay());
                        respContent.setTimeslot(timeslotPreference.get(currentSuggestion).getTimeslot());
                        currentSuggestion++;
                    }
                }
                case DECIDE_TIMESLOTS -> {
                    respContent.setState(SchedulingState.DECIDE_TIMESLOTS);

                    boolean acceptance = false;
                    for (TSPair ts : timeslotPreference) {
                        if(ts.getDay().equals(cfpContent.getDay()) && ts.getTimeslot() == cfpContent.getTimeslot()){
                            if(ts.getAvailableDuration() >= meetingDuration){
                                acceptance = true;
                                break;
                            }
                        }
                    }
                    respContent.setAcceptance(acceptance);
                }
            }

            response.setContentObject(respContent);

            //Logger
            this.employeeAgent.getLogger().logMessageContent("SENDING PROPOSAL", respContent, "TO", cfp.getSender());

        } catch (UnreadableException | IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
        ACLMessage response;
        MessageContent respContent = new MessageContent();
        respContent.setEmployeeId(employeeAgent.getId());
        try {
            MessageContent acceptContent = (MessageContent) accept.getContentObject();

            //Logger
            this.employeeAgent.getLogger().logMessageContent("RECEIVED ACCEPT PROPOSAL", acceptContent, "FROM", accept.getSender());

            MessageContent proposeContent = (MessageContent) propose.getContentObject();
            if (proposeContent.getAcceptance()){
                response = new ACLMessage(ACLMessage.INFORM);
                response.addReceiver(accept.getSender());
                response.setSender(employeeAgent.getAID());
                response.setConversationId(conversationId);

                TSPair ts = new TSPair(acceptContent.getDay(), acceptContent.getTimeslot());
                employeeAgent.removeAvailability(ts, meetingDuration);
                response.setContentObject(respContent);

                //Logger
                this.employeeAgent.getLogger().logMessageContent("RESPONSE TO ACCEPT PROPOSAL", respContent,"TO", accept.getSender());

                return response;
            }
        } catch (UnreadableException | IOException e) {
            e.printStackTrace();
        }

        response = new ACLMessage(ACLMessage.FAILURE);
        response.addReceiver(accept.getSender());
        response.setConversationId(conversationId);

        try {
            response.setContentObject(respContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Logger
        this.employeeAgent.getLogger().logMessageContent("RESPONSE TO ACCEPT PROPOSAL",  respContent,  "TO", accept.getSender());

        return response;
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {

        try {
            MessageContent rejectContent = (MessageContent) reject.getContentObject();
            this.employeeAgent.getLogger().logMessageContent("RECEIVED REJECT PROPOSAL", rejectContent, "FROM", reject.getSender());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handleOutOfSequence(ACLMessage message) {
        try {
            MessageContent content = (MessageContent) message.getContentObject();
            //Logger
            this.employeeAgent.getLogger().logMessageContent("OUT OF SEQUENCE",  content,  "FROM", message.getSender());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
