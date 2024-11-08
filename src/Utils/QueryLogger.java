package Utils;

import Constants.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class QueryLogger {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    private static void writeLog(String filePath, String logEntry) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(logEntry + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logGeneral(String generalType, String details) {
        String log = "{"
                + "\"timestamp\":\"" + getCurrentTimestamp() + "\","
                + "\"logType\":\"" + generalType + "\","
                + "\"details\":\"" + details + "\""
                + "}";
        writeLog(Constants.GENERALLOGFILE, log);
    }

    public static void logEvent(String eventType, String description) {
        String log = "{"
                + "\"timestamp\":\"" + getCurrentTimestamp() + "\","
                + "\"logType\":\"EVENT\","
                + "\"eventType\":\"" + eventType + "\","
                + "\"description\":\"" + description + "\""
                + "}";
        writeLog(Constants.EVENTLOGFILE, log);
    }

    public static void logQuery(String userId, String queryType, String query) {
        String log = "{"
                + "\"userId\":\"" + userId + "\","
                + "\"timestamp\":\"" + getCurrentTimestamp() + "\","
                + "\"queryType\":\"" + queryType + "\","
                + "\"query\":\"" + query + "\""
                + "}";
        writeLog(Constants.QUERYLOGFILE, log);
    }
}