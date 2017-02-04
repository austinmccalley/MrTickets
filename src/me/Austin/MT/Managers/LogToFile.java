package me.Austin.MT.Managers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is intended to be use with the default logging
 * class of java It save the log in an XML file and display a friendly message
 * to the user
 *
 * @author Ibrabel
 * @since 1.0
 */
public class LogToFile {

    private static final Logger logger = Logger.getLogger("MYLOG");

    /**
     * LogToFile Class - enable to log all exceptions to a file and display user
     * message on demand
     *
     * @param level Level the log is at, Severe, Warning/Warn, INFO, Config, Fine,
     *              Finer, Finest. Defaults if CONFIG
     * @param msg   Log message
     */
    public static void log(String level, String msg) {

        FileHandler fh = null;
        try {

            fh = new FileHandler("log.xml", true);
            logger.addHandler(fh);
            switch (level) {
                case "severe":
                    logger.log(Level.SEVERE, msg);
                    break;
                case "warning":
                    logger.log(Level.WARNING, msg);
                    break;
                case "info":
                    logger.log(Level.INFO, msg);

                    break;
                case "config":
                    logger.log(Level.CONFIG, msg);
                    break;
                case "fine":
                    logger.log(Level.FINE, msg);
                    break;
                case "finer":
                    logger.log(Level.FINER, msg);
                    break;
                case "finest":
                    logger.log(Level.FINEST, msg);
                    break;
                default:
                    logger.log(Level.CONFIG, msg);
                    break;
            }
        } catch (IOException | SecurityException ex1) {
            //logger.log(Level.SEVERE, null, ex1);
        } finally {
            if (fh != null)
                fh.close();
        }
    }

}