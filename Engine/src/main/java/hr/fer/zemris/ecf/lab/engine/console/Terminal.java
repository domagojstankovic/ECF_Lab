package hr.fer.zemris.ecf.lab.engine.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class is the Unix'x terminal implementation of {@link Console}.
 *
 * @version 1.0
 */
public class Terminal implements Console {

    @Override
    public void pardump(String ecfPath, String pardumpPath) {
        try {
            File file = new File(pardumpPath);
            file.getParentFile().mkdirs();
            Process process = new ProcessBuilder(ecfPath, "-gui", "-pardump", pardumpPath).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Problem with writing to linux terminal.");
            e.printStackTrace();
        }

    }

    @Override
    public void execute(Job job) {
        String programPath = job.getEcfPath();
        String args = job.getConfigPath();
        print(programPath + " " + args);
        try {
            job.started();
            File stdoutFile = File.createTempFile("ecflab-stdout", ".txt");
            File stderrFile = File.createTempFile("ecflab-stderr", ".txt");

            ProcessBuilder pb = new ProcessBuilder(programPath, args);
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

    private void print(String str) {
        synchronized (System.out) {
            System.out.println(str);
        }
    }
}
