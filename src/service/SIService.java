package service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import bean.StockInfoBean;
import constants.StockInfoStrLtrBaseConstant;
import dao.StockInfoDAO;
import logger.LoggerUtil;

public class SIService {
	
	// common list for both BSE and NSE
	public static Set<String> m_mcLinkSet = null;
	
	public static Set<String> m_dbLinkSet = null;
	
	// Map that is obtained from the BSE & NSE BhavCopies
	public static Map<String, StockInfoBean> m_siBCMap = null;
	// common MAP that is currently present in the DB
	// for both BSE and NSE
	public static Map<String, StockInfoBean> m_siMap = null;
	
	// new StockInfo data that has to be added to the database
	public static Map<String, StockInfoBean> m_siAddMap = null;
	
	// StockInfo data that has to be updated to the database
	public static Map<String, StockInfoBean> m_siUpdateMap = null;
	
	public static Map<String, String> m_mcResourceMap = null;
	
	/** The dao obj. */
	private StockInfoDAO daoObj = null;
	
	public SIService() throws ClassNotFoundException, SQLException{
		
		try{
			if(m_mcLinkSet == null) {
				m_mcLinkSet = new LinkedHashSet<String>();
			}
			if(m_siMap == null){
				m_siMap = new LinkedHashMap<String, StockInfoBean>();
			}
			if(daoObj == null){
				daoObj = new StockInfoDAO();
				
				if(m_mcResourceMap == null){
					m_mcResourceMap = daoObj.fetchMCResource(); 
				}
				
				if( m_siBCMap == null){
					m_siBCMap = new LinkedHashMap<String, StockInfoBean>();
				}
				if(m_siMap == null){
					m_siMap = new LinkedHashMap<String, StockInfoBean>();
				}
				if( m_siAddMap == null){
					m_siAddMap = new LinkedHashMap<String, StockInfoBean>();
				}
				if(m_siUpdateMap == null){
					m_siUpdateMap = new LinkedHashMap<String, StockInfoBean>();
				}
			}
		}finally{
			
		}
	}
	
