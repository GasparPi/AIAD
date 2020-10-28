package data;

public class TSPair {
    private String day;
    private int timeslot;
    private int availableDuration;

    public TSPair(String day, int timeslot){
        this.day = day;
        this.timeslot = timeslot;
        this.availableDuration = 0;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
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
}

