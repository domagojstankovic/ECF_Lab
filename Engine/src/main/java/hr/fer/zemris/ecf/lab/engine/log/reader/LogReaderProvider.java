package hr.fer.zemris.ecf.lab.engine.log.reader;

/**
 * Created by Domagoj on 01/05/15.
 */
public class LogReaderProvider {

    private LogReader reader = null;

    private LogReaderProvider() {
    }

    public LogReader getReader() {
        if (reader == null) {
            reader = new DefaultLogReader();
        }
        return reader;
    }
}
