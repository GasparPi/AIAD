package behaviours;

import agents.Employee;
import data.MessageContent;
import data.TSPair;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;

import java.io.IOException;
import java.util.ArrayList;

public class EmployeeBehaviour extends ContractNetResponder {

    private ArrayList<TSPair> timeslotPreference;
    private int currentSuggestion;
    private int meetingDuration;
    private Employee employeeAgent;

    public EmployeeBehaviour(Agent a, MessageTemplate mt, ArrayList<TSPair> timeslotPreference) {
        super(a, mt);
        this.timeslotPreference = timeslotPreference;
        this.currentSuggestion = 0;
        this.meetingDuration = 0;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {

        ACLMessage response = new ACLMessage(ACLMessage.PROPOSE);
        response.addReceiver(cfp.getSender());
        response.setSender(employeeAgent.getAID());
        try {
            MessageContent cfpContent = (MessageContent) cfp.getContentObject();
            MessageContent respContent = new MessageContent();
            respContent.setEmployeeId(employeeAgent.getId());
            if(cfpContent.getState() == 1){
                meetingDuration = cfpContent.getMeetingDuration();
                respContent.setState(1);
                while(timeslotPreference.get(currentSuggestion).getAvailableDuration() < cfpContent.getMeetingDuration() && currentSuggestion < timeslotPreference.size()){
                    currentSuggestion++;
                }
                if(currentSuggestion < timeslotPreference.size()){
                    respContent.setDay(timeslotPreference.get(currentSuggestion).getDay());
                    respContent.setTimeslot(timeslotPreference.get(currentSuggestion).getTimeslot());
                }
            }
            else if(cfpContent.getState() == 2){
                respContent.setState(2);
                boolean acceptance = false;
                for (TSPair ts : timeslotPreference){
                    if(ts.getDay() == cfpContent.getDay() && ts.getTimeslot() == cfpContent.getTimeslot()){
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
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
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
