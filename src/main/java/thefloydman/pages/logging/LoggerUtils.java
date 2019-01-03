package thefloydman.pages.logging;

import org.apache.logging.log4j.*;

public final class LoggerUtils
{
    private static Logger log;
    
    private static void configureLogging() {
        LoggerUtils.log = LogManager.getLogger("Pages");
    }
    
    public static void log(final Level level, String message, final Object... params) {
        if (LoggerUtils.log == null) {
            configureLogging();
        }
        if (message == null) {
            LoggerUtils.log.log(level, "Attempted to log null message.");
        }
        else {
            try {
                message = String.format(message, params);
            }
            catch (Exception ex) {}
            LoggerUtils.log.log(level, message);
        }
    }
    
    public static void info(final String message, final Object... params) {
        log(Level.INFO, message, params);
    }
    
    public static void warn(final String message, final Object... params) {
        log(Level.WARN, message, params);
    }
    
    public static void error(final String message, final Object... params) {
        log(Level.ERROR, message, params);
    }
    
    public static void debug(final String message, final Object... params) {
    }
    
    static {
        LoggerUtils.log = null;
    }
}
