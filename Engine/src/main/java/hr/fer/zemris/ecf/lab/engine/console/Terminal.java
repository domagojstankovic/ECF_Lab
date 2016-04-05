package hr.fer.zemris.ecf.lab.engine.console;

import java.io.File;
import java.io.IOException;

/**
 * This class is the Unix'x terminal implementation of {@link Console}.
 *
 * @version 1.0
 */
public class Terminal extends AbstractConsole {
    @Override
    public void pardump(String ecfPath, String pardumpPath) {
        try {
            File file = new File(pardumpPath);
            file.getParentFile().mkdirs();
            Process process = new ProcessBuilder(ecfPath, "-gui", "-pardump", pardumpPath).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Problem with writing to unix terminal.");
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Job job) {
        String programPath = job.getEcfPath();
        String args = job.getConfigPath();
        print(programPath + " " + args);
        runProcess(job, programPath, args);
    }

    @Override
    public void execute(Job job, File stdoutFile, File stderrFile) {
        String programPath = job.getEcfPath();
        String args = job.getConfigPath();
        print(programPath + " " + args);
        runProcess(job, stdoutFile, stderrFile, programPath, args);
    }
}
