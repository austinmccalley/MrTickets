package me.Austin.MT;

import me.Austin.MT.Managers.filters.ConsoleFilter;
import me.Austin.MT.Managers.filters.Log4JFilter;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

/**
 * Created by mcaus_000 on 3/4/2017.
 */
public class ConsoleLogger {
    private static Logger logger;

    /**
     * Set the logger to use.
     *
     * @param logger
     *         The logger
     */
    public static void setLogger(Logger logger) {
        ConsoleLogger.logger = logger;
    }

    /**
     * Sets up the console filter if enabled.
     *
     * @param logger
     *         the plugin logger
     */
    public static void setupConsoleFilter(Logger logger) {

        // Try to set the log4j filter
        try {
            Class.forName("org.apache.logging.log4j.core.filter.AbstractFilter");
            setLog4JFilter();
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            // log4j is not available
            System.out.println("[MrT] You're using Minecraft 1.6.x or older, Log4J support will be disabled");
            ConsoleFilter filter = new ConsoleFilter();
            logger.setFilter(filter);
            Bukkit.getLogger().setFilter(filter);
            Logger.getLogger("Minecraft").setFilter(filter);
        }
    }

    // Set the console filter to remove the passwords
    private static void setLog4JFilter() {
        org.apache.logging.log4j.core.Logger logger;
        logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        logger.addFilter(new Log4JFilter());
    }

}
