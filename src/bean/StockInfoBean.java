package bean;

import java.io.Serializable;
import java.sql.Date;

public class StockInfoBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// mc web link
	private String mcLink;
	
	// stock's isin symbol primary key
	private String isin;
	
	// bseSymbol of the stock
	private String bseSymbol;
	
	// nseSymbol of the stock
	private String nseSymbol;
	
	// sector the stock/company belongs to
	private String sectorName;
	
	private String companyName;
	
	// face value of the stock during the fetching date
	private double faceValue;
	
	// market cap of the stock during the fetching date (in crores)
	private double marketCap;
	
	private Date ipo;
	
	private Date faceValueDate;
	
	private String bseSCGroup;
	
	private String nseEquitySeries;
	
	public String getNseEquitySeries() {
		return nseEquitySeries;
	}

	public void setNseEquitySeries(String nseEquitySeries) {
		this.nseEquitySeries = nseEquitySeries;
	}

	public String getBseSCGroup() {
		return bseSCGroup;
	}

	public void setBseSCGroup(String bseSCGroup) {
		this.bseSCGroup = bseSCGroup;
	}

	public Date getIpo() {
		return ipo;
	}

	public void setIpo(Date ipo) {
		this.ipo = ipo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getFaceValueDate() {
		return faceValueDate;
	}

	public void setFaceValueDate(Date faceValueDate) {
		this.faceValueDate = faceValueDate;
	}

	public String getMcLink() {
		return mcLink;
	}

	public void setMcLink(String mcLink) {
		this.mcLink = mcLink;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getBseSymbol() {
		return bseSymbol;
	}

	public void setBseSymbol(String bseSymbol) {
		this.bseSymbol = bseSymbol;
	}

	public String getNseSymbol() {
		return nseSymbol;
	}

	public void setNseSymbol(String nseSymbol) {
		this.nseSymbol = nseSymbol;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public double getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(double faceValue) {
		this.faceValue = faceValue;
	}

	public double getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(double marketCap) {
		this.marketCap = marketCap;
	}
}
