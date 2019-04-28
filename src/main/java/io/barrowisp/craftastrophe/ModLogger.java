package io.barrowisp.craftastrophe;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.jetbrains.annotations.Contract;

/**
 *  Wrapper class for Apache Log4j logging system.
 *  Use this for all mod logging purporses.
 */
public class ModLogger {

    private static ModLogger instance;
    private Logger LOGGER;

    private ModLogger(Logger logger) {
        LOGGER = logger;
        /*
         * This will enable Log4j debug logs to be printed to console
         * in addition to the debug logfile.
         */
        if (Boolean.getBoolean("debug.mode")) {
            LoggerContext context = ((org.apache.logging.log4j.core.Logger) LOGGER).getContext();
            LoggerConfig logConfig = context.getConfiguration().getRootLogger();
            Appender consoleAppender = logConfig.getAppenders().get("Console");
            logConfig.removeAppender("Console");
            logConfig.addAppender(consoleAppender, Level.DEBUG, null);
        }
    }
    protected static void init(Logger logger) {
        if (instance == null)
            instance = new ModLogger(logger);
        else
            instance.LOGGER.error("Trying to initialize ModLogger more then once");
    }
    @Contract(pure = true)
    public static Logger get() {
        return instance.LOGGER;
    }
    /*
     * Short-hand methods to print longs to console.
     */
    public static void info(String log) {
        instance.LOGGER.info(log);
    }
    public static void error(String log) {
        instance.LOGGER.error(log);
    }
    public static void error(String log, Throwable e) {
        instance.LOGGER.error(log, e);
    }
}
