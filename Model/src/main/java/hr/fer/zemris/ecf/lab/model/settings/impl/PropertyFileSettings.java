package hr.fer.zemris.ecf.lab.model.settings.impl;

import hr.fer.zemris.ecf.lab.model.settings.SettingsException;
import hr.fer.zemris.ecf.lab.model.settings.Settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Configuration based on the .properties file.
 * @author Domagoj Stanković
 * @version 1.0
 */
public class PropertyFileSettings implements Settings {

	private Properties properties = new Properties();
	private String filePath;
	
	/**
	 * @param filePath Path of the .properties configuration file
	 * @throws java.io.IOException If file cannot be opened or read
	 */
	public PropertyFileSettings(String filePath) {
		super();
		this.filePath = filePath;
		try {
			read();
		} catch (IOException | NullPointerException e) {
			throw new SettingsException("Configuration file reading failed!");
		}
	}

	private void read() throws IOException {
		InputStream is = new FileInputStream(filePath);
		properties.load(is);
		is.close();
	}

	@Override
	public String getValue(String key) {
		return properties.getProperty(key);
	}

}
