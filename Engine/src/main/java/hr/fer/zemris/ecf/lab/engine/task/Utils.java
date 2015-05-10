package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.param.Entry;

import java.util.List;

/**
 * Collection of static methods that are often used.
 * @author Domagoj
 *
 */
public class Utils {

	/**
	 * If string has extension then infix will be inserted before extension, otherwise it will be appended to the end.
	 * @param str Original string
	 * @param infix String to be inserted (or appended)
	 * @return String with infix on the right place
	 */
	public static String addBeforeExtension(String str, String infix) {
		int dotIndex = str.lastIndexOf(".");
		int separatorIndex = Math.max(str.lastIndexOf("\\"), str.lastIndexOf("/"));
		if (separatorIndex > dotIndex) {
			return str + infix;
		}
		return str.substring(0, dotIndex) + infix + str.substring(dotIndex);
	}

	/**
	 * Adds number of fixed length with '_' prefix before extension.
	 * e.g. (log.txt, 16, 5) -> log_00016.txt
	 * @param str String with file name
	 * @param num Number to be inserted
	 * @param len Length of max number
	 * @return String with added number of fixed length
	 */
	public static String addBeforeExtension(String str, int num, int len) {
		StringBuilder sb = new StringBuilder("_");
		int n = len - String.valueOf(num).length();
		for (int i = 0; i < n; i++) {
			sb.append('0');
		}
		sb.append(num);
		return addBeforeExtension(str, sb.toString());
	}

	/**
	 * Finds entry in list of entries with given key
	 * @param list List of entries
	 * @param key Key
	 * @return Entry with given key, null if key does not exist in the list
	 */
	public static Entry findEntry(List<Entry> list, String key) {
		for (Entry e : list) {
			if (e.key.equals(key)) {
				return e;
			}
		}
		return null;
	}

}
