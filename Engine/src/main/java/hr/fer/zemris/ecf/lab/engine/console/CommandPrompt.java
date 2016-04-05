package hr.fer.zemris.ecf.lab.engine.console;

import java.io.File;
import java.io.IOException;

/**
 * This class is the Window's cmd implementation of {@link Console}.
 *
 * @version 1.0
 */
public class CommandPrompt extends AbstractConsole {
  @Override
  public void pardump(String ecfPath, String pardumpPath) {
    try {
      File file = new File(pardumpPath);
      file.getParentFile().mkdirs();
      String controlString = "-gui -pardump";
      String cmd3 = "\"" + (ecfPath + " " + controlString + " " + pardumpPath) + "\"";
      ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", cmd3);
      Process process = pb.start();
      process.waitFor();
    } catch (IOException e) {
      System.err.println("Problem with writing to cmd.");
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void execute(Job job) {
    String[] cmd = makeCommand(job);
    print(arrayToString(cmd));
    runProcess(job, cmd);
  }

  @Override
  public void execute(Job job, File stdoutFile, File stderrFile) {
    String[] cmd = makeCommand(job);
    print(arrayToString(cmd));
    runProcess(job, stdoutFile, stderrFile, cmd);
  }

  private static String[] makeCommand(Job job) {
    String command = job.getEcfPath() + " " + job.getConfigPath();
    String cmd3 = "\"" + command + "\"";
    String[] cmd = new String[] {"cmd.exe", "/c", cmd3};
    return cmd;
  }

  private static String arrayToString(String[] arr) {
    StringBuilder sb = new StringBuilder(arr[0]);
    for (int i = 1; i < arr.length; i++) {
      sb.append(' ').append(arr[i]);
    }
    return sb.toString();
  }
}
