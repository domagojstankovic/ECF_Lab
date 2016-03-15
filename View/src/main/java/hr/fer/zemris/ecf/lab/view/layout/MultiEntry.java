package hr.fer.zemris.ecf.lab.view.layout;

import java.util.List;

/**
 * Created by dstankovic on 3/1/16.
 */
public class MultiEntry {
  /**
   * Name of the entry.
   */
  public String key;

  /**
   * Description of the entry (usually provided by ECF parameters dumping)
   */
  public String desc;

  /**
   * Entry values.
   */
  public List<String> values;

  public MultiEntry(String key, String desc, List<String> values) {
    this.key = key;
    this.desc = desc;
    this.values = values;
  }
}
