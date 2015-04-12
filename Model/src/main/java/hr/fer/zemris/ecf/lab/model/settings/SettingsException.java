package hr.fer.zemris.ecf.lab.model.settings;

/**
 * Thrown when configuration error occurs.
 * @author Domagoj Stanković
 * @version 1.0
 */
public class SettingsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SettingsException() {
	}

	public SettingsException(String message) {
		super(message);
	}

	public SettingsException(Throwable cause) {
		super(cause);
	}

	public SettingsException(String message, Throwable cause) {
		super(message, cause);
	}

	public SettingsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
