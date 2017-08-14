package constants;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLSelectQueryConstant.
 */
public class SQLSelectQueryConstant {
	
	// get data from table tab_resource
	public static final String DB_SELECT_RESOURCE_TABLE = "SELECT stock_all, stock_all_seq FROM tab_resource";
	
	/** The Constant DB_SELECT_RESOURCE_TABLE. */
	public static final StringBuilder DB_SELECT_STOCK_INFO_TABLE = new StringBuilder("SELECT isin, mclink, bse_symbol, nse_symbol,")
		.append("company_name, sector_name, face_value, market_cap,")
		.append("ipo, face_value_update_date, bse_sc_group, nse_equity_series FROM tab_stock_info");
	
	public static final StringBuilder DB_SELECT_MCLINK_TABLE = new StringBuilder("SELECT ")
			.append(" mclink FROM tab_stock_mclink limit 0, 800");
}
