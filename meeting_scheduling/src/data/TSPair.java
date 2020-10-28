package data;

public class TSPair {
    private String day;
    private int timeslot;

    public TSPair(String day, int timeslot){
        this.day = day;
        this.timeslot = timeslot;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }

    public String getDay() {
        return day;
    }

    public int getTimeslot() {
        return timeslot;
    }
}

