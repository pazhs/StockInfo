package exception;

public class StockInfoCustomException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 967125573814435313L;

	public StockInfoCustomException() {
		super();
	}

	public StockInfoCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public StockInfoCustomException(String message, Throwable cause) {
		super(message, cause);
	}

	public StockInfoCustomException(String message) {
		super(message);
	}

	public StockInfoCustomException(Throwable cause) {
		super(cause);
	}
}
