package hr.fer.zemris.ecf.lab.engine.log.reader;

import hr.fer.zemris.ecf.lab.engine.log.LogModel;

import java.io.InputStream;

/**
 * Created by Domagoj on 01/05/15.
 */
public interface LogReader {

    LogModel read(InputStream is);

}
