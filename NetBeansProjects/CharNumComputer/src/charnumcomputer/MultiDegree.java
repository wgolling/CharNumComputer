/*
 * The MIT License
 *
 * Copyright 2018 William Gollinger.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package charnumcomputer;

import java.util.*;
import java.util.stream.*;

/**
 * A MultiDegree represents a tuple of exponents for a fixed number of variables.
 * They are constructed by a static nested Builder class.
 * @author William Gollinger
 */
public class MultiDegree {
  
  private final List<Integer> degrees;
  private final int total;
  private final int hashCode;                                                // hashCode is memoized to save some lookup time
  
  private MultiDegree(List<Integer> degrees) {
    this.degrees = new ArrayList<>(degrees);
    int tTotal = 0;
    for (Integer i : this.degrees) { 
      tTotal += i;
    }
    total        = tTotal;
    hashCode     = this.degrees.hashCode();
  }
  
  @Override
  public String toString() {
    return degrees.toString();
  }
  @Override
  public boolean equals(Object o) {
    return (o instanceof MultiDegree 
            && ((MultiDegree)o).degrees.equals(degrees));
  }
  @Override
  public int hashCode() {
    return hashCode;
  }
  public MultiDegree copy() {
    return new MultiDegree(this.degrees);
  }
  
  
  /*
  Getter functions.
  */
  
  /**
   * Returns the degree of the i-th variable.
   * @param i
   * @return 
   */
  public int get(int i) {
    return degrees.get(i);
  }
  /**
   * Returns the number of variables.
   * @return 
   */
  public int vars() {
    return degrees.size();
  }
  /**
   * Returns the sum of the degrees.
   * @return 
   */
  public int total() {
    return total;
  }
  
  /*
  static functions.
  */
  
  /**
   * Compares MultiDegrees d and e of the same length.  Returns true if 
   * d is strictly less than e entry-wise.
   * @param d
   * @param e
   * @return 
   */
  public static boolean lessThan(MultiDegree d, MultiDegree e) {
    if (d.degrees.size() != e.degrees.size()) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < d.degrees.size(); i++) {
      if (d.degrees.get(i) >= e.degrees.get(i)) {
        return false;
      }
    }
    return true;
  }
  /**
   * Returns a new MultiDegree, the concatenation of d and e.
   * @param d
   * @param e
   * @return 
   */
  public static MultiDegree concat(MultiDegree d, MultiDegree e) {
    List<Integer> f = new ArrayList<>(d.degrees);
    f.addAll(e.degrees);
    return new MultiDegree(f);
  }
  public static MultiDegree pad(MultiDegree d, int l, int r) {
    List<Integer> e = new ArrayList<>();
    IntStream.rangeClosed(1, l).forEach(i -> e.add(0));
    e.addAll(d.degrees);
    IntStream.rangeClosed(1, r).forEach(i -> e.add(0));
    return new MultiDegree(e);
  }
  /**
   * Creates a new MultiDegree whose entries are the entry-wise sum of the inputs.
   * @param d
   * @param e
   * @return 
   */
  public static MultiDegree add(MultiDegree d, MultiDegree e) {
    if (d.degrees.size() != e.degrees.size()) {
      throw new IllegalArgumentException();
    }
    List<Integer> f = new ArrayList<>();
    for (int i = 0; i < d.degrees.size(); i++) {
      f.add(d.get(i) + e.get(i));
    }
    return new MultiDegree(f);
  }
  
  public static MultiDegree raise(MultiDegree d) {
    List<Integer> f = new ArrayList<>();
    for (int i = 0; i < d.degrees.size(); i++) {
      f.add(d.get(i) + 1);
    }
    return new MultiDegree(f);
  }
  
    public static MultiDegree lower(MultiDegree d) {
    List<Integer> f = new ArrayList<>();
    for (int i = 0; i < d.degrees.size(); i++) {
      f.add(d.get(i) - 1);
    }
    return new MultiDegree(f);
  }

  /**
   * MultiDegree's Builder class facilitates easy construction of instances.
   */
  public static class Builder {
    
    List<Integer> tDegrees;
    
    public Builder(int vars) {
      tDegrees = new ArrayList<>();
      setVars(vars);
    }
    public Builder(MultiDegree d) {
      tDegrees = new ArrayList<>(d.degrees);
    }
    public Builder() {
      this(0);
    }
    
    public int vars() {
      return tDegrees.size();
    }
    
    public MultiDegree build() {
      return new MultiDegree(tDegrees);
    }

    /**
     * Sets builder's degrees to a copy of input degrees. 
     * @param degrees
     * @return 
     */
    public Builder set(List<Integer> degrees) {
      this.tDegrees = new ArrayList<>(degrees);
      return this;
    }
    /**
     * Sets the degree of the i-th variable to d.
     * @param i
     * @param d
     * @return 
     */
    public Builder set(int i, Integer d) {
      //TODO throws OutOfBoundsException?
      tDegrees.set(i, d);
      return this;
    }  
    /**
     * Sets all degrees to d.
     * @param d
     * @return 
     */
    public Builder setAll(Integer d) {
      for (int i = 0; i < tDegrees.size(); i++) tDegrees.set(i, d);
      return this;
    }
    
    public Builder increment() {
      for (int i = 0; i < tDegrees.size(); i++) {
        tDegrees.set(i, tDegrees.get(i) + 1);
      }
      return this;
    }
    public Builder increment(int i) {
      if (i < 0 || i >= tDegrees.size()) {
        throw new IllegalArgumentException();
      }
      tDegrees.set(i, tDegrees.get(i) + 1);
      return this;
    }
    /**
     * Sets all exponents to 0.
     * @return 
     */
    public Builder zero() {
      setAll(0);
      return this;
    }
    /**
     * setVars either adds 0s or deletes elements at the tail so that
     * the list of degrees will have size vars.
     * @param vars
     * @return 
     */
    final public Builder setVars(int vars) {
      if (vars < 0) {
        throw new IllegalArgumentException();
      }
      int size = tDegrees.size();
      if (vars < size) {
        tDegrees.subList(vars, size).clear();
      } else {
        for (int i = size; i < vars; i++) {
          tDegrees.add(0);
        }
      }
      return this;
    }    
    /**
     * Flushes the current state.
     * @return 
     */
    public Builder reset() {
      return setVars(0);
    }
  }
  
}
