package hr.fer.zemris.ecf.lab.model.logger;

/**
 * Created by Domagoj on 08/04/15.
 */
public class LoggerProvider {

    private static Logger instance;

    private LoggerProvider() {
    }

    public static Logger getLogger() {
        return instance;
    }

    public static void setLogger(Logger logger) {
        if (instance != null) {
            throw new IllegalStateException("Looger implementation has already been set");
        }
        instance = logger;
    }

}
