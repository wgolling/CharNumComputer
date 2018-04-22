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

/**
 * A MultiDegree represents a tuple of exponents for a fixed number of variables.
 * They are constructed by a static nested Builder class.
 * @author William Gollinger
 */
public class MultiDegree {
  
  private final List<Integer> degrees;
  private final int total;                                                   // total is the sum of the degrees
  private final int hashCode;                                                // hashCode is memoized to save a bit of lookup time
  
  /**
   * Produces a MultiDegree with the given degrees.
   * Constructor is private so they can only be made by a Builder 
   * or with static methods.
   * @param degrees 
   */
  private MultiDegree(List<Integer> degrees) {
    this.degrees = new ArrayList<>(degrees);
    total        = this.degrees.stream().mapToInt(Integer::intValue).sum();
    hashCode     = this.degrees.hashCode();
  }
  
  /*
  Utility methods.
  */
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
  Getter methods.
  */
  
  /**
   * Returns the degree of the i-th variable.
   * @param i
   * @return 
   */
  public int get(int i) {
    if (i < 0 || i > degrees.size()) {
      throw new IllegalArgumentException();
    }
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
  Comparison methods.
  */
 
  /**
   * Compares MultiDegrees d and e of the same length.  Returns true if 
   * d is strictly less than e entry-wise.
   * @param trunc
   * @return 
   */
  public boolean exceeds(MultiDegree trunc) {
    if (this.degrees.size() != trunc.degrees.size()) throw new IllegalArgumentException();
    for (int i = 0; i < this.degrees.size(); i++) 
      if (this.degrees.get(i) > trunc.degrees.get(i)) return true;
    return false;
  }
  /**
   * MultiDegree d1 divides MultiDegree d2 iff
   * they are the same size and d1.get(i) divides d2.get(i)for all i.
   * @param d
   * @return 
   */
  public boolean divides(MultiDegree d) {
    if (this.degrees.size() != d.degrees.size()) throw new IllegalArgumentException();
    int remainder = 0;
    for (int i = 0; i < degrees.size(); i++) 
      remainder += Math.abs(d.degrees.get(i) % degrees.get(i));              // In case the remainder is negative, take the absolute value.
    return (remainder == 0);
  }
  
  
  /*
  static functions.
  */
  
  /**
   * Returns a MultiDegree with n variables where each degree is 0.
   * @param n
   * @return 
   */
  public static MultiDegree zeros(int n) {
    if (n < 0) throw new IllegalArgumentException();
    List<Integer> zeros = new ArrayList<>();
    for (int i = 0; i < n; i++) 
      zeros.add(0);
    return new MultiDegree(zeros);
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
  
  /**
   * Adds l 0's to the left of d and r 0's to the right.
   * @param d
   * @param l
   * @param r
   * @return 
   */
  public static MultiDegree pad(MultiDegree d, int l, int r) {
    if (l < 0 || r < 0) {
      throw new IllegalArgumentException();
    }
    return concat(concat(zeros(l), d), zeros(r));
  }
  
  /**
   * Creates a new MultiDegree whose entries are the entry-wise sum of the inputs.
   * @param d
   * @param e
   * @return 
   */
  public static MultiDegree add(MultiDegree d, MultiDegree e) {
    if (d.degrees.size() != e.degrees.size()) throw new IllegalArgumentException();
    List<Integer> f = new ArrayList<>();
    for (int i = 0; i < d.degrees.size(); i++) 
      f.add(d.get(i) + e.get(i));
    return new MultiDegree(f);
  }
  
  /**
   * Returns a new MultiDegree whose degrees are 1 larger than those of d.
   * @param d
   * @return 
   */
  public static MultiDegree raise(MultiDegree d) {
    List<Integer> f = new ArrayList<>();
    for (int i = 0; i < d.degrees.size(); i++) {
      f.add(d.get(i) + 1);
    }
    return new MultiDegree(f);
  }
  /**
   * Returns a new MultiDegree whose degrees are 1 lower than those of d.
   * @param d
   * @return 
   */
  public static MultiDegree lower(MultiDegree d) {
    List<Integer> f = new ArrayList<>();
    for (int i = 0; i < d.degrees.size(); i++) {
      f.add(d.get(i) - 1);
    }
    return new MultiDegree(f);
  }

  
  /*
  nested class MultiDegree.Builder
  */
  
  /**
   * MultiDegree's Builder class facilitates easy construction of instances.
   */
  public static class Builder {
    
    List<Integer> tDegrees;
    
    /**
     * Constructs a MultiDegree.Builder with vars variables.
     * @param vars 
     */
    public Builder(int vars) {
      tDegrees = new ArrayList<>();
      setVars(vars);
    }
    /**
     * Constructs a MultiDegree.Builder set to d.
     * @param d 
     */
    public Builder(MultiDegree d) {
      tDegrees = new ArrayList<>(d.degrees);
    }
    /**
     * Constructs an empty MultiDegree.Builder.
     */
    public Builder() {
      this(0);
    }
    
    /**
     * Returns the current number of variables.
     * @return 
     */
    public int vars() {
      return tDegrees.size();
    }
    
    /**
     * Returns a MultiDegree with the current setting.
     * @return 
     */
    public MultiDegree build() {
      return new MultiDegree(tDegrees);
    }

    
    /*
    Setting methods.
    */
    
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
     * Throws IllegalArgumentException if i is out of bounds.
     * @param i
     * @param d
     * @return 
     */
    public Builder set(int i, Integer d) {
      if (i < 0 || i > tDegrees.size()) throw new IllegalArgumentException();
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
    
    /**
     * Increases all entries by 1.
     * @return 
     */
    public Builder increment() {
      for (int i = 0; i < tDegrees.size(); i++) 
        tDegrees.set(i, tDegrees.get(i) + 1);
      return this;
    }
    
    /**
     * Increases entry i by 1.
     * Throws IllegalArgumentException if i is out of bounds.
     * @param i
     * @return 
     */
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
      if (vars < 0) throw new IllegalArgumentException();
      int size = tDegrees.size();
      if (vars < size) 
        tDegrees.subList(vars, size).clear();
      else {
        for (int i = size; i < vars; i++) 
          tDegrees.add(0);
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
