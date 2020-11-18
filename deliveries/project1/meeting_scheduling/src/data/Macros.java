package data;

public class Macros {
    public final static String MONDAY = "monday";
    public final static String TUESDAY = "tuesday";
    public final static String WEDNESDAY = "wednesday";
    public final static String THURSDAY = "thursday";
    public final static String FRIDAY = "friday";

    public static int dayOfWeekToNumber(String day){
        return switch (day) {
            case MONDAY -> 1;
            case TUESDAY -> 2;
            case WEDNESDAY -> 3;
            case THURSDAY -> 4;
            case FRIDAY -> 5;
            default -> 0;
        };
    }
}
