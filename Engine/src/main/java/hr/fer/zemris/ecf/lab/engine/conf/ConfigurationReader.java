package hr.fer.zemris.ecf.lab.engine.conf;

import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.ParametersList;

import java.io.File;

/**
 * Created by Domagoj on 02/03/15.
 */
public interface ConfigurationReader {

    ParametersList readInitial(File file);

    Configuration readArchive(File file);

}
