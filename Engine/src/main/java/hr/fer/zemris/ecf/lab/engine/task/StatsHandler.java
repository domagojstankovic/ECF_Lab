package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.stats.StatsParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by dstankovic on 4/19/16.
 */
public class StatsHandler {
  private String statsfile;
  private StatsSupervisor supervisor;

  public StatsHandler(String statsfile, StatsSupervisor supervisor) {
    this.statsfile = statsfile;
    this.supervisor = supervisor;
  }

  public void finished() {
    try {
      List<String> lines = StatsParser.extractStatsLines(statsfile);
      supervisor.addStats(lines);
      new File(statsfile).delete();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
