package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.console.Job;

import java.io.File;

/**
 * Created by dstankovic on 4/5/16.
 */
public class ExperimentHandlerUtils {
  public static void deleteConfIfNeeded(Job job) {
    if (job.shouldDeleteConf()) {
      // job was created with implicit parallelism, delete configuration file
      String confPath = job.getConfigPath();
      File configFile = new File(confPath);
      if (configFile.exists()) {
        configFile.delete();
      }
    }
  }
}
