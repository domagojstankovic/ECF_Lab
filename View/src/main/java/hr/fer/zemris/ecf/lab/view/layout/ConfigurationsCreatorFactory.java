package hr.fer.zemris.ecf.lab.view.layout;

/**
 * Created by dstankovic on 3/1/16.
 */
public class ConfigurationsCreatorFactory {
  public static ConfigurationsCreator create() {
    return new DefaultConfigurationsCreator();
  }
}
