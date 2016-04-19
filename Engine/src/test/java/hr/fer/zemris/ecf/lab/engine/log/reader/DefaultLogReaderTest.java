package hr.fer.zemris.ecf.lab.engine.log.reader;

import hr.fer.zemris.ecf.lab.engine.log.ExperimentRun;
import hr.fer.zemris.ecf.lab.engine.log.Generation;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Domagoj on 02/05/15.
 */
public class DefaultLogReaderTest {

  @org.junit.Test
  public void testReadMoreDemes() throws Exception {
    DefaultLogReader reader = new DefaultLogReader();
    LogModel log = reader.read(new FileInputStream("res/test/log1.txt"));
    String hofString = "<HallOfFame size=\"1\">\n" +
        "\t<Individual size=\"1\" gen=\"0\">\n" +
        "\t\t<FitnessMax value=\"10\"/>\n" +
        "\t\t<BitString size=\"10\">1111111111</BitString>\n" +
        "\t</Individual>\n" +
        "</HallOfFame>";
    assertTrue("Hall of fame error", log.getRuns().get(0).getHallOfFame().equals(hofString));

    assertTrue("Generations num error", log.getRuns().get(0).getGenerations().size() == 31);
    Generation gen0 = log.getRuns().get(0).getGenerations().get(0);
    assertTrue("Generation id bug", gen0.id == 0);
    assertTrue("Demes size bug", gen0.demes.size() == 5);
    assertTrue("Elapsed time bug", gen0.elapsedTime == 0);
    assertTrue("5th deme min error", Math.abs(gen0.demes.get(4).stats.min - 2) < 1e-9);

    Generation gen30 = log.getRuns().get(0).getGenerations().get(30);
    assertTrue("Generation 30 id", gen30.id == 30);
    assertTrue("Generation 30 evaluations error", gen30.population.evaluations == 2733);
    assertTrue("Generation 30 population max error", Math.abs(gen30.population.stats.max - 10) < 1e-9);
  }

  @Test
  public void testReadOneDeme() throws Exception {
    DefaultLogReader reader = new DefaultLogReader();
    LogModel log = reader.read(new FileInputStream("res/test/log2.txt"));
    ExperimentRun run = log.getRuns().get(9);
    String hofString = "<HallOfFame size=\"1\">\n" +
        "\t<Individual size=\"1\" gen=\"14\">\n" +
        "\t\t<FitnessMax value=\"20\"/>\n" +
        "\t\t<BitString size=\"20\">11111111111111111111</BitString>\n" +
        "\t</Individual>\n" +
        "</HallOfFame>";
    assertTrue("Hall of fame error", run.getHallOfFame().equals(hofString));

    assertTrue("Generations num error", run.getGenerations().size() == 51);
    Generation gen0 = run.getGenerations().get(0);
    assertTrue("Generation id bug", gen0.id == 0);
    assertTrue("Demes size bug", gen0.demes.size() == 1);
    assertTrue("Elapsed time bug", gen0.elapsedTime == 0);
    assertTrue("Deme min error", Math.abs(gen0.demes.get(0).stats.min - 5) < 1e-9);
    assertTrue("Deme stdev error", Math.abs(gen0.demes.get(0).stats.stdev - 2.14449) < 1e-9);

    Generation gen50 = run.getGenerations().get(50);
    assertTrue("Generation 50 id bug", gen50.id == 50);
    assertTrue("Generation 50 population exists", gen50.population == null);
    assertTrue("Generation 50 deme avg error", Math.abs(gen50.demes.get(0).stats.avg - 19.8333) < 1e-9);
  }

  @Test
  public void testNanAndInf() throws Exception {
    DefaultLogReader reader = new DefaultLogReader();
    LogModel log = reader.read(new FileInputStream("res/test/log3.txt"));
    assertTrue(Double.isNaN(log.getRuns().get(0).getGenerations().get(0).demes.get(3).stats.avg));
    assertTrue(log.getRuns().get(0).getGenerations().get(0).demes.get(4).stats.stdev == Double.POSITIVE_INFINITY);
    assertTrue(log.getRuns().get(0).getGenerations().get(0).population.stats.max == Double.NEGATIVE_INFINITY);
    assertTrue(log.getRuns().get(0).getGenerations().get(0).population.stats.min == Double.NEGATIVE_INFINITY);
  }

  @Test
  public void testGenerationHof() throws Exception {
    DefaultLogReader reader = new DefaultLogReader();
    LogModel log = reader.read(new FileInputStream("res/test/log_with_generation_hof.txt"));

    // GT stands for Ground Truth
    String hofGen0GT = "<Individual size=\"1\">\n" +
        "\t<FitnessMin value=\"0.00986234\"/>\n" +
        "\t<Tree size=\"5\">- - 1 3 2 </Tree>\n" +
        "</Individual>";
    String hofGen0 = log.getRuns().get(0).getGenerations().get(0).hallOfFame;
    assertTrue(hofGen0.equals(hofGen0GT));

    String hofGen10GT = "<Individual size=\"1\">\n" +
        "\t<FitnessMin value=\"0.00929832\"/>\n" +
        "\t<Tree size=\"10\">- - sin / / 1 x1 1 1 3 </Tree>\n" +
        "</Individual>";
    String hofGen10 = log.getRuns().get(0).getGenerations().get(1).hallOfFame;
    assertTrue(hofGen10.equals(hofGen10GT));

    assertTrue(Math.abs(log.getRuns().get(0).getGenerations().get(1).demes.get(0).stats.max - 151429) < 10e-9);
  }

  @Test
  public void testPartialLogWithGenerationHof() throws Exception {
    DefaultLogReader reader = new DefaultLogReader();
    LogModel log = reader.read(new FileInputStream("res/test/log_with_generation_hof_partial.txt"));

    assertTrue(log.getRuns().get(0).getGenerations().size() == 1);
  }

  @Test
  public void testOtherLines() throws Exception {
    DefaultLogReader reader = new DefaultLogReader();
    LogModel log = reader.read(new FileInputStream("res/test/log_srm.txt"));

    List<String> otherLines = log.getRuns().get(0).getOtherLines();
    assertTrue(otherLines.get(0).equals("Termination: fitness value (1e-07) reached"));
    assertTrue(otherLines.get(otherLines.size() - 1).equals("Linear scaling parameters: scale=-1 offset=-4.41615"));
  }
}