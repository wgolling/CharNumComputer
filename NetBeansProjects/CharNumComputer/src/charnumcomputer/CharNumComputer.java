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

import polynomial.*;
import lib.*;
import java.util.*;
import java.math.*;
import java.util.stream.Collectors;
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
    
    System.out.println(
            "Every closed, oriented 12-manifold is orientedly bordant to one of the form");
    System.out.println(
            String.format("     a * %s + b * (%s) + c * (%s)", m1.toString()
                                                    , m2.toString()
                                                    , m3.toString()));
    System.out.println(
            "where a, b, and c are integers.\n"); 
    System.out.println("  Since a manifold's oriented bordism class is determined");
    System.out.println("by its Stiefel-Whitney and Pontryagin numbers, ");
    System.out.println("computing them for these particular manifolds would give");
    System.out.println("a lookup table to find a manifold's bordism class once its");
    System.out.println("characteristic numbers have been computed.");
    System.out.println("Here we compute the Stiefel-Whitney and Pontryagin numbers");
    System.out.println("for these model manifolds, as well as their Chern numbers.");
    System.out.println();
    
    Map<String, Polynomial> charClasses1 = m1.getCharClasses();
    System.out.println("The characteristic classes of " + m1.toString() + " are");
    System.out.println(
            String.format("c(%s) = %s" , m1.toString()
                                       , charClasses1.get("chern").toString()));
    System.out.println(
            String.format("w(%s) = %s" , m1.toString()
                                       , charClasses1.get("sw").toString()));
    System.out.println(
            String.format("p(%s) = %s" , m1.toString()
                                       , charClasses1.get("pont").toString()));
    System.out.println();
    
    Map<String, Polynomial> charClasses2 = m2.getCharClasses();
    System.out.println("The characteristic classes of " + m2.toString() + " are");
    System.out.println(
            String.format("c(%s) = %s" , m2.toString()
                                       , charClasses2.get("chern").toString()));
    System.out.println(
            String.format("w(%s) = %s" , m2.toString()
                                       , charClasses2.get("sw").toString()));
    System.out.println(
            String.format("p(%s) = %s" , m2.toString()
                                       , charClasses2.get("pont").toString()));
    System.out.println();

    Map<String, Polynomial> charClasses3 = m3.getCharClasses();
    System.out.println("The characteristic classes of " + m3.toString() + " are");
    System.out.println(
            String.format("c(%s) = %s" , m3.toString()
                                       , charClasses3.get("chern").toString()));
    System.out.println(
            String.format("w(%s) = %s" , m3.toString()
                                       , charClasses3.get("sw").toString()));
    System.out.println(
            String.format("p(%s) = %s" , m3.toString()
                                       , charClasses3.get("pont").toString()));
    System.out.println();

    System.out.println("Characteristic numbers are additive, so to compute for");
    System.out.println(
            String.format("     a * %s + b * (%s) + c * (%s)", m1.toString()
                                                    , m2.toString()
                                                    , m3.toString()));
    System.out.println("it suffices to compute for each product manifold");
    System.out.println("and add the results together.  This is done below.");
    System.out.println();
    
    PartitionComputer pc = new PartitionComputer();
    Manifold.CharNumbers charNums1 = m1.getCharNumbers(pc);
    Manifold.CharNumbers charNums2 = m2.getCharNumbers(pc);
    Manifold.CharNumbers charNums3 = m3.getCharNumbers(pc);

    List<Partition> partsOf3 = pc.getPartitions(3);
    List<Partition> partsOf6 = pc.getPartitions(6);
    List<Partition> evenPartsOf6 = partsOf6.stream()
                                        .map(part -> Partition.scale(part,2))
                                        .collect(Collectors.toList());
            
    

    System.out.println("Characteristic numbers of ");
    System.out.println(String.format("a * (%s) + b * (%s) + c * (%s)", 
                                    m1.toString(),
                                    m2.toString(),
                                    m3.toString()));
    System.out.println();
    // chern numbers
    for (Partition part : partsOf6) {
      System.out.println(
              String.format("c%s = a%s + b%s + c%s",
                      part.toString(),
                      charNums1.get("chern", part),
                      charNums2.get("chern", part),
                      charNums3.get("chern", part)));
    }
    System.out.println();
    // sw numbers
    for (Partition part : evenPartsOf6) {
      String prefix = "w" + part.toString() + " = ";
      List<String> nonZero = new ArrayList<>();
      if (!charNums1.get("sw", part).equals(BigInteger.ZERO)) nonZero.add("a");
      if (!charNums2.get("sw", part).equals(BigInteger.ZERO)) nonZero.add("b");
      if (!charNums3.get("sw", part).equals(BigInteger.ZERO)) nonZero.add("c");
      String number = "";
      boolean first = true;
      for (String s : nonZero) {
        if (!first) number += " + ";
        else first = false;
        number += s;
      }
      if (number.equals("")) number = "0";
      System.out.println(prefix + number);
    }
    System.out.println();
    // pont numbers
    for (Partition part : partsOf3) {
      System.out.println(
              String.format("p%s = a%s + b%s + c%s",
                      part.toString(),
                      charNums1.get("pont", part),
                      charNums2.get("pont", part),
                      charNums3.get("pont", part)));
    }

    System.out.println();
    System.out.println("Here is a list of tuples (a,b,c) such that");
    System.out.println("a is in [-5,5], b is in [-5, 5], c == 16 - a - b,");
    System.out.println("and such that the combination of complex projective spaces");
    System.out.println("has no Stiefel-Whitney number using a class of degree at least 8.");
    
    Map<Integer, Set<Partition>> occ = pc.getOccurrences(12);
    Set<Partition> atLeast8 = new HashSet<>();
    for (int i = 8; i < 13; i++) {
      atLeast8.addAll(occ.get(i));
    }
    
    for (int a = -5; a < 6; a++) {
      for (int b = -5; b < 6; b++) {
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
          System.out.println(
                  String.format("a = %d, b = %d, c = %d", a, b, 16 - a - b));
        }
      }
    }
  }
  

}
