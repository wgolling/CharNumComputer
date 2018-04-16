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
 * A PatitionComputer produces lists of partitions.  
 * It also produces occurrence tables, mapping integers to 
 * the list of partitions that contain it.
 * @author William Gollinger
 */
public class PartitionComputer {
  List<List<Partition>> partitions;
  List<Map<Integer, Set<Partition>>> occurrences;                            // occurences.get(n).get(i) is the list of Partitions of n containing i 
  int[][] p; 
  
  public PartitionComputer(int n) {
    if (n < 0) {
      throw new IllegalArgumentException();
    }
    partitions  = new ArrayList<>();
    occurrences = new ArrayList<>();
    computePartitions(n);
  }
  public PartitionComputer() {
    this(0);
  }
  
  public List<Partition> getPartitions(int n) {
    if (n >= partitions.size()) computePartitions(n);
    return partitions.get(n);
  }
  public Map<Integer, Set<Partition>> getOccurrences(int n) {
    if (n >= partitions.size()) computePartitions(n);
    
    return occurrences.get(n);
  }
  
  private void computePartitions(int max) {
    if (partitions.isEmpty()) {
      partitions.add(new ArrayList<>());
      partitions.get(0).add(new Partition());
      occurrences.add(new HashMap<>());
    }
    
    // Assuming partitions have been computed up to a point, 
    // extend the computation up to the desired level.
    for (int n = partitions.size(); n < max + 1; n++) {
      List<Partition> rowN = new ArrayList<>();
      Map<Integer, Set<Partition>> occN = new HashMap<>();
      // For each k, itterate over the number of times k can appear in the partition.
      for (int k = 1; k < n + 1; k++) {
        List<Integer> ks = new ArrayList<>();
        for (int i = 1; i * k <= n; i++) {
          ks.add(k);
          // Append ks to every partition of n - (i * k) which uses numbers < k
          // The "i == 0" case was handled when computing with k - 1.
          for (Partition part : partitions.get(n-i*k)) {
            // If the last element is at least k then break.
            if (part.max() >= k) {
              break;
            }
            Partition extention = Partition.merge(part, new Partition(ks));
            rowN.add(extention);
            occN = addOccurrences(occN, extention);
          }
        }
      }
      partitions.add(rowN);
      occurrences.add(occN);
    }
  }
  private Map<Integer, Set<Partition>> addOccurrences(
              Map<Integer, Set<Partition>> table, Partition part) {
    Set<Integer> uniques = new HashSet<>(part.getNumbers());
    for (Integer i : uniques) {
      if (table.get(i) == null) {
        table.put(i, new HashSet<>());
      }
      table.get(i).add(part);
    }
    return table;
  }
  
  public int dynamicCountPartitions(int max) {
    if (max < 0) {
      throw new IllegalArgumentException();
    }
    if (p != null && max < p.length) {
      return p[max][max];
    }
    // partitions of 0
    p = new int[max+1][max+1];
    p[0][0] = 1;
    //
    for (int n = 0; n < max + 1; n++) {
      for (int k = 1; k < n + 1; k++) {
        for (int i = 0; i * k <= n; i++) {
          p[n][k] += p[n-(i*k)][k-1];
        }
      }
      for (int k = n + 1; k < max + 1; k++) {
        p[n][k] = p[n][k-1];
      }
    }
    return p[max][max];    
  }
}
