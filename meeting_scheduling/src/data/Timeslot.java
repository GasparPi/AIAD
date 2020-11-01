package data;

public class Timeslot implements Comparable<Timeslot>{
    private final Integer slot_identifier;
    private final Integer priority;

    public Timeslot(int slot_identifier, int priority) {
        this.slot_identifier = slot_identifier;
        this.priority = priority;
    }

    public Integer getSlotIdentifier() {
        return slot_identifier;
    }

    public Integer getPriority() {
        return priority;
    }

    @Override
    public String toString(){
        return "slot: " + slot_identifier + " ; priority: " + priority;
    }

    @Override
    public int compareTo(Timeslot timeslot) {
        return timeslot.getSlotIdentifier() - this.getSlotIdentifier();
    }
}