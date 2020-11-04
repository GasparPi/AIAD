package agents;

import behaviours.GetEmployeeIDsBehaviour;
import behaviours.SchedulerBehaviour;
import data.Group;
import data.Meeting;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class Scheduler extends Agent {

    private final static String ID = "SCHEDULER";
    private final static String SERVICE_TYPE = "meeting-scheduling";

    private final HashMap<Integer, Group> groups;
    private final HashMap<Integer, Meeting> meetings;
    private final ArrayList<AID> agentsAIDs;
    private final HashMap<Integer, AID> employeeAIDs;

    public Scheduler(HashMap<Integer, Group> groups, HashMap<Integer, Meeting> meetings) {
        this.groups = groups;
        this.meetings = meetings;
        this.agentsAIDs = new ArrayList<>();
        this.employeeAIDs = new HashMap<>();
    }

    @Override
    protected void setup() {

        try {
            this.searchEmployeeAIDs();
        } catch (FIPAException e) {
            System.err.println("Failed to search for Employee Agents in DF Service");
            e.printStackTrace();
            return;
        }

        addBehaviour(new GetEmployeeIDsBehaviour(this, this.agentsAIDs));
        addBehaviour(new SchedulerBehaviour(this, new ACLMessage(ACLMessage.CFP)));

        //TODO: commit suicide
    }

    public void searchEmployeeAIDs() throws FIPAException {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(SERVICE_TYPE);
        template.addServices(sd);

        DFAgentDescription[] searchResults = DFService.search(this, template);

        for (DFAgentDescription agentDescription : searchResults) {
            AID aid = agentDescription.getName();
            this.agentsAIDs.add(aid);
            System.out.println("Agent found. AID: " + aid.toString());
        }
    }

    public String getId() {
        return ID;
    }

    public HashMap<Integer, Group> getGroups() {
        return groups;
    }

    public HashMap<Integer, Meeting> getMeetings() {
        return meetings;
    }

    public HashMap<Integer, AID> getEmployeeAIDs() {
        return employeeAIDs;
    }

    public boolean hasAllEmployeeIDs() {
        return this.employeeAIDs.size() == this.agentsAIDs.size();
    }

    public void addEmployeeID(Integer id, AID aid) {
        this.employeeAIDs.put(id, aid);
    }

    public boolean hasEmployeeID(AID aid) {
        for (AID agentId : this.employeeAIDs.values()) {
            if (agentId.equals(aid))
                return true;
        }

        return false;
    }
}
