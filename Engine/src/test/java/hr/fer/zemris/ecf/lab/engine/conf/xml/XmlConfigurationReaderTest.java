package hr.fer.zemris.ecf.lab.engine.conf.xml;

import hr.fer.zemris.ecf.lab.engine.param.Configuration;
import hr.fer.zemris.ecf.lab.engine.param.Entry;
import hr.fer.zemris.ecf.lab.engine.param.EntryBlock;
import hr.fer.zemris.ecf.lab.engine.param.EntryList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

/**
 * Created by Domagoj on 12/04/15.
 */
public class XmlConfigurationReaderTest {

    private XmlConfigurationReader reader = null;

    @Before
    public void setUp() throws Exception {
        reader = new XmlConfigurationReader();
    }

    @After
    public void tearDown() throws Exception {
        reader = null;
    }

    @Test
    public void testReadInitial() throws Exception {

    }

    @Test
    public void testReadArchive() throws Exception {
        File file = new File("res/_test_parameters.xml");
        System.out.println(file.getAbsolutePath());
        Configuration conf = reader.readArchive(file);

        List<EntryBlock> algs = conf.algorithms;
        assertTrue(algs.size() == 1);

        EntryBlock alg1 = algs.get(0);
        assertTrue(alg1.getName().equals("RouletteWheel"));
        assertEntryBlock(alg1, "crxprob", "0.5", "selpressure", "10");

        assertTrue(conf.genotypes.size() == 1);
        List<EntryBlock> genList = conf.genotypes.get(0);

        assertTrue(genList.size() == 1);
        EntryBlock gen1 = genList.get(0);

        assertTrue(gen1.getName().equals("BitString"));
        assertEntryBlock(gen1, "size", "20");


    }

    private void assertEntryBlock(EntryList block, String... keysValues) {
        int size = keysValues.length / 2;
        for (int i = 0; i < size; i++) {
            Entry entry = block.getEntryAt(i);
            assertTrue(entry.key.equals(keysValues[i * 2]));
            assertTrue(entry.value.equals(keysValues[i * 2 + 1]));
        }
    }
}