package hr.fer.zemris.ecf.lab.model.logger.impl;

import hr.fer.zemris.ecf.lab.model.logger.Logger;
import hr.fer.zemris.ecf.lab.model.logger.LoggerException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * Logs to file.
 * @author Domagoj Stanković
 * @version 1.0
 */
public class FileLogger implements Logger {

	private String filePath;

	/**
	 * @param filePath Path to the log file.
	 */
	public FileLogger(String filePath) {
		super();
		this.filePath = filePath;
	}

	@Override
	public void log(String message) {
		try {
			FileWriter fw = new FileWriter(filePath, true);
			String string = Calendar.getInstance().getTime().toString() + "\n" + message + "\n-----------------------\n";
			fw.write(string);
			fw.close();
		} catch (IOException e) {
			throw new LoggerException("Writing to log file failed!");
		}
	}

	@Override
	public void log(Exception e) {
		StringBuilder sb = new StringBuilder(e.getMessage());
		StackTraceElement[] ste = e.getStackTrace();
		for (StackTraceElement el : ste) {
			sb.append("\n" + el.toString());
		}
		log(sb.toString());
	}

}
