package hr.fer.zemris.ecf.lab.engine.log.reader;

import hr.fer.zemris.ecf.lab.engine.log.ExperimentRun;
import hr.fer.zemris.ecf.lab.engine.log.Generation;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import org.junit.Test;

import java.io.FileInputStream;

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
                "</HallOfFame>\n";
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
                "</HallOfFame>\n";
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
}