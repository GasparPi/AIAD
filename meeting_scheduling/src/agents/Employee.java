package agents;

import data.Macros;
import data.TSPair;
import data.Timeslot;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.HashMap;

public class Employee extends Agent {
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

    @Override
    protected void setup() {
        //TODO: parsing
        //TODO: get ordered ArrayList of TSPairs to suggest (timeslotPreference)
        //TODO: addBehaviour(new EmployeeBehaviour(this, MessageTemplate.MatchAll(), timeslotPreference));
    }

    public void removeAvailability(TSPair ts, int duration){
        //TODO: remove <duration> timeslots from agenda, starting on <ts.day>,<ts.timeslot>
    }
}
