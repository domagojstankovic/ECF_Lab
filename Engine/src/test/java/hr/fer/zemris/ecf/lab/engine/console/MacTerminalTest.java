package hr.fer.zemris.ecf.lab.engine.console;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertFalse;

/**
 * Created by Domagoj on 12/04/15.
 */
public class MacTerminalTest {

    private boolean isMac = false;

    @Before
    public void setUp() throws Exception {
        if (DetectOS.isMac()) {
            isMac = true;
        } else {
            isMac = false;
        }
    }

    @Test
    public void testBasic() throws Exception {
        if (isMac) {
            String prefix = new File("").getAbsolutePath();
            String prog = prefix + "/res/test/onemax";
            String args = prefix + "/res/test/parameters2.xml";

            ProcessBuilder pb = new ProcessBuilder(prog, args);
            Process process = pb.start();
            InputStream is = process.getInputStream();
            InputStream eis = process.getErrorStream();
            process.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            BufferedReader eReader = new BufferedReader(new InputStreamReader(eis));

            String line = reader.readLine();
            if (line == null) {
                assertFalse("Nothing is printed out", true);
            }
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }

            System.out.println("--------------------********************************--------------------");
            System.out.println("--------------------********************************--------------------");

            line = eReader.readLine();
            while (line != null) {
                System.err.println(line);
                line = eReader.readLine();
            }
        }
    }
}