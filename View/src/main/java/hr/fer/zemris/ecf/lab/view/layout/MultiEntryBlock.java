package hr.fer.zemris.ecf.lab.view.layout;

import java.util.List;

/**
 * Created by dstankovic on 3/1/16.
 */
public class MultiEntryBlock {
  private String name;
  private List<MultiEntry> entries;

  public MultiEntryBlock(String name, List<MultiEntry> entries) {
    this.name = name;
    this.entries = entries;
  }

  public String getName() {
    return name;
  }

  public List<MultiEntry> getEntries() {
    return entries;
  }
}
