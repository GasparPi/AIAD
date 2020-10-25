package agents;

import data.Macros;
import data.Timeslot;
import jade.core.Agent;

import java.time.DayOfWeek;
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
}
