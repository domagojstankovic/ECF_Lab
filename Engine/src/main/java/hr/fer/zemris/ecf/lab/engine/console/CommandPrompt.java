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
//			System.out.println("cmd.exe /c \""+(ecfPath + " " + controlString + " " + paramsPath)+"\"");
            String cmd3 = "\"" + (ecfPath + " " + controlString + " " + pardumpPath) + "\"";
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", cmd3); // ispravljeno, parametri se moraju odvojiti, ne mogu se samo slijepiti stringovi
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
        String path = job.ecfPath + ">";
        String errorFilePath = job.logFilePath + ".err";
        //		String command = job.logFilePath + " " + job.paramsPath;
        String command = job.logFilePath + " 2> " + errorFilePath + " " + job.paramsPath;
        Process process;
        synchronized (System.out) {
            System.out.println("cmd.exe /c \"" + path + "" + command + "\"");
        }
        try {
            String cmd3 = "\"" + path + "" + command + "\"";
            process = new ProcessBuilder("cmd.exe", "/c", cmd3).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
