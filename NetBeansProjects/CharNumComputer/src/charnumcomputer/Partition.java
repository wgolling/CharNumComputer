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
 * A partition of n is a list of positive integers in ascending order
 * whose sum is n.
 * 
 * A Partition is an immutable object with a private final list of Integers, 
 * and variables noting the sum and the largest element.  
 * It has methods for reading these attributes, as well as for
 * amalgamating Partitions to create new ones.
 * 
 * A Partition memoizes its hashcode at construction for more efficient Map lookups.
 * 
 * @author William Gollinger
 */
public class Partition {
  
  private final List<Integer> numbers;
  private final int           sum;
  private final int           max;
  private final int           hashCode;
  
  
  /*
  Constructors
  */
  
  /**
   * Constructs a Partition whose numbers are from the given list.
   * Throws an IllegalArgumentException if there is a non-positive element.
   * @param numbers 
   */
  public Partition(List<Integer> numbers) {
    this.numbers = new ArrayList<>(numbers);
    Collections.sort(this.numbers);
    if (!this.numbers.isEmpty() && this.numbers.get(0) <= 0)
      throw new IllegalArgumentException();
    hashCode = this.numbers.hashCode();                                      // this.numbers is now fixed, so we can memoize its hashCode to save look-up time in hash tables
    sum = numbers.isEmpty() ? 0 : this.numbers.stream().reduce((a, b) -> a + b).get();
    max = numbers.isEmpty() ? 0 : this.numbers.get(this.numbers.size() - 1);
  }
  /**
   * Constructs a Partition whose numbers are from the given array.
   * Throws an IllegalArgumentException if there is a non-positive element.
   * @param numbers 
   */
  public Partition(Integer[] numbers) {
    this(Arrays.asList(numbers));
  }
  /**
   * Constructs the empty partition.
   */
  public Partition() {
    this(new ArrayList<>());
  }
  
  
  /*
  Utility methods.
  */
  
  @Override
  public String toString() {
    return numbers.toString();
  }
  @Override
  public boolean equals(Object o) {
    return (o instanceof Partition && ((Partition)o).numbers.equals(numbers));
  }
  @Override
  public int hashCode() {
    return hashCode;
  }
  
  
  /*
  Getting methods.
  */
  
  /**
   * Returns a copy of the partitions number list.
   * @return 
   */
  public List<Integer> getNumbers() {
    return new ArrayList<>(numbers);
  }
  /**
   * Returns the sum of the partition.
   * @return 
   */
  public int sum() {
    return sum;
  }
  /**
   * Returns the maximum element of the partition.
   * @return 
   */
  public int max() {
    return max;
  }
  
  
  /*
  Static methods.
  */
  
  /**
   * Produces a partition of p.sum + q.sum 
   * whose elements are those of p combined with q.
   * @param p
   * @param q
   * @return 
   */
  static Partition merge(Partition p, Partition q) {
    // This merge method is Theta(n) for n = p.size() + q.size(), because
    // the Partition constructor is O(n) if input is already sorted
    // (otherwise the worst case is n^2), and
    // shuffling together two sorted lists is O(n).
    if (p == null) return q;
    if (q == null) return p;
    return new Partition(listMerge(p.getNumbers(), q.getNumbers()));
  }
  /**
   * Assumes parameters are sorted, and returns their sorted union.
   * @param p
   * @param q
   * @return 
   */
  private static List<Integer> listMerge(List<Integer> p, List<Integer> q) {
    List<Integer> merged = new ArrayList<>();
    int i = 0;
    int j = 0;
    while (i + j < p.size() + q.size()) {
      if (i == p.size()) {
        merged.addAll(q.subList(j, q.size()));
        return merged;
      }
      if (j == q.size()) {
        merged.addAll(p.subList(i, p.size()));
        return merged;
      }
      if (p.get(i) < q.get(j)) {
        merged.add(p.get(i));
        i++;
      } else {
        merged.add(q.get(j));
        j++;
      }
    }
    return merged;
  }
  
  /**
   * Returns a new partition of s * p.sum
   * whose elements are those of p multiplied s.
   * Throws IllegalArgumentException is s <= 0;
   * @param p
   * @param s
   * @return 
   */
  static Partition scale(Partition p, int s) {
    if (s <= 0) throw new IllegalArgumentException();
    List<Integer> q = new ArrayList<>();
    for (int i = 0; i < p.numbers.size(); i++) {
      q.add(p.numbers.get(i) * s);
    }
    return new Partition(q);
  }
  
}