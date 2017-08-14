package constants;

public class SQLIQConst {
	
	public static final StringBuilder DB_INSERT_MCLINK_TABLE = new StringBuilder("INSERT ")
			.append(" INTO tab_stock_mclink (mclink, update_date) ")
			.append(" VALUES(?, ?)");
	
	public static final StringBuilder DB_INSERT_SI_TABLE = new StringBuilder("INSERT ")
			.append(" INTO tab_stock_info (isin, mclink, bse_symbol, nse_symbol, ")
			.append(" company_name, sector_name, face_value, market_cap, bse_sc_group, nse_equity_series) ")
			.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

}
