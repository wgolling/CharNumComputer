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
import java.math.*;
import manifold.*;

/**
 *
 * @author William Gollinger
 */
public class CharNumComputer {

  PartitionComputer pc;
  
  public CharNumComputer() {
    pc = new PartitionComputer();
  }
  
  public Map<String, Map<Partition, BigInteger>> computeCharNumbers(Manifold m) {
    Map<String, Map<Partition, BigInteger>> charNumbers = new HashMap<>();
    Map<String, Polynomial> charClasses = m.getCharClasses();
    
    MultiDegree mu = m.truncation();
    Polynomial.Ring pr = new Polynomial.Ring(mu.vars());
    
    // In all cases we have Stiefel-Whitney numbers.
    charNumbers.put("sw", new HashMap<>());
    Map<Partition, BigInteger> swNumbers = charNumbers.get("sw");

    /*
    If m is complex, we can compute the Chern numbers first 
    and use their mod 2 reductions for Steifel-Whitney numbers.
    */
    if (m.isComplex()) {
      charNumbers.put("chern", new HashMap<>());
      Map<Partition, BigInteger> chernNumbers = charNumbers.get("chern");
      // compute chern numbers and take mod 2 for sw 
      Polynomial chernClass = charClasses.get("chern");
      List<Polynomial> chernList = new ArrayList<>();
      for (int i = 0; i < m.cDim() + 1; i++) {
        chernList.add(chernClass.getHomogeneousPart(2 * i));
      }
      List<Partition> chernParts = pc.getPartitions(m.cDim());
      for (Partition part : chernParts) {
        BigInteger n = makeNumber(chernList, pr, part, mu);
        chernNumbers.put(part, n);
        swNumbers.put(Partition.scale(part, 2), n);
      }
    } else {
      charNumbers.put("sw", new HashMap<>());
      // compute sw
      Polynomial swClass = charClasses.get("sw");
      List<Polynomial> swList = new ArrayList<>();
      for (int i = 0; i < m.rDim() + 1; i++) {
        swList.add(swClass.getHomogeneousPart(i));
      }
      List<Partition> swParts = pc.getPartitions(m.rDim());
      Polynomial.Ring mod2 = new Polynomial.Ring(mu.vars());
      mod2.setModulus(2);
      for (Partition part : swParts) {
        BigInteger n = makeNumber(swList, mod2, part, mu);
        swNumbers.put(part, n);
      }
    }
    if (m.rDim() % 4 == 0) {
      int qDim = m.rDim() / 4;
      charNumbers.put("pont", new HashMap<>());
      Map<Partition, BigInteger> pontNumbers = charNumbers.get("pont");
      // compute pont
      Polynomial pontClass = charClasses.get("pont");
      List<Polynomial> pontList = new ArrayList<>();
      for (int i = 0; i < qDim + 1; i++) {
        pontList.add(pontClass.getHomogeneousPart(4 * i));
      }
      List<Partition> pontParts = pc.getPartitions(qDim);
      for (Partition part : pontParts) {
        BigInteger n = makeNumber(pontList, pr, part, mu);
        pontNumbers.put(part, n);
      }
    }
    return charNumbers;
  }
  /**
   * Assumes all elements of Partition are < gradedPoly.size(),
   * and that all elements of gradedPoly, rb and d all have 
   * the same number of variables.
   * @param gradedPoly
   * @param part
   * @param d
   * @return 
   */
  private BigInteger makeNumber(
      List<Polynomial> gradedPoly,
      Polynomial.Ring pr,
      Partition part,
      MultiDegree d) {
    
    Polynomial p = pr.one();
    for (Integer i : part.getNumbers()) {
      p = pr.times(p, gradedPoly.get(i));
    }
    return p.get(d);
  }
  
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
