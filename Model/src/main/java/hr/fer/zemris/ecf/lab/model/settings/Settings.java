package hr.fer.zemris.ecf.lab.model.settings;

/**
 * Retrieves values from configuration object.
 * @author Domagoj Stanković
 * @version 1.0
 */
public interface Settings {
	
	/**
	 * Retrieves value mapped by specified key from the configuration object.
	 * @param key Configuration key
	 * @return Value mapped by configuration key in the configuration object
	 */
	String getValue(String key);
	
	/**
	 * Changes value under a specified key.
	 * @param key Configuration key
	 * @param value New value
	 */
	void changeValue(String key, String value);
	
}
