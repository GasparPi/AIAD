
import logger.MyLogger;
import model.SchedulingModel;
import uchicago.src.sim.engine.SimInit;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: <num_employees> <num_groups> <num_meetings>");
            return;
        }

        int numberOfEmployees = Integer.parseInt(args[0]);
        int numberOfGroups = Integer.parseInt(args[1]);
        int numberOfMeetings = Integer.parseInt(args[2]);

        MyLogger.deletePreviousLogs();

        SimInit init = new SimInit();
        init.loadModel(new SchedulingModel(numberOfEmployees, numberOfGroups, numberOfMeetings), null, false);
    }
}
