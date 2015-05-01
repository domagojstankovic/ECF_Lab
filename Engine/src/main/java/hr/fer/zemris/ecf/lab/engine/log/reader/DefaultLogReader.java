package hr.fer.zemris.ecf.lab.engine.log.reader;

import hr.fer.zemris.ecf.lab.engine.log.*;

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
                sb.append("\n");
            }
        }
        return new LogModel(sb.toString());
    }

    private void generationState(LogModel log, String line, Scanner sc) {
        String genId = extractValue(line);
        Generation generation = new Generation(Integer.parseInt(genId));

        while (sc.hasNextLine()) {
            String ll = sc.nextLine();
            if (ll.startsWith("Elapsed time:")) {
                generation.elapsedTime = Integer.parseInt(extractValue(ll));
            } else if (ll.startsWith("Deme:")) {
                Deme deme = readDeme(line, sc);
                generation.demes.add(deme);
            } else if (ll.startsWith("Population:")) {
                Population population = new Population();
                EvalsStats es = readEvalsStats(sc);
                population.evaluations = es.evaluations;
                population.stats = es.stats;
                generation.population = population;
            } else if (ll.startsWith(GENERATION_PREFIX)) {
                log.getGenerations().add(generation);
                generationState(log, ll, sc);
                break;
            } else if (ll.startsWith("Best of run:")) {
                log.getGenerations().add(generation);
                hallOfFameState(log, sc);
            }
        }
    }

    private void hallOfFameState(LogModel log, Scanner sc) {
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine());
            sb.append("\n");
        }
        log.setHallOfFame(sb.toString());
    }

    private Deme readDeme(String line, Scanner sc) {
        Deme deme = new Deme();
        deme.id = Integer.parseInt(extractValue(line));
        EvalsStats es = readEvalsStats(sc);
        deme.evaluations = es.evaluations;
        deme.stats = es.stats;
        return deme;
    }

    private EvalsStats readEvalsStats(Scanner sc) {
        EvalsStats es = new EvalsStats();
        while (sc.hasNextLine()) {
            String ll = sc.nextLine().trim();
            if (ll.isEmpty()) {
                break;
            }
            if (ll.startsWith("Evaluations:")) {
                es.evaluations = Integer.parseInt(extractValue(ll));
            } else if (ll.startsWith("Stats:")) {
                es.stats = readStats(sc);
                break;
            }
        }
        return es;
    }

    private Stats readStats(Scanner sc) {
        Stats stats = new Stats();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }
            if (line.startsWith("max:")) {
                stats.max = Double.parseDouble(extractValue(line));
            } else if (line.startsWith("min:")) {
                stats.min = Double.parseDouble(extractValue(line));
            } else if (line.startsWith("avg:")) {
                stats.avg = Double.parseDouble(extractValue(line));
            } else if (line.startsWith("stdev:")) {
                stats.stdev = Double.parseDouble(extractValue(line));
            }
        }
        return stats;
    }

    private static String extractValue(String str) {
        return str.split(":")[1].trim();
    }

    private static class EvalsStats {
        int evaluations;
        Stats stats;
    }
}
