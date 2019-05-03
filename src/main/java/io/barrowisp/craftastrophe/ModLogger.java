package io.barrowisp.craftastrophe;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper class for Apache Log4j logging system.
 * Use this for all mod logging purposes.<br>
 *
 * <i>Note that this class cannot be instantialized from outside,
 * use {@link #init(Logger)} instead.</i>
 *
 * @see io.barrowisp.craftastrophe.ModLogger.DebugMode
 */
@Nullable
public class ModLogger {

    private static ModLogger instance;
    private Logger logger;

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
        static DebugMode init() {
            return mode = findFromSysProperties("debug.mode");
        }
        @Nullable
        static DebugMode findFromSysProperties(String property) {

            String sMode = System.getProperty(property);
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
    /* This is a private constructor, use init method to call from public */
    private ModLogger(Logger loggerInst) {

        logger = loggerInst;
        DebugMode debugMode = DebugMode.init();

        if (debugMode != null) {
            /*
             * This will enable Forge and mod Log4j debug logs to be printed to
             * console in addition to the debug logfile.
             */
            if (debugMode.is(DebugMode.VERBOSE))
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
    protected static void init(Logger logger) {

        if (instance == null)
            instance = new ModLogger(logger);
        else
            instance.logger.warn("Trying to initialize ModLogger more than once");
    }
    @Contract(pure = true)
    public static Logger get() {
        return instance.logger;
    }
    /*
     * Short-hand methods to print longs to console.
     */
    public static void info(String log) {
        instance.logger.info(log);
    }
    public static void error(String log) {
        instance.logger.error(log);
    }
    public static void error(String log, Object...args) {
        instance.logger.error(log, args);
    }
    public static void error(String log, Throwable e) {
        instance.logger.error(log, e);
    }
    public static void warn(String log) {
        instance.logger.warn(log);
    }
    /** Print debug log to console and mod logfile */
    public static void debug(String log) {
        if (DebugMode.is(DebugMode.STANDARD))
            instance.logger.info("DEBUG: " + log);
        instance.logger.debug(log);
    }
    /** Print debug log to console and mod logfile */
    public static void debug(String format, Object...args) {
        if (DebugMode.is(DebugMode.STANDARD))
            instance.logger.printf(Level.INFO, "DEBUG: " + format, args);
        instance.logger.printf(Level.DEBUG, format, args);
    }
}
