package logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {
	
	private static final Logger errorLogger = LogManager.getLogger("errorLogger");
	private static final Logger infoLogger = LogManager.getLogger("infoLogger");
	
	public static void info(final String logMsg) {
		infoLogger.info(logMsg);
	}
	
	public static void debug(final String logMsg) {
		infoLogger.debug(logMsg);
	}
	
	public static void error(final String logMsg) {
		errorLogger.error(logMsg);
	}

}
