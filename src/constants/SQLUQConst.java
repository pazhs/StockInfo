package constants;

public class SQLUQConst {
	
	public static final StringBuilder SI_UPDATE = new StringBuilder("UPDATE tab_stock_info SET mclink = ?, ")
			.append(" company_name = ?, sector_name = ?, ipo =?, ")
			.append(" face_value = ?, face_value_update_date = ?, market_cap = ? WHERE isin = ?");

}
