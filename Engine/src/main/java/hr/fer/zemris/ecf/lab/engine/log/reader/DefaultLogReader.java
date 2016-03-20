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
        return new Reader().read(is);
    }

    /**
     * Not thread safe. New instance is created for each reading.
     */
    private static class Reader implements LogReader {

        private static final String ERROR_PREFIX = "Error:";
        private static final String GENERATION_PREFIX = "Generation:";
        private static final String HALL_OF_FAME_END = "</HallOfFame>";
        private static final String POPULATION_PREFIX = "Population:";
        private static final String DEME_PREFIX = "Deme:";
        private static final String ELAPSED_TIME_PREFIX = "Elapsed time:";
        private static final String BEST_OF_RUN_PREFIX = "Best of run:";
        private static final String EVALUATIONS_PREFIX = "Evaluations:";
        private static final String STATS_PREFIX = "Stats:";

        private static final String MAX_PREFIX = "max:";
        private static final String MIN_PREFIX = "min:";
        private static final String AVG_PREFIX = "avg:";
        private static final String STDEV_PREFIX = "stdev:";

        private Scanner sc;
        private String line;

        @Override
        public LogModel read(InputStream is) {
            sc = new Scanner(is);
            LogModel log = startState();
            sc.close();
            return log;
        }

        private String nextLine() {
            if (line == null) {
                line = sc.nextLine();
            }
            return line;
        }

        private String popNextLine() {
            String line = nextLine();
            this.line = null;
            return line;
        }

        private boolean hasNextLine() {
            return line != null || sc.hasNextLine();
        }

        private void invalidateLine() {
            line = null;
        }

        private LogModel startState() {
            ArrayList<ExperimentRun> runs = new ArrayList<>();
            while (hasNextLine()) {
                String line = nextLine();
                if (line.startsWith(ERROR_PREFIX)) {
                    return errorState();
                } else if (line.startsWith(GENERATION_PREFIX)) {
                    ExperimentRun run = generationState();
                    runs.add(run);
                } else {
                    invalidateLine();
                }
            }
            if (runs.isEmpty()) {
                return new LogModel("Empty file");
            } else {
                return new LogModel(runs);
            }
        }

        private LogModel errorState() {
            StringBuilder sb = new StringBuilder(popNextLine());
            while (hasNextLine()) {
                String ll = popNextLine();
                if (ll.startsWith(ERROR_PREFIX)) {
                    sb.append(ll);
                    sb.append("\n");
                }
            }
            return new LogModel(sb.toString());
        }

        private ExperimentRun generationState() {
            ExperimentRun run = new ExperimentRun(new ArrayList<>());
            Generation generation = new Generation(Integer.parseInt(extractValue(popNextLine())));

            while (hasNextLine()) {
                String ll = nextLine();
                if (ll.startsWith(ELAPSED_TIME_PREFIX)) {
                    generation.elapsedTime = Integer.parseInt(extractValue(ll));
                    invalidateLine();
                } else if (ll.startsWith(DEME_PREFIX)) {
                    Deme deme = readDeme();
                    generation.demes.add(deme);
                } else if (ll.startsWith(POPULATION_PREFIX)) {
                    Population population = new Population();
                    invalidateLine();
                    EvalsStats es = readEvalsStats();
                    population.evaluations = es.evaluations;
                    population.stats = es.stats;
                    generation.population = population;
                } else if (ll.startsWith(GENERATION_PREFIX)) {
                    run.getGenerations().add(generation);
                    generation = new Generation(Integer.parseInt(extractValue(popNextLine())));
                } else if (ll.startsWith(BEST_OF_RUN_PREFIX)) {
                    run.getGenerations().add(generation);
                    run.setHallOfFame(hallOfFameState());
                    break;
                } else {
                    invalidateLine();
                }
            }

            return run;
        }

        private String hallOfFameState() {
            invalidateLine();
            StringBuilder sb = new StringBuilder();
            while (hasNextLine()) {
                String line = popNextLine();
                sb.append(line);
                sb.append("\n");
                if (line.trim().equals(HALL_OF_FAME_END)) {
                    break;
                }
            }
            return sb.toString();
        }

        private Deme readDeme() {
            Deme deme = new Deme();
            deme.id = Integer.parseInt(extractValue(popNextLine()));
            EvalsStats es = readEvalsStats();
            deme.evaluations = es.evaluations;
            deme.stats = es.stats;
            return deme;
        }

        private EvalsStats readEvalsStats() {
            EvalsStats es = new EvalsStats();
            while (hasNextLine()) {
                String ll = nextLine().trim();
                if (ll.isEmpty()) {
                    invalidateLine();
                    break;
                }
                if (ll.startsWith(EVALUATIONS_PREFIX)) {
                    es.evaluations = Integer.parseInt(extractValue(ll));
                    invalidateLine();
                } else if (ll.startsWith(STATS_PREFIX)) {
                    es.stats = readStats();
                    break;
                } else {
                    break;
                }
            }
            return es;
        }

        private Stats readStats() {
            invalidateLine();
            Stats stats = new Stats();
            while (hasNextLine()) {
                String line = nextLine().trim();
                if (line.isEmpty()) {
                    invalidateLine();
                    break;
                }
                String strVal = extractValue(line);
                if (line.startsWith(MAX_PREFIX)) {
                    try {
                        stats.max = Double.parseDouble(strVal);
                    } catch (NumberFormatException e) {
                        stats.max = extractNanOrInfinity(strVal);
                    } finally {
                        invalidateLine();
                    }
                } else if (line.startsWith(MIN_PREFIX)) {
                    try {
                        stats.min = Double.parseDouble(strVal);
                    } catch (NumberFormatException e) {
                        stats.min = extractNanOrInfinity(strVal);
                    } finally {
                        invalidateLine();
                    }
                } else if (line.startsWith(AVG_PREFIX)) {
                    try {
                        stats.avg = Double.parseDouble(strVal);
                    } catch (NumberFormatException e) {
                        stats.avg = extractNanOrInfinity(strVal);
                    } finally {
                        invalidateLine();
                    }
                } else if (line.startsWith(STDEV_PREFIX)) {
                    try {
                        stats.stdev = Double.parseDouble(strVal);
                    } catch (NumberFormatException e) {
                        stats.stdev = extractNanOrInfinity(strVal);
                    } finally {
                        invalidateLine();
                    }
                } else {
                    break;
                }
            }
            return stats;
        }

        private static double extractNanOrInfinity(String strVal) {
            String lowStr = strVal.toLowerCase();
            if ("nan".equals(lowStr)) {
                return Double.NaN;
            } else if ("inf".equals(lowStr)) {
                return Double.POSITIVE_INFINITY;
            } else if ("-inf".equals(lowStr) || "- inf".equals(lowStr)) {
                return Double.NEGATIVE_INFINITY;
            } else {
                throw new NumberFormatException();
            }
        }

        private static String extractValue(String str) {
            return str.substring(str.indexOf(':') + 1).trim();
        }

        private static class EvalsStats {
            int evaluations;
            Stats stats;
        }
    }
}
