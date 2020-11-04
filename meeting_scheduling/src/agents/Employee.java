package agents;

import behaviours.EmployeeBehaviour;
import behaviours.EmployeeSendIDBehaviour;
import data.Macros;
import data.TSPair;
import data.Timeslot;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.MessageTemplate;

import java.util.*;

public class Employee extends Agent {
    private final static String SERVICE_TYPE = "meeting-scheduling";

    private final int id;
    private final HashMap<String, ArrayList<Timeslot>> agenda;

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


        //TODO: get ordered ArrayList of TSPairs to suggest (timeslotPreference)

        ArrayList<TSPair> timeslotPreference = new ArrayList<>();

        addBehaviour(new EmployeeSendIDBehaviour(this, MessageTemplate.MatchAll()));
        //addBehaviour(new EmployeeBehaviour(this, MessageTemplate.MatchAll(), timeslotPreference));
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

    public ArrayList<TSPair> sortAgendaByPreference(){
        ArrayList<TSPair> timeslots = new ArrayList<>();

        getDayOfWeekTimeslots("monday", timeslots);
        getDayOfWeekTimeslots("tuesday", timeslots);
        getDayOfWeekTimeslots("wednesday", timeslots);
        getDayOfWeekTimeslots("thursday", timeslots);
        getDayOfWeekTimeslots("friday", timeslots);

        Collections.sort(timeslots);

        System.out.println("EMPLOYEE " + id + ": AGENDA BY PREFERENCE: \n" + timeslots.toString());

        return timeslots;
    }

    private void getDayOfWeekTimeslots(String dayOfWeek, ArrayList<TSPair> timeslots){
        ArrayList<Timeslot> dayTimeslots = this.agenda.get(dayOfWeek);

        if(dayTimeslots != null) {
            Collections.sort(dayTimeslots);

            for (int i = 0; i < dayTimeslots.size(); i++) {
                int duration = getTimeslotDuration(dayOfWeek, dayTimeslots, i, timeslots);
                TSPair ts = new TSPair(dayOfWeek, dayTimeslots.get(i).getSlotIdentifier(), dayTimeslots.get(i).getPriority());
                ts.setAvailableDuration(duration);
                timeslots.add(ts);
            }
        }
    }

    private int getTimeslotDuration(String dayOfWeek, ArrayList<Timeslot> dayTimeslots, int index, ArrayList<TSPair> timeslots){
        if(timeslots.size() > 0){
            TSPair last_ts = timeslots.get(timeslots.size()-1);
            if(dayOfWeek.equals(last_ts.getDay())
                    && dayTimeslots.get(index).getSlotIdentifier() - 1 == last_ts.getTimeslot()){
                return last_ts.getAvailableDuration() - 1;
            }
        }

        int duration = 1;
        int current_timeslot = dayTimeslots.get(index).getSlotIdentifier();

        for(int i = index + 1; i < dayTimeslots.size(); i++){
            if(current_timeslot + 1 == dayTimeslots.get(i).getSlotIdentifier()){
                current_timeslot++;
                duration++;
            }
            else break;
        }

        return duration;
    }
}