	public void closeDBConnection() {
		daoObj.closeConnection();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public boolean addMCLinkSet() throws SQLException{
		
		try{
			daoObj.addMCLinkSet(m_mcLinkSet);
		}finally{
			
		}
		return true;
	}
	
	public boolean addUpdateDatabaseWithSIData() throws SQLException{
		
		try{
			daoObj.addUpdateDatabaseWithSIData(m_siAddMap, m_siUpdateMap);
			
		}finally{
			
		}
		return true;
	}
	
	public boolean compareBhavCopyDB(){
		String strISIN = null;
		StockInfoBean siBean = null;
		
		StringBuilder sb = null;
		
		try{
			
			sb = new StringBuilder("Adding info of \"%s\" to the database");
			// MAP that contains data from BhavCopy
			for (Map.Entry<String, StockInfoBean> entry : m_siBCMap.entrySet()){
				strISIN = entry.getKey();
				siBean = entry.getValue();
				// whether BhavCopy's ISIN is already in the database
				if(m_siMap.containsKey(strISIN)){
					
					// database has to be updated
					m_siUpdateMap.put(strISIN, siBean);
				}else{
					m_siAddMap.put(strISIN, siBean);
					
					// write the log here
					// Show which ISIN has been added newly to the database
					LoggerUtil.info(String.format(sb.toString(), strISIN));
				}
			}
		}finally{
			
		}
		return true;
	}
	
	public boolean getDBStockInfoMap() throws SQLException{
		
		try{
			m_siMap = daoObj.getDBStockInfoMap();
		}finally{
			
		}
		
		return true;
	}
	
	public boolean updateDBWithSIData() throws SQLException, IOException{
		try{
			
			// Get the first 800 entries from tab_stock_mclink
			// and get StockInfo data from MC
			// compare that MC's data with SIData that is available in database
			// update the MC's data to database.
			// after that delete the obtained 800 entries from the database
			if(getMCLinkSetFromDB()){
				getStockInfoFromMC();
				
				updateSIDataToDB();
				
				deleteUpdatedMcLinks();
				
				// repeat the process again
				// updateDBWithSIData();
			}
		}finally{
			
		}
		return true;
	}
	
	private boolean deleteUpdatedMcLinks() throws SQLException{
		
		try{
			daoObj.deleteUpdatedMcLinks(m_dbLinkSet);
		}finally{
			
		}
		return true;
	}
	
	private boolean updateSIDataToDB() throws SQLException{
		try{
			daoObj.updateStockInfoData(m_siMap);
		}finally{
			
		}
		
		return true;
	}
	
	private boolean getStockInfoFromMC() throws IOException{
		StockInfoBean siMCBean = null;
		String strMCIsin = null;
		
		StockInfoBean siDBMapBean = null;
		
		try{
			for(String mcLink : m_dbLinkSet){
				siMCBean = getStockInfoFromMC(mcLink);
				
				if(siMCBean == null){
					continue;
				}
				
				strMCIsin = siMCBean.getIsin();
				
				if(m_siMap.containsKey(strMCIsin)){
					siDBMapBean = m_siMap.get(strMCIsin);
					
					siDBMapBean.setMcLink(mcLink);
					siDBMapBean.setCompanyName(siMCBean.getCompanyName());
					siDBMapBean.setSectorName(siMCBean.getSectorName());
					siDBMapBean.setFaceValue(siMCBean.getFaceValue());
					siDBMapBean.setMarketCap(siMCBean.getMarketCap());
				}
			}
		}finally{
			
		}
		return true;
	}
	
	private boolean getMCLinkSetFromDB() throws SQLException{
		try{
			m_dbLinkSet = daoObj.getMCLinkSetFromDB();
		}finally{
			
		}
		if(m_dbLinkSet.size() > 0){
			return true;
		}
		return false;
	}
	
	/*
	 * 
	 */
	private StockInfoBean getStockInfoFromMC(String mcLink) throws IOException{
		
		StockInfoBean siBean = null;
		Document doc = null;
		
		Elements comInfoNameContents = null;
		Elements comInfoContents = null;
		Elements marketCapContents = null;
		
		String strCompanyName = null;
		String strCompanyInfo = null;
		String strBSESymbol = null;
		String strNSESymbol = null;
		String strISIN = null;
		String strSector = null;
		String [] infoArray = null;
		
		
		
		double marketCap = 0.00;
		double faceValue = 0.00;
		
		int index = 0;
		StringBuilder sb = new StringBuilder();
		
		try{
			
			doc = Jsoup.connect(mcLink).get();
			
			comInfoNameContents = doc.select("div.b_42, div.PT5 PR");
			
			for(Element content : comInfoNameContents) {
				strCompanyName = content.text().trim();
				break;
			}
			
			comInfoContents = doc.select("div.FL.gry10");
			
			for(Element content : comInfoContents) {
				
				infoArray = null;
				strCompanyInfo = content.text().trim();
				infoArray = strCompanyInfo.split("\\|");
				
				// bse
				strBSESymbol = (infoArray[0].split(":"))[1];
				
				// nse
				strNSESymbol = (infoArray[1].split(":"))[1];
				
				// isin
				strISIN = (infoArray[2].split(":"))[1].trim();
				
				if(strISIN.isEmpty()){
					return null;
				}
				
				// sector
				strSector = (infoArray[3].split(":"))[1];
			}
			
			// MarketCap and FaceValue 
			marketCapContents = doc.select("div.FL.gL_10.UC");
			for (Element subContent : marketCapContents) {
				String Tag = subContent.text().trim();
				String TagValue = subContent.nextElementSibling().text().trim();
				
				if(Tag.compareToIgnoreCase("MARKET CAP (Rs Cr)") == 0) {
					
					if(sb.length() > 0){
						sb.delete(0, (sb.length() - 1));
					}
					sb.append(TagValue);
					
					index = sb.toString().indexOf(',');
					while (index >= 0){
						sb.deleteCharAt(index);
					    index = sb.toString().indexOf(',', index + 1);
					}
					
					marketCap = Double.parseDouble(sb.toString());
				}
				
				if(TagValue != null && Tag.compareToIgnoreCase("FACE VALUE (Rs)") == 0 && !TagValue.isEmpty()) {
					faceValue = Double.parseDouble(TagValue);
				}
			}
			
			if(strISIN != null && !strISIN.trim().isEmpty()){
				
				siBean = new StockInfoBean();
				// set the StockInfoBean
				siBean.setMcLink(mcLink.trim());
				siBean.setIsin(strISIN.trim());
				siBean.setCompanyName(strCompanyName.trim());
				siBean.setBseSymbol(strBSESymbol.trim());
				siBean.setNseSymbol(strNSESymbol.trim());
				siBean.setSectorName(strSector.trim());
				siBean.setFaceValue(faceValue);
				siBean.setMarketCap(marketCap);
				
				System.out.println("Getting data for company : " + strCompanyName);
			}
		}finally{
			
		}
		return siBean;
	}
	
	/**
	 * <p>
	 * method that is used to get the Set of MCLinks from mc.
	 * </p>
	 * @return
	 * @throws IOException 
	 */
	public boolean getMCLinkSet() throws IOException{
		
		String stockAll = null;
		String stockAllSeq = null;
		
		try{
			stockAll = m_mcResourceMap.get("stockAll");
			stockAllSeq = m_mcResourceMap.get("stockAllSeq");
			
			// Sets the m_mcLinkSet with the data obtained from mc.
			getMCLinkSet(stockAll, stockAllSeq);
			
		}finally{
			
		}
		return true;
	}
	
	private boolean getMCLinkSet(String stockAll, String stockAllSeq) throws IOException{
		
		String[] seqArr = null;
		StringBuilder sb = null;
		
		Document doc = null;
		
		Elements contents = null;
		
		try{
			seqArr = stockAllSeq.split(StockInfoStrLtrBaseConstant.STR_COMMA);
			
			for (String letter : seqArr) {
				sb = new StringBuilder(stockAll).append(letter);
				
				doc = Jsoup.connect(sb.toString()).get();
				contents = doc.select("td a.bl_12");
				
				for (Element content : contents) {
					String hRef = content.attr("href").trim();
					
					if(hRef.isEmpty()){
						continue;
					}
					
					m_mcLinkSet.add(hRef);
				}
			}
			
		}finally{
			
		}
		return true;
	}
}
