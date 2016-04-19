package hr.fer.zemris.ecf.lab.engine.task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dstankovic on 4/19/16.
 */
public class StatsSupervisor {
  private String originalStatsfile;
  private int statsfileCount;

  private List<List<String>> statsList;

  public StatsSupervisor(String originalStatsfile, int statsfileCount) {
    this.originalStatsfile = originalStatsfile;
    this.statsfileCount = statsfileCount;

    statsList = new LinkedList<>();
  }

  public synchronized void addStats(List<String> stats) {
    statsList.add(stats);

    if (statsList.size() >= statsfileCount) {
      writeStatsToFile();
    }
  }

  private void writeStatsToFile() {
    try {
      FileWriter fw = new FileWriter(originalStatsfile);
      fw.append("runId\tfit_min\tfit_max\tfit_avg\tfit_std\t#evals\ttime\tgen\n");
      for (List<String> stats : statsList) {
        for (String line : stats) {
          fw.append(line + "\n");
        }
      }
      fw.flush();
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
