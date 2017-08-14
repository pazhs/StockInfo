package controller;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;

import constants.StockInfoNumConstant;
import constants.StockInfoStrLtrConstant;
import constants.StockInfoStrProcConstant;
import logger.LoggerUtil;
import logger.LoggerWritingUtil;
import net.lingala.zip4j.exception.ZipException;
import service.Service;
import util.StockInfoProcessTimeUtil;

/**
 * <p>
 * Project Flow
 * </p>
 * <p>
 * -------------
 * </p>
 * <p>
 * Get StockInfoMap from the database table "tab_stock_info"
 * </p>
 * <p>
 * Get the mcLink of companies from mc (companies that start with A, B, etc)
 * </p>
 * <p>
 * if the mcLink that has been obtained from mc is already in the local database, then skip adding that company
 * </p>
 * <p>
 * if the mcLink is not in the local database then, get the market capitalization 
 * of that company from mc and if it is greater than ZERO crores,
 * then fetch all the relevant data for that company
 * </p>
 * <p>
 * My definition of Market Capitalization(in crores)
 * </p>
 * <p>
 *    - Micro Cap : between 0 and 50 crores
 *    </p>
 * <p>
 *    - Mini Cap : between (>50 crores) and 200 crores
 *    </p>
 * <p>
 *    - small cap : between (>200 crores) and 2000 crores
 *    </p>
 * <p>
 *    - Mid cap : between (>2000 crores) and 20000 crores
 *    </p>
 * <p>
 *    - Large cap : > 20000
 * </p>
 * <p>
 * put the new data into a Map and add it to the database.
 *</p>
 */
public class StockInfoController {
	
	private Service m_serviceObj = null;
	
	public StockInfoController() throws ClassNotFoundException, SQLException {
		m_serviceObj = new Service();
	}

	/**
	 * @param args the arguments
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		String strClassName = StockInfoController.class.getSimpleName();
        String strMethodName = Thread.currentThread().getStackTrace()[StockInfoNumConstant.INT_ONE]
                .getMethodName();
        
        Runtime rt = null;
        DateTime startTime = null;
        DateTime endTime = null;
        int exitStatus = StockInfoNumConstant.INT_ZERO;
		
		StockInfoController obj = new StockInfoController();
		
		try{
			LoggerUtil.info(String.format(StockInfoStrProcConstant.AC_PROC_SE, StockInfoStrLtrConstant.AC_START));
            LoggerWritingUtil.writeLoggerInfo(true, strMethodName, strClassName);

            startTime = new DateTime();

            rt = Runtime.getRuntime();
            
            obj.execute();
            
            endTime = new DateTime();
            
            LoggerWritingUtil.writeLoggerInfo(false, strMethodName, strClassName);
            StockInfoProcessTimeUtil.end(startTime, endTime, false, StockInfoStrLtrConstant.STR_EMPTY);
            LoggerUtil.info(String.format(StockInfoStrProcConstant.AC_PROC_SE, StockInfoStrLtrConstant.AC_END));
		}catch(Exception ex) {
            exitStatus = StockInfoNumConstant.INT_ONE;
            LoggerUtil.error(ExceptionUtils.getStackTrace(ex));
        }finally {
            rt.exit(exitStatus);
        }
	}
	
	/**
	 * Execute.
	 *
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws ZipException 
	 */
	public boolean execute() throws SQLException, IOException, ClassNotFoundException, ZipException {
		
		try{
			m_serviceObj.execute();
		}catch(Exception ex){
			System.out.println();
		}finally{
			m_serviceObj.closeDBConnection();
		}
		return false;
	}
}
