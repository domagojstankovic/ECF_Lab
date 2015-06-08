package hr.fer.zemris.ecf.lab.engine.conf;

/**
 * This exception is thrown if errors occur while reading configuration.
 *
 * Created by Domagoj on 08/06/15.
 */
public class ConfigurationReadingException extends RuntimeException {

    public ConfigurationReadingException() {
    }

    public ConfigurationReadingException(String message) {
        super(message);
    }

    public ConfigurationReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationReadingException(Throwable cause) {
        super(cause);
    }

    public ConfigurationReadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
