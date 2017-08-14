package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.StockInfoBean;
import constants.SQLDQConst;
import constants.SQLIQConst;
import constants.SQLSelectQueryConstant;
import constants.SQLUQConst;

/**
 * The Class StockInfoDAO.
 */
public class StockInfoDAO {
	
	public static Connection conn = null;
	
	public StockInfoDAO() throws ClassNotFoundException, SQLException {
		conn = getConnection();
	}
	
	public void closeConnection() {
		
		try{
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
			
		}catch(SQLException ex) {
			
		}
	}
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 * @throws SQLException the SQL exception
	 * @throws ClassNotFoundException 
	 */
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		// JDBC driver name and database URL
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		final String DB_URL = "jdbc:mysql://localhost/allcapz";
		
		//  Database credentials
		final String USER = "reader";
		final String PASS = "reader_1";

		try{
			// register JDBC driver
			Class.forName(JDBC_DRIVER);
			
			// open database connection
			return DriverManager.getConnection(DB_URL, USER, PASS);
			
		}catch(SQLException ex) {
			
		}
		return null;
	}
	
	/**
	 * Close connection.
	 *
	 * @param stmt the stmt
	 * @param conn the conn
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public boolean closeConnection(final Statement stmt, final Connection conn) throws SQLException{
		if(stmt != null){
			stmt.close();
		}
		if(conn != null){
			conn.close();
		}
		return true;
	}
	
	public boolean addUpdateDatabaseWithSIData(Map<String, StockInfoBean>siAddMap, 
			Map<String, StockInfoBean>siUpdateMap) throws SQLException{
		
		try{
			addStockInfoData(siAddMap);
			updateStockInfoData(siUpdateMap);
		}finally{
			
		}
		return true;
	}
	
	public boolean addMCLinkSet(Set<String> m_mcLinkSet) throws SQLException{
		
		int counter = 0;
		int [] committedList = null;
		PreparedStatement preStmt = null;
		
		try{
			preStmt = conn.prepareStatement(SQLIQConst.DB_INSERT_MCLINK_TABLE.toString());
			
			conn.setAutoCommit(false);
			for(String mcLink : m_mcLinkSet){
				
				preStmt.setString (1, mcLink);
				preStmt.setDate(2, new Date(new java.util.Date().getTime()));
				
				preStmt.addBatch();
				
				if(counter++ == 100){
					committedList = preStmt.executeBatch();
					counter = 0;
				}
			}
			if(counter < 100){
				committedList = preStmt.executeBatch();
			}
			conn.commit();
			
		}finally{
			preStmt.close();
		}
		return true;
	}
	
	public boolean addStockInfoData(Map<String, StockInfoBean> siMap) throws SQLException{
		
		int counter = 0;
		PreparedStatement preStmt = null;
		
		// isin is the key of the MAP
		StockInfoBean siBean = null;
		
		try{
			preStmt = conn.prepareStatement(SQLIQConst.DB_INSERT_SI_TABLE.toString());
			
			conn.setAutoCommit(false);
			
			for (Map.Entry<String, StockInfoBean> entry : siMap.entrySet()) {
				siBean = entry.getValue();
				
				/*isin, mclink, bse_symbol, nse_symbol, ")
				.append(" company_name, sector_name, face_value, market_cap
*/				
				preStmt.setString(1, siBean.getIsin());
				preStmt.setString(2, siBean.getMcLink());
				preStmt.setString(3, siBean.getBseSymbol());
				preStmt.setString(4, siBean.getNseSymbol());
				preStmt.setString(5, siBean.getCompanyName());
				preStmt.setString(6, siBean.getSectorName());
				preStmt.setDouble(7, siBean.getFaceValue());
				preStmt.setDouble(8, siBean.getMarketCap());
				preStmt.setString(9, siBean.getBseSCGroup());
				preStmt.setString(10, siBean.getNseEquitySeries());
				
				preStmt.addBatch();
				
				if(counter++ == 100){
					preStmt.executeBatch();
					counter = 0;
				}
			}
			if(counter < 100){
				preStmt.executeBatch();
			}
			conn.commit();
		}finally{
			
		}
		return true;
	}
	
	public boolean deleteUpdatedMcLinks(Set<String> dbLinkSet) throws SQLException{
		
		int counter = 0;
		PreparedStatement preStmt = null;
		
		try{
			// DELETE FROM table_name
			//  WHERE condition
			preStmt = conn.prepareStatement(SQLDQConst.MCLINK_DELETE.toString());
			
			conn.setAutoCommit(false);
			
			for (String mcLink : dbLinkSet) {
				preStmt.setString(1, mcLink);

				preStmt.addBatch();
				if (counter++ == 100) {
					preStmt.executeBatch();
					counter = 0;
				}
			}
			if(counter < 100){
				preStmt.executeBatch();
			}
			conn.commit();
		}finally{
			
		}
		return true;
	}
	 
	/**
	 * Fetch MC resource.
	 *
	 * @return the map
	 * @throws SQLException the SQL exception
	 */
	public Map<String, String> fetchMCResource() throws SQLException{
		
		// stock info from mc website
		Map<String, String> mcResourceMap = null;
		ResultSet rs = null;
		Statement stmt = null;
		
		String stockAll = null;
		String stockAllSeq = null;
		
		try{
			// execute a query
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQLSelectQueryConstant.DB_SELECT_RESOURCE_TABLE);
			
			// get data from database
			mcResourceMap = new HashMap<String, String>();
			
			while(rs.next()) {
				stockAll = rs.getString("stock_all");
				stockAllSeq = rs.getString("stock_all_seq");
				
				mcResourceMap.put("stockAll", stockAll);
				mcResourceMap.put("stockAllSeq", stockAllSeq);
			}
		}finally{
			stmt.close();
		}
		return mcResourceMap;
	}
	
	public Set<String> getMCLinkSetFromDB() throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		
		Set<String> mcLinkSet = null;
		
		
		try{
			mcLinkSet = new LinkedHashSet<String>();
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQLSelectQueryConstant.DB_SELECT_MCLINK_TABLE.toString());
			
			while(rs.next()) {
				mcLinkSet.add(rs.getString("mclink"));
			}
		}finally{
			stmt.close();
		}
		return mcLinkSet;
	}
	
	/**
	 * <p>
	 * get the stockInfo from "tab_stock_info" table of the database.
	 * </p>
	 *
	 * @return the stock info map
	 * @throws SQLException 
	 */
	public Map<String, StockInfoBean> getDBStockInfoMap() throws SQLException{
		Map<String, StockInfoBean> stockInfoMap = null;
		
		ResultSet rs = null;
		Statement stmt = null;
		
		// data that has been fetched from the tab_stock_info of the database
		StockInfoBean stockInfoObj = null;
		
		try{
			// execute a query
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQLSelectQueryConstant.DB_SELECT_STOCK_INFO_TABLE.toString());
			
			stockInfoMap = new LinkedHashMap<String, StockInfoBean>();
			
			while(rs.next()) {
				stockInfoObj = new StockInfoBean();
				
				stockInfoObj.setIsin(rs.getString("isin"));
				stockInfoObj.setMcLink(rs.getString("mclink"));
				stockInfoObj.setBseSymbol(rs.getString("bse_symbol"));
				stockInfoObj.setNseSymbol(rs.getString("nse_symbol"));
				stockInfoObj.setCompanyName(rs.getString("company_name"));
				stockInfoObj.setSectorName(rs.getString("sector_name"));
				stockInfoObj.setFaceValue(rs.getDouble("face_value"));
				stockInfoObj.setMarketCap(rs.getDouble("market_cap"));
				stockInfoObj.setIpo(rs.getDate("ipo"));
				stockInfoObj.setFaceValueDate(rs.getDate("face_value_update_date"));
				stockInfoObj.setBseSCGroup(rs.getString("bse_sc_group"));
				stockInfoObj.setNseEquitySeries(rs.getString("nse_equity_series"));
				
				//  Since we are going to compare 
				// whether this mcLink is already present in the database with the 
				// mcLink that is obtained from the mc. 
				stockInfoMap.put(stockInfoObj.getIsin(), stockInfoObj);
			}
		}finally{
			stmt.close();
		}
		return stockInfoMap;
	}
	
	public boolean updateStockInfoData(Map<String, StockInfoBean> siUpdateMap) throws SQLException{
		
		int counter = 0;
		int [] committedList = null;
		PreparedStatement preStmt = null;
		
		// isin is the key of the MAP
		String strISIN = null;
		StockInfoBean siBean = null;
		
		/*UPDATE tab_stock_info SET mclink = ?, ")
		.append(" company_name = ?, sector_name = ?, ipo =?, ")
		.append(" face_value = ?, face_value_update_date = ?, market_cap = ? WHERE isin = ?")*/
		
		try{
			
			preStmt = conn.prepareStatement(SQLUQConst.SI_UPDATE.toString());
			
			conn.setAutoCommit(false);
			
			for (Map.Entry<String, StockInfoBean> entry : siUpdateMap.entrySet()) {
				siBean = entry.getValue();

				preStmt.setString(1, siBean.getMcLink());
				preStmt.setString(2, siBean.getCompanyName());
				preStmt.setString(3, siBean.getSectorName());
				preStmt.setDate(4, siBean.getIpo());
				preStmt.setDouble(5, siBean.getFaceValue());
				preStmt.setDate(6, siBean.getFaceValueDate());
				preStmt.setDouble(7, siBean.getMarketCap());
				preStmt.setString(8, siBean.getIsin());

				preStmt.addBatch();

				if (counter++ == 100) {
					committedList = preStmt.executeBatch();
					counter = 0;
				}
			}
			if(counter < 100){
				committedList = preStmt.executeBatch();
			}
			conn.commit();
		}finally{
			
		}
		return true;
	}
}
