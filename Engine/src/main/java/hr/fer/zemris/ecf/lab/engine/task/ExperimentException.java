package hr.fer.zemris.ecf.lab.engine.task;

/**
 * Created by Domagoj on 03/05/15.
 */
public class ExperimentException extends RuntimeException {

    public ExperimentException() {
    }

    public ExperimentException(String message) {
        super(message);
    }

    public ExperimentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExperimentException(Throwable cause) {
        super(cause);
    }

    public ExperimentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
