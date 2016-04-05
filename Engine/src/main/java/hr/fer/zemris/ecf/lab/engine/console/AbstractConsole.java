package hr.fer.zemris.ecf.lab.engine.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Domagoj on 16/05/15.
 */
public abstract class AbstractConsole implements Console {
  protected void runProcess(Job job, String... args) {
    try {
      File stdoutFile = File.createTempFile("ecflab-stdout", ".txt");
      File stderrFile = File.createTempFile("ecflab-stderr", ".txt");
      runProcess(job, stdoutFile, stderrFile, args);
    } catch (IOException e) {
      e.printStackTrace();
      job.failed();
    }
  }

  protected void runProcess(Job job, File stdoutFile, File stderrFile, String... args) {
    try {
      job.started();

      ProcessBuilder pb = new ProcessBuilder(args);
      pb.redirectOutput(stdoutFile);
      pb.redirectError(stderrFile);
      Process process = pb.start();
      process.waitFor();

      ProcessOutput output = new ProcessOutput(new FileInputStream(stdoutFile), new FileInputStream(stderrFile));
      job.finished(output);

      stdoutFile.deleteOnExit();
      stderrFile.deleteOnExit();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      job.failed();
    }
  }

  protected void print(String str) {
    synchronized (System.out) {
      System.out.println(str);
    }
  }
}
