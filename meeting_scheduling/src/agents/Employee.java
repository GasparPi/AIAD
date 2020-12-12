package agents;

import behaviours.employee.EmployeeBehaviour;
import data.Macros;
import data.TSPair;
import data.Timeslot;
import logger.MyLogger;

import sajas.core.Agent;
import sajas.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.sql.Time;
import java.util.*;

public class Employee extends Agent {
    private final static String SERVICE_TYPE = "meeting-scheduling";

    private final int id;
    private final HashMap<String, ArrayList<Timeslot>> agenda;
    private final MyLogger logger;
    private int meetings;

    public Employee(int id, HashMap<String, ArrayList<Timeslot>> agenda) {
        this.id = id;
        this.agenda = agenda;
        this.logger = new MyLogger( "Employee" + id);
        this.meetings = 0;
    }

    public int getId() {
        return id;
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
        StringBuilder agendaDay = new StringBuilder(dayOfWeek + '\n');

        ArrayList<Timeslot> timeslots = agenda.get(dayOfWeek);

        for(Timeslot timeslot : timeslots){
            agendaDay.append("   ").append(timeslot.toString()).append('\n');
        }

        return agendaDay.toString();
    }

    public String getStringId() {
        return "EmployeeAgent" + id;
    }

    @Override
    protected void setup() {

        try {
            this.register();
        } catch (FIPAException e) {
            System.err.println("Failed to register agent in DF Service. Agent ID: " + this.id);
            e.printStackTrace();
            return;
        }

        addBehaviour(new EmployeeBehaviour(this));
        System.out.println("Employee " + id + " is set up.");
    }

    private void register() throws FIPAException {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(SERVICE_TYPE);
        serviceDescription.setName(getLocalName());
        dfAgentDescription.addServices(serviceDescription);

        DFService.register(this, dfAgentDescription);

        //Logger
        this.logger.logInfo("REGISTERED IN DF SERVICE");
    }

    public void removeAvailability(TSPair ts, int duration) {
        ArrayList<Timeslot> dayTimeSlots = this.agenda.get(ts.getDay());

        // Remove duration slots
        for (int i = 0; i < dayTimeSlots.size(); i++) {
            if (dayTimeSlots.get(i).getSlotIdentifier() == ts.getTimeslot()) {
                for (int j = 0; j < duration; j++) dayTimeSlots.remove(i);
            }
        }

        this.agenda.put(ts.getDay(), dayTimeSlots);
    }

    public ArrayList<TSPair> sortAgendaByPreference() {
        ArrayList<TSPair> timeslots = new ArrayList<>();

        getDayOfWeekTimeslots("monday", timeslots);
        getDayOfWeekTimeslots("tuesday", timeslots);
        getDayOfWeekTimeslots("wednesday", timeslots);
        getDayOfWeekTimeslots("thursday", timeslots);
        getDayOfWeekTimeslots("friday", timeslots);

        Collections.sort(timeslots);

        //System.out.println("EMPLOYEE " + id + ": AGENDA BY PREFERENCE: \n" + timeslots.toString());

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

    public MyLogger getLogger() {
        return logger;
    }

    public void addMeetings(int m) { meetings += m; }

    public int getMeetings() {
        return meetings;
    }

    private String parseDay(int day) {
        if (day == 0) return "monday";
        else if (day == 1) return "tuesday";
        else if (day == 2) return "wednesday";
        else if (day == 3) return "thursday";
        else if (day == 4) return "friday";
        else return "";
    }

    public boolean hasTimeSlotScheduled(int day, int timeslot) {
        ArrayList<Timeslot> dayAgenda = this.agenda.get(parseDay(day));
        for (Timeslot ts : dayAgenda) {
            if (ts.getSlotIdentifier() == timeslot + 1) {
                return false;
            }
        }

        return true;
    }
}
