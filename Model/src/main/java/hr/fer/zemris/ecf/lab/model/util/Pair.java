package hr.fer.zemris.ecf.lab.model.util;

/**
 * Created by dstankovic on 3/15/16.
 */
public class Pair<F, S> {

  private final F first;
  private final S second;

  public Pair(F first, S second) {
    this.first = first;
    this.second = second;
  }

  public F getFirst() { return first; }
  public S getSecond() { return second; }

  @Override
  public int hashCode() {
    return first.hashCode() ^ second.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pair)) return false;
    Pair pairo = (Pair) o;
    return this.first.equals(pairo.getFirst()) &&
        this.second.equals(pairo.getSecond());
  }

  public Pair<F, S> shallowCopy() {
    return new Pair<>(first, second);
  }
}
