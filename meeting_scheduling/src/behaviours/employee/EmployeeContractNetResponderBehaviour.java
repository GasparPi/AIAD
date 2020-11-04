package behaviours.employee;

import agents.Employee;
import behaviours.SchedulingState;
import data.MessageContent;
import data.TSPair;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;

import java.io.IOException;
import java.util.ArrayList;

public class EmployeeContractNetResponderBehaviour extends ContractNetResponder {

    private final ArrayList<TSPair> timeslotPreference;
    private int currentSuggestion;
    private int meetingDuration;
    private final Employee employeeAgent;

    public EmployeeContractNetResponderBehaviour(Employee employee) {
        super(employee, MessageTemplate.MatchPerformative(ACLMessage.CFP));

        this.employeeAgent = employee;
        this.timeslotPreference = employee.getTimeSlotPreference();
        this.currentSuggestion = 0;
        this.meetingDuration = 0;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) {

        ACLMessage response = new ACLMessage(ACLMessage.PROPOSE);
        response.addReceiver(cfp.getSender());
        response.setSender(employeeAgent.getAID());

        MessageContent cfpContent;

        try {
                System.out.println(cfp.getContentObject().toString());
                cfpContent = (MessageContent) cfp.getContentObject();
            MessageContent respContent = new MessageContent();
            respContent.setEmployeeId(employeeAgent.getId());
            if(cfpContent.getState() == SchedulingState.REQUEST_TIMESLOTS){
                meetingDuration = cfpContent.getMeetingDuration();
                respContent.setState(SchedulingState.REQUEST_TIMESLOTS);
                while(timeslotPreference.get(currentSuggestion).getAvailableDuration() < cfpContent.getMeetingDuration() && currentSuggestion < timeslotPreference.size()){
                    currentSuggestion++;
                }
                if(currentSuggestion < timeslotPreference.size()){
                    respContent.setDay(timeslotPreference.get(currentSuggestion).getDay());
                    respContent.setTimeslot(timeslotPreference.get(currentSuggestion).getTimeslot());
                }
            }
            else if(cfpContent.getState() == SchedulingState.DECIDE_TIMESLOTS){
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
            response.setContentObject(respContent);
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
            if(((MessageContent)propose.getContentObject()).getAcceptance()){
                response = new ACLMessage(ACLMessage.INFORM);
                response.addReceiver(accept.getSender());
                response.setSender(employeeAgent.getAID());
                MessageContent acceptContent = (MessageContent) accept.getContentObject();
                TSPair ts = new TSPair(acceptContent.getDay(), acceptContent.getTimeslot());
                employeeAgent.removeAvailability(ts, meetingDuration);
                response.setContentObject(respContent);
                return response;
            }
        } catch (UnreadableException | IOException e) {
            e.printStackTrace();
        }

        response = new ACLMessage(ACLMessage.FAILURE);
        response.addReceiver(accept.getSender());
        try {
            response.setContentObject(respContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
