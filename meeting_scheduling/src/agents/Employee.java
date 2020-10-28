package agents;

import data.Macros;
import data.TSPair;
import data.Timeslot;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;
import java.util.HashMap;

public class Employee extends Agent {
    private final static String SERVICE_TYPE = "meeting-scheduling";

    private final int id;
    private HashMap<String, ArrayList<Timeslot>> agenda;

    public Employee(int id, HashMap<String, ArrayList<Timeslot>> agenda) {
        this.id = id;
        this.agenda = agenda;
    }

    public int getId() {
        return id;
    }

    public HashMap<String, ArrayList<Timeslot>> getAgenda() {
        return agenda;
    }

    @Override
    public String toString(){
        return "----------" + '\n' + "id: " + id + '\n' +
            agendaDayToString(Macros.MONDAY) + '\n' +
            agendaDayToString(Macros.TUESDAY) + '\n' +
            agendaDayToString(Macros.WEDNESDAY) + '\n' +
            agendaDayToString(Macros.THURSDAY) + '\n' +
            agendaDayToString(Macros.FRIDAY) + '\n';
    }

    private String agendaDayToString(String dayOfWeek){
        String agendaDay = dayOfWeek + '\n';

        ArrayList<Timeslot> timeslots = agenda.get(dayOfWeek);

        for(Timeslot timeslot : timeslots){
            agendaDay = agendaDay + "   " + timeslot.toString() + '\n';
        }

        return agendaDay;
    }

    public String getStringId() {
        return "EmployeeAgent" + id;
    }

    @Override
    protected void setup() {

        try {
            this.register();
        } catch (FIPAException e) {
            System.out.println("Failed to register agent in DF Service. Agent ID: " + this.id);
            e.printStackTrace();
            return;
        }

        //TODO: parsing
        //TODO: get ordered ArrayList of TSPairs to suggest (timeslotPreference)
        //TODO: addBehaviour(new EmployeeBehaviour(this, MessageTemplate.MatchAll(), timeslotPreference));
    }

    private void register() throws FIPAException {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(SERVICE_TYPE);
        serviceDescription.setName(getLocalName());
        dfAgentDescription.addServices(serviceDescription);

        DFService.register(this, dfAgentDescription);
    }

    public void removeAvailability(TSPair ts, int duration){
        //TODO: remove <duration> timeslots from agenda, starting on <ts.day>,<ts.timeslot>
    }
}
