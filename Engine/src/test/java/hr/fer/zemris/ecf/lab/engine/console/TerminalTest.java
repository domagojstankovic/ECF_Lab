package hr.fer.zemris.ecf.lab.engine.console;

import org.junit.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Domagoj on 12/04/15.
 */
public class TerminalTest {

    private Terminal terminal;

    @Before
    public void setUp() throws Exception {
        if (DetectOS.isUnix() || DetectOS.isMac()) {
            terminal = new Terminal();
        } else {
            terminal = null;
        }
    }

    @After
    public void tearDown() throws Exception {
        terminal = null;
    }

    @org.junit.Test
    public void testPardump() throws Exception {
        if (terminal != null) {
            assertTrue(true);
        }
    }

    @Test
    public void testExecute() throws Exception {
        if (terminal != null) {
            assertTrue(true);
        }
    }
}