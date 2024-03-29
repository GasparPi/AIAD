import logger.MyLogger;
import model.SchedulingModel;
import uchicago.src.sim.engine.SimInit;

public class Main {
    public static void main(String[] args) {
        SchedulingModel schedulingModel = new SchedulingModel();

        if (args.length == 4) {
            schedulingModel.setNumberOfEmployees(Integer.parseInt(args[0]));
            schedulingModel.setNumberOfGroups(Integer.parseInt(args[1]));
            schedulingModel.setNumberOfEmployees(Integer.parseInt(args[2]));
            schedulingModel.setOccupancyRate(Integer.parseInt(args[3]));
        }

        MyLogger.deletePreviousLogs();

        SimInit init = new SimInit();
        init.loadModel(schedulingModel, null, false);
    }
}
