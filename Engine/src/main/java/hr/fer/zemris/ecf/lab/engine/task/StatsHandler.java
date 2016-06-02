package hr.fer.zemris.ecf.lab.engine.task;

import hr.fer.zemris.ecf.lab.engine.console.Job;
import hr.fer.zemris.ecf.lab.engine.stats.StatsParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

  public void finished(Job job) {
    try {
      List<String> lines = StatsParser.extractStatsLines(statsfile);
      List<String> newLines = updateRunId(lines, job);
      supervisor.addStats(newLines);
      new File(statsfile).delete();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static List<String> updateRunId(List<String> lines, Job job) {
    Integer runId = job.getRunId();
    if (runId == null) {
      // no run id, don't modify
      return lines;
    }

    List<String> newLines = new ArrayList<>(lines.size());
    for (String line : lines) {
      newLines.add(updateRunId(line, runId));
    }

    return newLines;
  }

  private static String updateRunId(String line, Integer runId) {
    int firstTabIndex = line.indexOf('\t');
    return runId + line.substring(firstTabIndex);
  }
}
