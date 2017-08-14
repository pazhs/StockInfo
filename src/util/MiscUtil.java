package util;

public class MiscUtil {
	
	public static String getMonthString(int month) throws Exception{
		String strMonth = null;
		
		try{
			switch (month) {
            case 1:  strMonth = "JAN";
                     break;
            case 2:  strMonth = "FEB";
                     break;
            case 3:  strMonth = "MAR";
                     break;
            case 4:  strMonth = "APR";
                     break;
            case 5:  strMonth = "MAY";
                     break;
            case 6:  strMonth = "JUN";
                     break;
            case 7:  strMonth = "JUL";
                     break;
            case 8:  strMonth = "AUG";
                     break;
            case 9:  strMonth = "SEP";
                     break;
            case 10: strMonth = "OCT";
                     break;
            case 11: strMonth = "NOV";
                     break;
            case 12: strMonth = "DEC";
                     break;
            default: throw new Exception();
        }
		}catch(Exception ex){
			throw ex;
		}
		return strMonth;
	}
}
