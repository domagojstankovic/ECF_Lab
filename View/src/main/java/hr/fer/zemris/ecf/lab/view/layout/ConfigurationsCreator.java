package hr.fer.zemris.ecf.lab.view.layout;

import hr.fer.zemris.ecf.lab.engine.param.Configuration;

import java.util.List;

/**
 * Created by dstankovic on 3/1/16.
 */
public interface ConfigurationsCreator {
  List<Configuration> createConfigurations(List<MultiEntryBlock> algorithmList,
                                           List<List<MultiEntryBlock>> genotypeListBlock,
                                           MultiEntryBlock registry);
}
