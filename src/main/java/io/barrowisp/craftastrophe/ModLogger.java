package io.barrowisp.craftastrophe;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper class for Apache Log4j logging system.
 * Use this for all mod logging purposes.<br>
 *
 * @see io.barrowisp.craftastrophe.ModLogger.DebugMode
 */
@SuppressWarnings("unused")
public abstract class ModLogger {
    /*
     * This is the same way FML gets our logger instance just before it
     * is passed through the initialization event argument.
     */
    private static final @NotNull Logger logger = LogManager.getLogger(Craftastrophe.MODID);

    /**
     * A data type representing which debug mode our session is using.
     * <ul>
     *     <li><b>STANDARD</b>: Print <i>only</i> mod debug logs to console</li>
     *     <li><b>VERBOSE</b>: Print both mod and Forge logs to console</li>
     *     <li><b>UNKNOWN</b>: Debug mode was not recognized</li>
     * </ul>
     */
    public enum DebugMode {

        STANDARD("standard"),
        VERBOSE("verbose"),
        UNKNOWN("");

        private static DebugMode mode;
        private final String name;

        DebugMode(String mode) {
            this.name = mode;
        }
        static boolean init() {
            return (mode = findFromSysProperties()) != null;
        }
        @Nullable
        static DebugMode findFromSysProperties() {

            String sMode = System.getProperty("debug.mode");
            if (sMode == null) return null;

            for (DebugMode eMode : DebugMode.values()) {
                if (eMode.name.equalsIgnoreCase(sMode))
                    return eMode;
            }
            return UNKNOWN;
        }
        static boolean is(DebugMode mode) {
            return DebugMode.mode == mode;
        }
    }
    static void init() {

        if (DebugMode.init()) {
            /*
             * This will enable Forge and mod Log4j debug logs to be printed to
             * console in addition to the debug logfile.
             */
            if (DebugMode.is(DebugMode.VERBOSE))
            {
                LoggerContext context = ((org.apache.logging.log4j.core.Logger) logger).getContext();
                LoggerConfig logConfig = context.getConfiguration().getRootLogger();
                Appender consoleAppender = logConfig.getAppenders().get("Console");
                logConfig.removeAppender("Console");
                logConfig.addAppender(consoleAppender, Level.DEBUG, null);
            }
            else if (DebugMode.is(DebugMode.UNKNOWN))
                logger.warn("unknown debug mode passed as VM argument");
        }
    }
    @Contract(pure = true)
    public static Logger get() {
        return logger;
    }
    /*
     * Short-hand methods to print longs to console.
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
    public static void error(String log, Throwable e) {
        logger.error(log, e);
    }
    public static void warn(String log) {
        logger.warn(log);
    }
    /** Print debug log to console and mod logfile */
    public static void debug(String log) {

        logger.debug(log);
        if (DebugMode.is(DebugMode.STANDARD))
            logger.info("DEBUG: " + log);

    }
    /** Print debug log to console and mod logfile */
    public static void debug(String format, Object...args) {

        logger.printf(Level.DEBUG, format, args);
        if (DebugMode.is(DebugMode.STANDARD))
            logger.printf(Level.INFO, "DEBUG: " + format, args);

    }
}
