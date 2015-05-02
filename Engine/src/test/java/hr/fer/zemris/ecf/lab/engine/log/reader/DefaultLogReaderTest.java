package hr.fer.zemris.ecf.lab.engine.log.reader;

import hr.fer.zemris.ecf.lab.engine.log.Generation;
import hr.fer.zemris.ecf.lab.engine.log.LogModel;
import org.junit.*;

import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * Created by Domagoj on 02/05/15.
 */
public class DefaultLogReaderTest {

    @org.junit.Test
    public void testRead() throws Exception {
        DefaultLogReader reader = new DefaultLogReader();
        LogModel log = reader.read(new FileInputStream("res/test/log1.txt"));
        assertTrue("Hall of fame error", log.getHallOfFame().equals("<HallOfFame size=\"1\">\n" +
                "\t<Individual size=\"1\" gen=\"0\">\n" +
                "\t\t<FitnessMax value=\"10\"/>\n" +
                "\t\t<BitString size=\"10\">1111111111</BitString>\n" +
                "\t</Individual>\n" +
                "</HallOfFame>\n\n"));

        assertTrue("Generations num error", log.getGenerations().size() == 31);
        Generation gen0 = log.getGenerations().get(0);
        assertTrue("Generation id bug", gen0.id == 0);
        assertTrue("Demes size bug", gen0.demes.size() == 5);
        assertTrue("Elapsed time bug", gen0.elapsedTime == 0);
        assertTrue("5th deme min error", Math.abs(gen0.demes.get(4).stats.min - 2) < 1e-9);
        Generation gen30 = log.getGenerations().get(30);
        assertTrue("Generation 30 id", gen30.id == 30);
        assertTrue("Generation 30 evaluations error", gen30.population.evaluations == 2733);
        assertTrue("Generation 30 population max error", Math.abs(gen30.population.stats.max - 10) < 1e-9);
    }
}