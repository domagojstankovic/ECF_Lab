package hr.fer.zemris.ecf.lab.engine.console;

import java.io.File;

/**
 * This interface describes methods that are used for communication to the ECF through the different operating systems terminal/cmd.
 * This interface is implemented by classes that specify this type of communication for specific operating system.
 *
 * @version 1.0
 */
public interface Console {

  /**
   * This method is used for getting initial ECF's xml parameters.
   *
   * @param ecfPath     Path to the ECF's .exe file.
   * @param pardumpPath Path to file where initial ECF's parameters will be dumped. If file exists it will be overridden.
   */
  void pardump(String ecfPath, String pardumpPath);

  /**
   * This method is used for starting one of ECF's computations with given {@link Job}.
   *
   * @param job class job with needed parameters for one of ECF's computation to start.
   */
  void execute(Job job);

  /**
   * Used for executing a job with predefined stdout and stderr files. Used for live reading.
   */
  void execute(Job job, File stdoutFile, File stderrFile);
}
