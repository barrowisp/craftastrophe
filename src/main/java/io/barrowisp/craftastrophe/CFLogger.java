package io.barrowisp.craftastrophe;

import io.yooksi.forgelib.ModLogger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

/**
 * <p>This class is a singleton container for our logger class. It contains a single
 * static instance of a `CommonLogger` implementation as well as static helper methods
 * to make log calls easier, so you can freely use this as your main logger class.</p>
 * <p>Just remember to initialize it with {@link #init()} method before invoking calls.</p>
 *
 * @see ModLogger
 */
public final class CFLogger {

    private static ModLogger logger;

    /* Make the constructor private to disable instantiation */
    private CFLogger() {
        throw new UnsupportedOperationException();
    }

    static void init() {

        if (logger == null)
            logger = ModLogger.create(Craftastrophe.MODID);
        else
            logger.warn("Trying to initialize mod logger more then once");
    }

    @Contract(pure = true)
    public static Logger get() {
        return logger.get();
    }
    /*
     * Short-hand methods to print logs to console.
     */
    public static void info(String log) {
        logger.info(log);
    }
    public static void error(String log) {
        logger.error(log);
    }
    public static void error(String log, Object...args) {
        logger.printf(Level.ERROR, log, args);
    }
    public static void error(String log, Throwable t) {
        logger.error(log, t);
    }
    public static void warn(String log) {
        logger.warn(log);
    }
    public static void debug(String log) {
        logger.debug(log);
    }
    public static void debug(String format, Object...args) {
        logger.debug(format, args);
    }
    public static void debug(String log, Throwable t) {
        logger.debug(log, t);
    }
}
