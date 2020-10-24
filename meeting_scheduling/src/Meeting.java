import java.util.ArrayList;

public class Meeting {

    private final int id;
    private final int duration;

    private int day;
    private int startTime;
    private int endTime;

    private ArrayList<Integer> obligatoryEmployees;

    public Meeting(int id, int duration) {
        this.id = id;
        this.duration = duration;
    }


}
