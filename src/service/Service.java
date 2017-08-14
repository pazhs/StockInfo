package service;

import java.sql.SQLException;

/**
 * The Class StockInfoService.
 */
public class Service {
	
	private SIService m_siService = null;
	private BCBSENSEService m_bsenseBCService = null;
	
	public Service() throws ClassNotFoundException, SQLException {
		m_siService = new SIService();
		m_bsenseBCService = new BCBSENSEService();
	}
	
	public boolean closeDBConnection(){
		m_siService.closeDBConnection();
		return true;
	}

	/**
	 * <p>
	 * execute metod - Get StockInfoMap from the database table "tab_stock_info"
	 * 
	 * - Get the mcLink of companies from mc (companies that start with A, B,
	 * etc) and form a List
	 * 
	 * - if the mcLink that has been obtained from mc is already in the local
	 * database, then skip adding that company and delete that company from the
	 * newly formed List
	 * 
	 * - if the mcLink is not in the local database then, get the market
	 * capitalization of that company from mc and if it is greater than ZERO
	 * crores, then fetch all the relevant data for that company
	 * 
	 * - put the new data into a Map and add it to the database.
	 * </p>
	 * .
	 *
	 * @return true, if successful
	 * @throws Exception 
	 */
	public boolean execute() throws Exception {

		try {
			// get the mcLink set from MC
			// m_siService.getMCLinkSet();
			
			// add the obtained mcLink SET to the database
			// m_siService.addMCLinkSet();
			
			m_siService.getDBStockInfoMap();
			
			// get the bhavcopies from bse and nse
			// MAP m_siBCMap has been created
			// m_bsenseBCService.getBSEBhavCopyData();
			
			// m_bsenseBCService.getNSEBhavCopyData();
			
			// m_siService.compareBhavCopyDB();
			
			// add or update the StockInfo Data to the database
			// m_siService.addUpdateDatabaseWithSIData();
			
			m_siService.updateDBWithSIData();
			
		} finally {

		}

		return true;
	}
	
/*	private boolean addDBMCLinkList(List<String> missingMCLinkList) throws SQLException{
		try{
			daoObj.addDBMCLinkList(missingMCLinkList);
		}finally{
			
		}
		return true;
	}*/
	
/*	private boolean addStockInfoData() throws SQLException{
		try{
			daoObj.addStockInfoData(m_missingStockInfoMap);
			
		}finally{
			
		}
		return true;
	}*/
	
	

	/**
	 * <p>
	 * Gets data from tab_stock_info_info of the database.
	 * </p>
	 *
	 * @return the stock info map
	 * @throws SQLException
	 *//*
	private boolean getDBStockInfoMap() throws SQLException {

		try {
			// get the stockInfo from "tab_stock_info" table of the database.
			m_dbStockInfoMap = daoObj.getDBStockInfoMap();

		} finally {

		}
		return true;
	}
	
	private boolean getMissingStockInfoMap() throws SQLException, IOException {
		
		try{
			
			// get the mcLink list again
			getDBMCLinkList();
			
			// get data from the table "tab_stock_info"
			getDBStockInfoMap();
			
			m_missingStockInfoMap = new HashMap<String, StockInfoBean>();
			
			// after getting the StockInfo from the database,
			// insert the stockInfo for the new stocks that are yet to be 
			// present in the database
			for(String mcLink : m_dbMcLinkList){
								
				// if the stock's Info is not present in the database, 
				// then fetch it from mc
				if( !m_dbStockInfoMap.containsKey(mcLink) ){
					
					StockInfoBean siBean = fetchStockInfo(mcLink);
					
					if(siBean != null && !siBean.getIsin().trim().isEmpty()){
						m_missingStockInfoMap.put(siBean.getIsin(), siBean);
					}
				}
			}
			
			if(m_missingStockInfoMap.size() >0 ) {
				// insert the data into the database
				addStockInfoData();
			}
			
		}finally{
			
		}
		return true;
	}*/
}
