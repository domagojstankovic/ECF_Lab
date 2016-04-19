package hr.fer.zemris.ecf.lab.engine.stats;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by dstankovic on 4/19/16.
 */
public class StatsParser {
  public static List<RunStats> parse(String filename) throws FileNotFoundException {
    Scanner sc = new Scanner(new File(filename));
    sc.nextLine(); // skip first line

    List<RunStats> statsList = new LinkedList<>();
    while (sc.hasNextLine()) {
      String line = sc.nextLine().trim();
      if (line.isEmpty()) {
        continue;
      }
      String[] parts = line.split("\\s+");

      RunStats stats = new RunStats();
      stats.setRunId(Integer.parseInt(parts[0]));
      stats.setFitMin(Double.parseDouble(parts[1]));
      stats.setFitMax(Double.parseDouble(parts[2]));
      stats.setFitAvg(Double.parseDouble(parts[3]));
      stats.setFitStd(Double.parseDouble(parts[4]));
      stats.setEvals(Integer.parseInt(parts[5]));
      stats.setTime(Double.parseDouble(parts[6]));
      stats.setGen(Integer.parseInt(parts[7]));
      statsList.add(stats);
    }
    sc.close();

    return statsList;
  }

  public static List<String> extractStatsLines(String filename) throws FileNotFoundException {
    Scanner sc = new Scanner(new File(filename));
    sc.nextLine(); // skip first line
    List<String> list = new LinkedList<>();
    while (sc.hasNextLine()) {
      String line = sc.nextLine().trim();
      if (line.isEmpty()) {
        continue;
      }
      list.add(line);
    }
    sc.close();

    return list;
  }
}
