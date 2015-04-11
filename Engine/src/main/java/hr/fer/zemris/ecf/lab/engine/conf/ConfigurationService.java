package hr.fer.zemris.ecf.lab.engine.conf;

/**
 * Created by Domagoj on 02/03/15.
 */
public class ConfigurationService {

    private ConfigurationReader reader;
    private ConfigurationWriter writer;

    private static ConfigurationService instance;

    private ConfigurationService() {
    }

    public static ConfigurationService getInstance() {
        if (instance == null) {
            instance = new ConfigurationService();
        }
        return instance;
    }

    public ConfigurationReader getReader() {
        return reader;
    }

    public ConfigurationWriter getWriter() {
        return writer;
    }

    public void setReader(ConfigurationReader reader) {
        if (this.reader != null) {
            throw new IllegalStateException();
        }
        this.reader = reader;
    }

    public void setWriter(ConfigurationWriter writer) {
        if (this.writer != null) {
            throw new IllegalStateException();
        }
        this.writer = writer;
    }
}
