package hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.console;

import java.io.IOException;

/**
 * This class is the Unix'x terminal implementation of {@link Console}.
 * @version 1.0
 *
 */
public class Terminal implements Console {

	@Override
	public void pardump(String ecfPath, String pardumpPath) {
		try {

			Process process = new ProcessBuilder(ecfPath, "-gui", "-pardump", pardumpPath).start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			System.err.println("Problem with writing to linux terminal.");
			e.printStackTrace();
		}

	}

	@Override
	public void execute(Job job) {
		String errorFilePath = job.logFilePath + ".err";
		String command = job.ecfPath + " > " + job.logFilePath + " 2> " + errorFilePath + " " + job.paramsPath;
		synchronized (System.out) {
			System.out.println(command);
		}
		try {
			Process process = new ProcessBuilder(command).start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
