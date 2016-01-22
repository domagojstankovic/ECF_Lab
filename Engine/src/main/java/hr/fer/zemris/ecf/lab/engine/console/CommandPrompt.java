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
        String command = job.getEcfPath() + " " + job.getConfigPath();
        print("cmd.exe /c \"" + command + "\"");
        String cmd3 = "\"" + command + "\"";
        runProcess(job, "cmd.exe", "/c", cmd3);
    }
}
