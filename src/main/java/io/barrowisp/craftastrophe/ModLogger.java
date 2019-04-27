package io.barrowisp.craftastrophe;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

public class ModLogger {

    private static ModLogger instance;
    private Logger LOGGER;
    private boolean debug;

    private ModLogger(Logger logger) {
        LOGGER = logger;
        debug = Boolean.getBoolean("debug.mode");
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
    /** Short-hand method to print information to console. */
    public static void info(Object log) {
        instance.LOGGER.info(log);
    }
    /** Print debug log to both console and logfile. */
    public static void debug(String log) {
        instance.LOGGER.debug(log);
        if (instance.debug == true)
            instance.LOGGER.info("DEBUG: " + log);
    }
}
