package hr.fer.zemris.ecf.lab.engine.param;

/**
 * This class represents one entry from the configuration file.
 * @version 1.0
 *
 */
public class Entry {

	/**
	 * Name of the entry.
	 */
	public String key;

	/**
	 * Description of the entry (usually provided by ECF parameters dumping)
	 */
	public String desc;

	/**
	 * Value of that entry.
	 */
	public String value;
	
	/**
	 * Constructor.
	 */
	public Entry() {
		this(null, null);
	}
	
	/**
	 * Constructor
	 * @param key Key
	 * @param value Value
	 */
	public Entry(String key, String value) {
		this(key, null, value);
	}

	public Entry(String key, String desc, String value) {
		super();
		this.key = key == null ? "" : key;
		this.desc = desc == null ? "" : desc;
		this.value = value == null ? "" : value;
	}

	/**
	 * Checks if this entry is mandatory.
	 * @return true if it is mandatory, false otherwise
	 */
	public boolean isMandatory() {
		return desc.contains("(mandatory)");
	}
	
	@Override
	public String toString() {
		String str = key + " = " + value;
		if (desc != null && !desc.trim().isEmpty()) {
			str += " (" + desc + ")";
		}
		return str;
	}

	public Entry copy() {
		return new Entry(key, desc, value);
	}
}
