package hr.fer.zemris.ecf.lab.engine.stats;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by dstankovic on 4/19/16.
 */
public class StatsParserTest {

  @Test
  public void testParse() throws Exception {
    List<RunStats> statsList = StatsParser.parse("res/test/stats.txt");

    assertTrue(statsList.size() == 8);
    assertTrue(Math.abs(statsList.get(0).getFitMin() - 5.0) < 1e-9);
    assertTrue(Math.abs(statsList.get(7).getFitMax() - 20.0) < 1e-9);
    assertTrue(Math.abs(statsList.get(6).getFitStd() - 0.430183) < 1e-9);
    assertTrue(statsList.get(7).getGen() == 5000);
  }

  @Test
  public void testExtractLines() throws Exception {
    List<String> list = StatsParser.extractStatsLines("res/test/stats.txt");

    assertTrue(list.size() == 8);
    assertTrue(list.get(7).equals("8\t6\t20\t19.7333\t0.52083\t84637\t1\t5000"));
  }
}