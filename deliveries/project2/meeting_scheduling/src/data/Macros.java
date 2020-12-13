package data;

public class Macros {
    public final static String MONDAY = "monday";
    public final static String TUESDAY = "tuesday";
    public final static String WEDNESDAY = "wednesday";
    public final static String THURSDAY = "thursday";
    public final static String FRIDAY = "friday";

    public static int dayOfWeekToNumber(String day){
        if(day.equals(MONDAY))
            return 1;
        if(day.equals(TUESDAY))
            return 2;
        if(day.equals(WEDNESDAY))
            return 3;
        if(day.equals(THURSDAY))
            return 4;
        if(day.equals(FRIDAY))
            return 5;
        return 0;
    }
}
