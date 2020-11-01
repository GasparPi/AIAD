package data;

public class TSPair implements Comparable<TSPair> {
    private final String day;
    private final int timeslot;
    private final int preference;
    private int availableDuration;

    public TSPair(String day, int timeslot, int preference){
        this.day = day;
        this.timeslot = timeslot;
        this.preference = preference;
        this.availableDuration = 0;
    }

    public TSPair(String day, int timeslot) {
        this.day = day;
        this.timeslot = timeslot;
        this.preference = 0;
        this.availableDuration = 0;
    }

    public void setAvailableDuration(int availableDuration) {
        this.availableDuration = availableDuration;
    }

    public String getDay() {
        return day;
    }

    public int getTimeslot() {
        return timeslot;
    }

    public int getAvailableDuration() {
        return availableDuration;
    }

    public int getPreference() {
        return preference;
    }

    @Override
    public String toString() {
        return "TSPair{" +
                "day='" + day + '\'' +
                ", timeslot=" + timeslot +
                ", preference=" + preference +
                ", availableDuration=" + availableDuration +
                '}';
    }

    @Override
    public int compareTo(TSPair tsPair) {
        if(this.preference != tsPair.getPreference()){
            return this.preference - tsPair.getPreference();
        }
        else if(this.availableDuration != tsPair.getAvailableDuration()){
            return this.availableDuration - tsPair.getAvailableDuration();
        }
        else if(this.timeslot != tsPair.getTimeslot()){
            return this.timeslot - tsPair.getTimeslot();
        }
        else if(!this.day.equals(tsPair.getDay())){
            return Macros.dayOfWeekToNumber(this.day) - Macros.dayOfWeekToNumber(tsPair.getDay());
        }
        else return 0;
    }
}

