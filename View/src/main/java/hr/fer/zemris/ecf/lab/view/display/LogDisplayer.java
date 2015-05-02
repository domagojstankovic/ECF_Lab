package hr.fer.zemris.ecf.lab.view.display;

import hr.fer.zemris.ecf.lab.engine.log.LogModel;

/**
 * Object that can display result.
 *
 * @author Domagoj
 */
public interface LogDisplayer {

    /**
     * Displays chart in a new frame. Reads results from the log file and then
     * displays it.
     *
     * @param log Log model.
     */
    void displayLog(LogModel log);

}
