package hr.fer.zemris.ecf.lab.engine.log.reader;

import hr.fer.zemris.ecf.lab.engine.log.LogModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Domagoj on 01/05/15.
 */
public class DefaultLogReader implements LogReader {
    @Override
    public LogModel read(InputStream is) {
        Scanner sc = new Scanner(is);
        LogModel log = startState(sc);
        sc.close();
        return log;
    }

    private static final String ERROR_PREFIX = "Error:";
    private static final String GENERATION_PREFIX = "Generation:";

    private LogModel startState(Scanner sc) {
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith(ERROR_PREFIX)) {
                return errorState(line, sc);
            }
            if (line.startsWith(GENERATION_PREFIX)) {
                LogModel log = new LogModel(new ArrayList<>());
                generationState(log, line, sc);
                return log;
            }
        }
        return new LogModel("Empty file");
    }

    private LogModel errorState(String line, Scanner sc) {
        StringBuilder sb = new StringBuilder(line);
        while (sc.hasNextLine()) {
            String ll = sc.nextLine();
            if (ll.startsWith(ERROR_PREFIX)) {
                sb.append(ll);
            }
        }
        return new LogModel(sb.toString());
    }

    private void generationState(LogModel log, String line, Scanner sc) {

    }
}
