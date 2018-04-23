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

import lib.*;
import java.util.*;
import java.math.*;
import manifold.*;

/**
 *
 * @author William Gollinger
 */
public class CharNumComputer {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    searchFor12Manifold();
  }
  
  public static void searchFor12Manifold() {    
    Manifold m1 = new CP(6);
    Manifold m2 = new Product(Arrays.asList(new CP(2), new CP(4)));
    Manifold m3 = new Product(Arrays.asList(new CP(2), new CP(2), new CP(2)));   
    
    PartitionComputer pc = new PartitionComputer();
    Manifold.CharNumbers charNums1 = m1.getCharNumbers(pc);
    Manifold.CharNumbers charNums2 = m2.getCharNumbers(pc);
    Manifold.CharNumbers charNums3 = m3.getCharNumbers(pc);
    
    Map<Integer, Set<Partition>> occ = pc.getOccurrences(12);
    Set<Partition> atLeast8 = new HashSet<>();
    for (int i = 8; i < 13; i++) {
      atLeast8.addAll(occ.get(i));
    }
    
    for (int a = -10; a < 11; a++) {
      for (int b = -10; b < 11; b++) {
        Map<Partition, BigInteger> swSums = new HashMap<>();
        for (Partition part : atLeast8) {
          BigInteger num1 = charNums1.get("sw", part);
          BigInteger num2 = charNums2.get("sw", part);
          BigInteger num3 = charNums3.get("sw", part);
          
          BigInteger sum = num1.multiply(BigInteger.valueOf(a))
                              .add(num2.multiply(BigInteger.valueOf(b)))
                              .add(num3.multiply(BigInteger.valueOf(16 - a - b)))
                              .mod(BigInteger.valueOf(2));
          if (sum.equals(BigInteger.ZERO)) continue;
          swSums.put(part, sum);
        }
        if (swSums.isEmpty()) {
          System.out.println(String.format("(%d, %d, %d)", a, b, 16 - a - b));
        }
      }
    }
  }
}
