package hr.fer.zemris.ecf.lab.engine.conf.xml;

import hr.fer.zemris.ecf.lab.engine.param.AlgGenRegUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

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
        File file = new File("_test_parameters.xml");
        System.out.println(file.getAbsolutePath());
        AlgGenRegUser agru = reader.readArchive(file);
        
    }
}