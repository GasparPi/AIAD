package agents;

import behaviours.scheduler.SchedulerBehaviour;
import data.Group;
import data.Meeting;
import jade.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import logger.MyLogger;

import java.util.ArrayList;
import java.util.HashMap;

public class Scheduler extends Agent {

    protected final static String ID = "SCHEDULER";
    protected final static String SERVICE_TYPE = "meeting-scheduling";

    private final ArrayList<Group> groups;
    private final ArrayList<Meeting> meetings;
    protected final ArrayList<AID> agentsAIDs;
    private final HashMap<Integer, AID> employeeAIDs;
    protected final MyLogger logger;
    protected int employeeNumber;

    public Scheduler(ArrayList<Group> groups, ArrayList<Meeting> meetings) {
        this.groups = groups;
        this.meetings = meetings;
        this.agentsAIDs = new ArrayList<>();
        this.employeeAIDs = new HashMap<>();
        this.logger = new MyLogger(ID);
        this.employeeNumber = 0;
    }

    @Override
    protected void setup() {
        /*
        try {
            this.searchEmployeeAIDs();
        } catch (FIPAException e) {
            System.err.println("Failed to search for Employee Agents in DF Service");
            e.printStackTrace();
            return;
        }
        */

        addBehaviour(new SearchBehaviour(this));
        //addBehaviour(new SchedulerBehaviour(this));
    }

    public void searchEmployeeAIDs() throws FIPAException {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(SERVICE_TYPE);
        template.addServices(sd);

        DFAgentDescription[] searchResults;
        do {
            searchResults = DFService.search(this, template);
        }while(searchResults.length < employeeNumber);

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

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public ArrayList<Meeting> getMeetings() {
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

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

}

class SearchBehaviour extends SimpleBehaviour{
    private boolean done = false;
    Scheduler myAgent;

    SearchBehaviour(Scheduler a){
        super();
        myAgent = a;
    }

    @Override
    public void action() {
        System.out.println("Scheduler will now seek agents.");

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(Scheduler.SERVICE_TYPE);
        template.addServices(sd);

        DFAgentDescription[] searchResults = new DFAgentDescription[0];
        try {
            searchResults = DFService.search(myAgent, template);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        if (searchResults.length < myAgent.employeeNumber)
            return;

        for (DFAgentDescription agentDescription : searchResults) {
            AID aid = (AID) agentDescription.getName();
            myAgent.agentsAIDs.add(aid);

            //Logger
            myAgent.logger.logInfo("FOUND AGENT IN DF SERVICE: AID: " + aid.getLocalName());
        }
        myAgent.addBehaviour(new SchedulerBehaviour(myAgent));
        done = true;
    }

    @Override
    public boolean done() {
        return done;
    }

}
