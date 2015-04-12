package hr.fer.zemris.ecf.lab.model.logger;

/**
 * Thrown when log error occurs.
 * @author Domagoj Stanković
 * @version 1.0
 */
public class LoggerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LoggerException() {
		super();
	}

	public LoggerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoggerException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoggerException(String message) {
		super(message);
	}

	public LoggerException(Throwable cause) {
		super(cause);
	}

}
