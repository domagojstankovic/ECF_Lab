package hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.conf;

import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.param.AlgGenRegUser;
import hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.param.ParametersList;

import java.io.File;

/**
 * Created by Domagoj on 02/03/15.
 */
public interface ConfigurationReader {

    ParametersList readInitial(File file);

    AlgGenRegUser readArchive(File file);

}
