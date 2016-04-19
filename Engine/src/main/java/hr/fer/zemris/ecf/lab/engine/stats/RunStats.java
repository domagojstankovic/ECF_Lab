package hr.fer.zemris.ecf.lab.engine.stats;

/**
 * Created by dstankovic on 4/19/16.
 */
public class RunStats {
  private int runId;
  private double fitMin;
  private double fitMax;
  private double fitAvg;
  private double fitStd;
  private int evals;
  private double time;
  private int gen;

  public int getRunId() {
    return runId;
  }

  public double getFitMin() {
    return fitMin;
  }

  public double getFitMax() {
    return fitMax;
  }

  public double getFitAvg() {
    return fitAvg;
  }

  public double getFitStd() {
    return fitStd;
  }

  public int getEvals() {
    return evals;
  }

  public double getTime() {
    return time;
  }

  public int getGen() {
    return gen;
  }

  public void setRunId(int runId) {
    this.runId = runId;
  }

  public void setFitMin(double fitMin) {
    this.fitMin = fitMin;
  }

  public void setFitMax(double fitMax) {
    this.fitMax = fitMax;
  }

  public void setFitAvg(double fitAvg) {
    this.fitAvg = fitAvg;
  }

  public void setFitStd(double fitStd) {
    this.fitStd = fitStd;
  }

  public void setEvals(int evals) {
    this.evals = evals;
  }

  public void setTime(double time) {
    this.time = time;
  }

  public void setGen(int gen) {
    this.gen = gen;
  }
}
