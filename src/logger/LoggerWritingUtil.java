package logger;

import constants.StockInfoStrLtrConstant;

public class LoggerWritingUtil {
	
	/**
	 * Write logger info.
	 *
	 * @param bStart the b start
	 * @param strMethodName the str method name
	 * @param strClassName the str class name
	 */
	public static void writeLoggerInfo(final boolean bStart, final String strMethodName, final String strClassName) {
		
		StringBuilder sb = new StringBuilder();
		
		if( bStart ) {
			sb.append(String.format("%s of method \"%s\" of class \"%s\"", StockInfoStrLtrConstant.AC_START, strMethodName, strClassName));
		} else {
			sb.append(String.format("%s of method \"%s\" of class \"%s\"", StockInfoStrLtrConstant.AC_END, strMethodName, strClassName));
		}
		LoggerUtil.info(sb.toString());
	}

}
