package data;

public class Timeslot {
    private final int slot_identifier;
    private final int priority;

    public Timeslot(int slot_identifier, int priority) {
        this.slot_identifier = slot_identifier;
        this.priority = priority;
    }

    @Override
    public String toString(){
        return "slot: " + slot_identifier + " ; priority: " + priority;
    }
}
