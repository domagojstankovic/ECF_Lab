package hr.fer.zemris.ecf.lab.engine.log;

import java.util.List;

/**
 * Created by Domagoj on 03/06/15.
 */
public class ExperimentRun {

  private List<Generation> generations;
  private String hallOfFame;
  private List<String> otherLines;

  public ExperimentRun(List<Generation> generations) {
    this.generations = generations;
  }

  public List<Generation> getGenerations() {
    return generations;
  }

  public String getHallOfFame() {
    return hallOfFame;
  }

  public void setHallOfFame(String hallOfFame) {
    this.hallOfFame = hallOfFame;
  }

  public List<String> getOtherLines() {
    return otherLines;
  }

  public void setOtherLines(List<String> otherLines) {
    this.otherLines = otherLines;
  }
}
