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
import java.util.stream.Collectors;
import manifold.*;
import manifold.Manifold.CharNumbers;

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
    identifyBordismClass();
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
    
    System.out.println("The characteristic classes of " + m1.toString() + " are");
    System.out.println(
            String.format("c(%s) = \n%s" , m1.toString()
                                       , m1.chernClass().toString()));
    System.out.println(
            String.format("w(%s) = \n%s" , m1.toString()
                                       , m1.swClass().toString()));
    System.out.println(
            String.format("p(%s) = \n%s" , m1.toString()
                                       , m1.pontClass().toString()));
    System.out.println();
    System.out.println("The characteristic classes of " + m2.toString() + " are");
    System.out.println(
            String.format("c(%s) = \n%s" , m2.toString()
                                       , m2.chernClass().toString()));
    System.out.println(
            String.format("w(%s) = \n%s" , m2.toString()
                                       , m2.swClass().toString()));
    System.out.println(
            String.format("p(%s) = \n%s" , m2.toString()
                                       , m2.pontClass().toString()));
    System.out.println();
    System.out.println("The characteristic classes of " + m3.toString() + " are");
    System.out.println(
            String.format("c(%s) = \n%s" , m3.toString()
                                       , m3.chernClass().toString()));
    System.out.println(
            String.format("w(%s) = \n%s" , m3.toString()
                                       , m3.swClass().toString()));
    System.out.println(
            String.format("p(%s) = \n%s" , m3.toString()
                                       , m3.pontClass().toString()));
    System.out.println();
    
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
    List<Partition> evenPartsOf12 = partsOf6.stream()
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
              String.format("c%s = %sa + %sb + %sc",
                      part.toString(),
                      charNums1.chernNumber(part),
                      charNums2.chernNumber(part),
                      charNums3.chernNumber(part)));
    }
    System.out.println();
    // sw numbers
    for (Partition part : evenPartsOf12) {
      String prefix = "w" + part.toString() + " = ";
      List<String> nonZero = new ArrayList<>();
      if (!charNums1.stiefelWhitneyNumber(part).equals(IntMod2.ring.zero())) nonZero.add("a");
      if (!charNums2.stiefelWhitneyNumber(part).equals(IntMod2.ring.zero())) nonZero.add("b");
      if (!charNums3.stiefelWhitneyNumber(part).equals(IntMod2.ring.zero())) nonZero.add("c");
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
              String.format("p%s = %sa + %sb + %sc",
                      part.toString(),
                      charNums1.pontryaginNumber(part),
                      charNums2.pontryaginNumber(part),
                      charNums3.pontryaginNumber(part)));
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
        Map<Partition, IntMod2> swSums = new HashMap<>();
        for (Partition part : atLeast8) {
          IntMod2 num1 = charNums1.stiefelWhitneyNumber(part);
          IntMod2 num2 = charNums2.stiefelWhitneyNumber(part);
          IntMod2 num3 = charNums3.stiefelWhitneyNumber(part);
          
          IntMod2 sum = num1.times(new IntMod2(a))
                              .plus(num2.times(new IntMod2(b)))
                              .plus(num3.times(new IntMod2(16 - a - b)));
          if (sum.equals(IntMod2.ring.zero())) continue;
          swSums.put(part, sum);
        }
        if (swSums.isEmpty()) {
          System.out.println(
                  String.format("a = %d, b = %d, c = %d", a, b, 16 - a - b));
        }
      }
    }
  }
  
  static void identifyBordismClass() {
    System.out.println(
            "HP(2) is a closed oriented 8-manifold, so there must be\n"
            + "integers a and b such that HP(2) is orientedly bordant to\n"
            + "a * CP(4) + b * (CP(2) x CP(2))\n"
            + "\nWe determine with brute force what these coefficients are.\n"
            + "HP(2) has signature 1, so at least we know b = 1 - a.\n");
    
    PartitionComputer pc = new PartitionComputer();
    
    Manifold hp2 = new HP(2);
    CharNumbers cnHP2 = hp2.getCharNumbers(pc);
    
    Manifold cp4    = new CP(4);
    CharNumbers cnCP4 = cp4.getCharNumbers(pc);
    Manifold cp2cp2 = new Product(Arrays.asList(new CP(2), new CP(2)));
    CharNumbers cnCP2CP2 = cp2cp2.getCharNumbers(pc);
    
    List<Partition> pontList = pc.getPartitions(2);
    List<Partition> swList = pc.getPartitions(8);
    
    // Search for pairs (a,b) with |a| <= 10
    for (int i = -10; i < 11; i++) {
      
      boolean pontPass = true;                                               // is set to false if any of the Pontryagin numbers don't work out.
      boolean swPass = true;
      
      for (Partition part : pontList) {
        
        BigInt first  = cnCP4   .pontryaginNumber(part).times(new BigInt(i));
        BigInt second = cnCP2CP2.pontryaginNumber(part).times(new BigInt(1 - i));
        BigInt sum = first.plus(second);
        
        // If the pontryagin classes don't match, break out and try the next value of i.
        if (!cnHP2.pontryaginNumber(part).equals(sum)) {
          System.out.println(String.format(
              "%d * %s + %d * (%s) does not work.",
              i, cp4.toString(), 1 - i, cp2cp2.toString()));
          System.out.println(String.format(
              "p(%s)%s = %s,  but p(%s)%s = %s.\n",
              hp2.toString(),
              part.toString(), 
              cnHP2.pontryaginNumber(part).toString(),
              i + cp4.toString() + " + " + (1 - i) + cp2cp2.toString(),
              part.toString(),
              sum.toString()));
          pontPass = false;
          break;   
        }
      }
      if (!pontPass)
        continue;
      // If all the Pontryagin numbers passed, check Stiefel-Whitney numbers.
      for (Partition part : swList) {
        //System.out.println(part.toString());
        IntMod2 first  = cnCP4   .stiefelWhitneyNumber(part).times(new IntMod2(i));
        IntMod2 second = cnCP2CP2.stiefelWhitneyNumber(part).times(new IntMod2(1 - i));
        IntMod2 sum = first.plus(second);
        
        if (!cnHP2.stiefelWhitneyNumber(part).equals(sum)) {
          System.out.println(String.format(
              "%d * %s + %d * (%s) does not work.",
              i, cp4.toString(), 1 - i, cp2cp2.toString()));
          System.out.println(String.format(
              "p(%s)%s = %s,  but p(%s)%s = %s.\n",
              hp2.toString(),
              part.toString(), 
              cnHP2.stiefelWhitneyNumber(part).toString(),
              i + cp4.toString() + " + " + (1 - i) + cp2cp2.toString(),
              part.toString(),
              sum.toString()));
          swPass = false;
          break;
        }
      }
      if (!swPass)
        continue;
      // If all Stiefel-Whitney numbers pass, you've found it!.
      System.out.println(String.format(
          "%s is orientedly bordant to %d%s + %d(%s).",
          hp2.toString(),
          i,
          cp4.toString(),
          1 - i,
          cp2cp2.toString()));
      return;
    }
  }
//  private static CharNumbers scaleCharNumbers(Manifold m, int a) {
//    CharNumbers c = new CharNumbers();
//    c.pontNums = m.getCharNumbers();
//    
//  }
//  private static CharNumbers add(CharNumbers a, CharNumbers b) {
//
//  }

}
