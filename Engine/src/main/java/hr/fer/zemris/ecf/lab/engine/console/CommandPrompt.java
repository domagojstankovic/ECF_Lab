package hr.fer.zemris.ecf.lab.engine.console;

import java.io.IOException;

/**
 * This class is the Window's cmd implementation of {@link Console}.
 *
 * @version 1.0
 */
public class CommandPrompt implements Console {

    @Override
    public void pardump(String ecfPath, String pardumpPath) {

        try {
            /**
             * Process is created because it has nice waitFor method which waits for c process to end
             * and doesn't execute Java code till then.
             */
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
        String command = job.ecfPath + " " + job.configPath;
        synchronized (System.out) {
            System.out.println("cmd.exe /c \"" + command + "\"");
        }
        try {
            String cmd3 = "\"" + command + "\"";
            Process process = new ProcessBuilder("cmd.exe", "/c", cmd3).start();
            process.waitFor();
            ProcessOutput output = new ProcessOutput(process.getInputStream(), process.getErrorStream());
            job.finished(output);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            job.failed();
        }

    }

}
