package agents;

import behaviours.scheduler.SchedulerBehaviour;
import data.Group;
import data.Meeting;
import jade.core.AID;
import sajas.core.Agent;
import sajas.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import logger.MyLogger;

import java.util.ArrayList;
import java.util.HashMap;

public class Scheduler extends Agent {

    private final static String ID = "SCHEDULER";
    private final static String SERVICE_TYPE = "meeting-scheduling";

    private final HashMap<Integer, Group> groups;
    private final HashMap<Integer, Meeting> meetings;
    private final ArrayList<AID> agentsAIDs;
    private final HashMap<Integer, AID> employeeAIDs;
    private final MyLogger logger;

    public Scheduler(HashMap<Integer, Group> groups, HashMap<Integer, Meeting> meetings, String logsDir) {
        this.groups = groups;
        this.meetings = meetings;
        this.agentsAIDs = new ArrayList<>();
        this.employeeAIDs = new HashMap<>();
        this.logger = new MyLogger(logsDir, ID);
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

        addBehaviour(new SchedulerBehaviour(this));
    }

    public void searchEmployeeAIDs() throws FIPAException {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(SERVICE_TYPE);
        template.addServices(sd);

        DFAgentDescription[] searchResults = DFService.search(this, template);

        for (DFAgentDescription agentDescription : searchResults) {
            AID aid = (AID) agentDescription.getName();
            this.agentsAIDs.add(aid);

            //Logger
            this.logger.logInfo("FOUND AGENT IN DF SERVICE: AID: " + aid.getLocalName());
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

    public ArrayList<AID> getAgentsAIDs() {
        return this.agentsAIDs;
    }

    public void addEmployeeID(Integer id, AID aid) {
        this.employeeAIDs.put(id, aid);
    }

    public boolean hasAllEmployeeIDs() {
        return this.agentsAIDs.size() == this.employeeAIDs.size();
    }

    public MyLogger getLogger() {
        return logger;
    }
}
