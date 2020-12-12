package logger;

import data.MessageContent;
import jade.core.AID;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
    private static final String LOGS_DIR = "logs/";
    private final Logger logger;

    public MyLogger(String agentId) {
        this.logger = Logger.getLogger(agentId);
        this.logger.setUseParentHandlers(false);

        try {
            File logDir = new File(LOGS_DIR);

            if (!logDir.exists())
                logDir.mkdir();

            FileHandler fh = new FileHandler(LOGS_DIR + System.currentTimeMillis() + "_" + agentId + ".log");
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            this.logger.addHandler(fh);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void logInfo(String info) {
        this.logger.info("\t" + info);
    }

    public void logMessageContent(String header, MessageContent content, String direction, AID correspondent) {
        this.logger.info("\t" + header + ": " + content + "\n\t\t" + direction + ": " + correspondent.getLocalName());
    }

    public void logMessageContent(String header, MessageContent content, String direction, ArrayList<AID> correspondents) {
        StringBuilder stringBuilder = new StringBuilder("\t" + header + ": " + content + "\n\t\t" + direction + ": ");

        for (AID aid : correspondents)
            stringBuilder.append(aid.getLocalName()).append("; ");

        this.logger.info(stringBuilder.toString());
    }

    public void logMessageContent(String header, String content, String direction, AID correspondent) {
        this.logger.info("\t" + header + ": " + content + "\n\t\t" + direction + ": " + correspondent.getLocalName());
    }

    public static void deletePreviousLogs() {
        File logDir = new File(LOGS_DIR);
        if (logDir.exists())
            deleteDirRecursively(logDir);
    }

    private static void deleteDirRecursively(File dir) {
        File[] allContents = dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirRecursively(file);
            }

        }
    }
}
