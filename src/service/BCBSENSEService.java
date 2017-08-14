package service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;

import bean.StockInfoBean;

import java.io.BufferedReader;
import java.io.FileReader;

import constants.URLConst;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import util.MiscUtil;

/**
 * <p>
 * Class BCBSENSEService
 * - that is used to download the bhavcopies of NSE & BSE
 * - used to get the ISIN set from both the downloaded
 * - NSE & BSE bhavcopies
 * </p>
 * @author sundar
 *
 */
public class BCBSENSEService extends SIService{

	public BCBSENSEService() throws ClassNotFoundException, SQLException {
		super();
	}
	
	public boolean getBSEBhavCopyData() throws MalformedURLException, IOException, ZipException{
		
		Calendar nowDate = null;
		int dayofWeek = -1;
		
		StringBuilder sbDate = null;
		StringBuilder sbMonth = null;
		StringBuilder sbYear = null;
		
		StringBuilder bseURL = null;
		StringBuilder bseFileName = null;
		StringBuilder bseFolder = null;
		StringBuilder bseCSVBhavCopy = null;
		
		int dateOfMonth = -1;
		int month = -1;
		int year = -1;
		try{
			// get yesterday's bhavcopy,
			// if not available then get the latest bhavcopy
			// bse bhavcopy
			nowDate = Calendar.getInstance();
			dayofWeek = nowDate.get(Calendar.DAY_OF_WEEK);
			
			if(dayofWeek == 1){
				nowDate.add(Calendar.DATE, -2);
			}else if(dayofWeek == 2){
				nowDate.add(Calendar.DATE, -3);
			}else{
				nowDate.add(Calendar.DATE, -1);
			}
			
			dateOfMonth = nowDate.get(Calendar.DATE);
			
			sbDate = new StringBuilder();
			if(dateOfMonth < 10){
				sbDate.append("0");
			}
			sbDate.append(dateOfMonth);
			
			
			month = nowDate.get(Calendar.MONTH) + 1;
			
			sbMonth = new StringBuilder();
			if(month < 10){
				sbMonth.append("0");
			}
			sbMonth.append(month);
			
			year = nowDate.get(Calendar.YEAR);
			
			sbYear = new StringBuilder().append((year % 2000));
			
			// form the BSE's BhavCopy's URL
			// format "EQ_ISINCODE_100817.zip"
			bseURL = new StringBuilder(URLConst.BSE_BC_URL)
			.append("EQ_ISINCODE_").append(sbDate).append(sbMonth)
			.append(sbYear).append(".zip");
			
			bseCSVBhavCopy = new StringBuilder("C:\\Users\\sundar\\StockData\\bse\\")
					.append("EQ_ISINCODE_").append(sbDate).append(sbMonth)
					.append(sbYear).append(".CSV");
			
			bseFileName = new StringBuilder("C:\\Users\\sundar\\StockData\\bse\\bse.zip");
			bseFolder = new StringBuilder("C:\\Users\\sundar\\StockData\\bse\\");
			
			saveFileFromUrlWithCommonsIO(bseFileName.toString(), bseURL.toString());
			
			// uncompress the downloaded BSE bhavcopy
			unZipBCFile(bseFileName.toString(), bseFolder.toString());
			
			// read the bhavcopy CSV file and 
			// store the content into the MAP<ISIN, StockInfoBean>
			parseBhavCopy(bseCSVBhavCopy.toString(), true);
			
		}finally{
			
		}
		return true;
	}
	
