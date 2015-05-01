package hr.fer.zemris.ecf.lab.engine.console;

import java.io.InputStream;

/**
 * Created by Domagoj on 15/04/15.
 */
public class ProcessOutput {

    private InputStream stdout;
    private InputStream stderr;

    public ProcessOutput(InputStream stdout, InputStream stderr) {
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public InputStream getStdout() {
        return stdout;
    }

    public InputStream getStderr() {
        return stderr;
    }
}