	public boolean getNSEBhavCopyData() throws Exception{
		
		Calendar nowDate = null;
		int dayofWeek = -1;
		
		StringBuilder sbDate = null;
		String strMonth = null;
		String strYear = null;
		
		StringBuilder nseURL = null;
		StringBuilder nseFileName = null;
		StringBuilder nseFolder = null;
		StringBuilder nseCSVBhavCopy = null;
		
		int dateOfMonth = -1;
		int month = -1;
		int year = -1;
		
		try {
			// get yesterday's bhavcopy,
			// if not available then get the latest bhavcopy
			// bse bhavcopy
			nowDate = Calendar.getInstance();
			dayofWeek = nowDate.get(Calendar.DAY_OF_WEEK);

			if (dayofWeek == 1) {
				nowDate.add(Calendar.DATE, -2);
			} else if (dayofWeek == 2) {
				nowDate.add(Calendar.DATE, -3);
			} else {
				nowDate.add(Calendar.DATE, -1);
			}

			dateOfMonth = nowDate.get(Calendar.DATE);

			sbDate = new StringBuilder();
			if (dateOfMonth < 10) {
				sbDate.append("0");
			}
			sbDate.append(dateOfMonth);

			month = nowDate.get(Calendar.MONTH) + 1;
			strMonth = MiscUtil.getMonthString(month);

			year = nowDate.get(Calendar.YEAR);
			strYear = Integer.toString(year);

			// form the NSE's BhavCopy's URL
			// format "cm10AUG2017bhav.csv.zip"
			nseURL = new StringBuilder(URLConst.NSE_BC_URL).append(strYear).append("/").append(strMonth).append("/")
					.append("cm").append(sbDate).append(strMonth).append(strYear).append("bhav.csv.zip");

			nseCSVBhavCopy = new StringBuilder("C:\\Users\\sundar\\StockData\\nse\\").append("cm").append(sbDate)
					.append(strMonth).append(strYear).append("bhav.CSV");

			nseFileName = new StringBuilder("C:\\Users\\sundar\\StockData\\nse\\").append("cm").append(sbDate)
					.append(strMonth).append(strYear).append("bhav.csv.zip");
			nseFolder = new StringBuilder("C:\\Users\\sundar\\StockData\\nse\\");

			saveFileFromUrlWithCommonsIO(nseFileName.toString(), nseURL.toString());

			// uncompress the downloaded BSE bhavcopy
			unZipBCFile(nseFileName.toString(), nseFolder.toString());

			// read the bhavcopy CSV file and
			// store the content into the MAP<ISIN, StockInfoBean>
			parseBhavCopy(nseCSVBhavCopy.toString(), false);

		} finally {
			
		}
		return true;
	}
	
	private boolean saveFileFromUrlWithCommonsIO(String fileName,
			 String fileUrl) throws MalformedURLException, IOException{
		
		FileUtils.copyURLToFile(new URL(fileUrl), new File(fileName));
		
		return true;
	}
	
	private boolean parseBhavCopy(String bhavCopy, boolean isBSE) throws IOException{
		
        String line = "";
        String cvsSplitBy = ",";
        String [] dataArr = null;
        
        int header = 1;
        
        String bseSymbol = null;
        String bseSCGroup = null;
        String bseSCType = null;
        String ISIN = null;
        
        String nseSymbol = null;
        String nseSeries = null;
        
        StockInfoBean siBean = null;
        
		try{
			BufferedReader br = new BufferedReader(new FileReader(bhavCopy));
			
			 while ((line = br.readLine()) != null) {
				 dataArr = line.split(cvsSplitBy);
				 
				 if(header++ == 1){
					 // skip the header
					 continue;
				 }
				 
				 if(isBSE){
					 bseSymbol = null;
					 bseSCGroup = null;
				     bseSCType = null;
				     ISIN = null;
					 
					 bseSymbol = dataArr[0].trim();
					 bseSCGroup = dataArr[2].trim();
					 bseSCType = dataArr[3].trim();
					 ISIN = dataArr[14].trim();
					 
					 if(bseSCType.compareToIgnoreCase("Q") == 0){
						// form the StockInfoBean
						 siBean = new StockInfoBean();
						 siBean.setIsin(ISIN);
						 siBean.setBseSymbol(bseSymbol);
						 siBean.setBseSCGroup(bseSCGroup);
						 
						 m_siBCMap.put(ISIN, siBean);
					 }
				 } else{
					 nseSymbol = null;
				     nseSeries = null;
				     
				     nseSymbol = dataArr[0].trim();
					 nseSeries = dataArr[1].trim();
					 ISIN = dataArr[12].trim();
					 
					 if(m_siBCMap.containsKey(ISIN)){
						 siBean = m_siBCMap.get(ISIN);
					 } else{
						 siBean = new StockInfoBean();
						 siBean.setIsin(ISIN);
						 m_siBCMap.put(ISIN, siBean);
					 }
					 siBean.setNseSymbol(nseSymbol);
					 siBean.setNseEquitySeries(nseSeries);
				 }
			 }
		}finally{
			
		}
		return true;
	}
	
	private boolean unZipBCFile(String source, String destination) throws ZipException{

	    try {
	         ZipFile zipFile = new ZipFile(source);
	         if (zipFile.isEncrypted()) {
	            zipFile.setPassword("");
	         }
	         zipFile.extractAll(destination);
	    } catch (ZipException e) {
	        throw e;
	    }
		return true;
	}

}
