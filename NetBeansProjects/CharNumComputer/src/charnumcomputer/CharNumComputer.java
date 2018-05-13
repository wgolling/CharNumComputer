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
 * CharNumComputer computes the characteristic numbers of some simple manifolds.\
 * 
 * It does this by providing a package for polynomial arithmetic,
 * and then uses this functionality to help model manifolds whose
 * cohomologies are truncated polynomial rings.
 * 
 * It can currently handle manifolds which are complex projective space CP(n),
 * quaternionic projective space HP(n), and products or them.
 * 
 * It computes Pontryagin numbers when the manifold's dimension is divisible by 4,
 * Chern numbers when the manifold is complex, 
 * and Stiefel-Whitney numbers in all cases.
 * 
 * @author William Gollinger
 */
public class CharNumComputer {

  /**
   * The main method runs demos.
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    searchFor12Manifold();
    identifyBordismClass();
  }
  
  /**
   * Computes characteristic information for some basic 12-manifolds, 
   * and looks for general examples with a particular property.
   */
  public static void searchFor12Manifold() {   
    Manifold m1 = new CP(6);
    Manifold m2 = new Product(Arrays.asList(new CP(2), new CP(4)));
    Manifold m3 = new Product(Arrays.asList(new CP(2), new CP(2), new CP(2)));  
    
    System.out.println(
              "**********************************\n"
            + "*                                *\n"
            + "*   Searching for 12 Manifolds   *\n"
            + "*                                *\n"
            + "**********************************\n");
    
    System.out.println(
            "Every closed, oriented 12-manifold is orientedly bordant\n" 
            +"to one (and only one) of the form\n"
    + String.format(
            "     a * %s + b * (%s) + c * (%s)\n", 
            m1.toString(), 
            m2.toString(), 
            m3.toString())
    + "where a, b, and c are integers and\n"
    + "CP(n) is complex projective space with complex dimension n.\n");
    
    System.out.println("  Since a manifold's oriented bordism class is determined\n"
    + "by its Stiefel-Whitney and Pontryagin numbers,\n"
    + "computing them for these particular manifolds would give\n"
    + "a lookup table to find a manifold's bordism class once its\n"
    + "characteristic numbers have been computed.\n"
    + "Here the software computes the Stiefel-Whitney and Pontryagin numbers\n"
    + "for these model manifolds, as well as their Chern numbers.\n");
    
    // Print characteristic classes of the three basic manifolds.
    System.out.println("The characteristic classes of " + m1.toString() + " are\n\n"
    + String.format(
            "c(%s) = \n%s\n\n" , 
            m1.toString(), 
            m1.chernClass().toString())
    + String.format(
            "w(%s) = \n%s\n\n", 
            m1.toString(), 
            m1.swClass().toString())
    + String.format(
            "p(%s) = \n%s\n" , 
            m1.toString(), 
            m1.pontClass().toString())
    + "\n");
    
    System.out.println("The characteristic classes of " + m2.toString() + " are\n\n"
    + String.format(
            "c(%s) = \n%s\n\n" , 
            m2.toString(), 
            m2.chernClass().toString())
    + String.format(
            "w(%s) = \n%s\n\n",
            m2.toString(), 
            m2.swClass().toString())
    + String.format(
            "p(%s) = \n%s\n", 
            m2.toString(), 
            m2.pontClass().toString())
    + "\n"); 
    
    System.out.println("The characteristic classes of " + m3.toString() + " are\n\n"
    + String.format(
            "c(%s) = \n%s\n\n" , 
            m3.toString(),
            m3.chernClass().toString())
    + String.format(
            "w(%s) = \n%s\n\n" , 
            m3.toString(), 
            m3.swClass().toString())
    + String.format(
            "p(%s) = \n%s\n" , 
            m3.toString(), 
            m3.pontClass().toString())
    + "\n");
    
    System.out.println();

    
    // Printing characteristic numbers.
    System.out.println("Characteristic numbers are additive, so to compute for\n"
    + String.format(
            "     a * %s + b * (%s) + c * (%s)", 
            m1.toString(), 
            m2.toString(), 
            m3.toString()));
    System.out.println("the software computes for each individual product manifold");
    System.out.println("and then adds the results together.");
    System.out.println();
    
    // get CharNumbers objects from the manifolds.
    PartitionComputer pc = new PartitionComputer();
    Manifold.CharNumbers charNums1 = m1.getCharNumbers(pc);
    Manifold.CharNumbers charNums2 = m2.getCharNumbers(pc);
    Manifold.CharNumbers charNums3 = m3.getCharNumbers(pc);
    // init lists of Partitions to iterate through.
    List<Partition> partsOf3 = pc.getPartitions(3);
    List<Partition> partsOf6 = pc.getPartitions(6);
    List<Partition> evenPartsOf12 = partsOf6.stream()
                                        .map(part -> Partition.scale(part,2))
                                        .collect(Collectors.toList());
            
    

    System.out.println("Characteristic numbers for the coefficients (a, b, c):\n ");
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
    System.out.println(
            "Since these manifolds are complex, the Stiefel-Whitney numbers\n"
            + "are just the mod 2 reduction of corresponding Chern numbers\n"
            + "and any Stiefel-Whitney number with an odd entry must vanish.\n"
    );
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

    System.out.println("\n\n");
    System.out.println("We now perform a search for manifolds with a certain property.\n"
            + "We want a 12-manifold with signature 16 whose tangent bundle\n" 
            + "stably reduces to a rank 7 bundle.\n"
            + "It would be necessary for this manifold that all Stiefel-Whitney\n" 
            + "classes above w_8 vanish, but if we only want a candiate bordism class\n"
            + "we can search for coefficients (a, b, c) such that any\n"
            + "Stiefel-Whitney number using classes w_8 and above must vanish.\n"
            + "\n"
            + "Here is a list of tuples (a,b,c) such that\n"
            + "a is in [-5,5], b is in [-5, 5], c == 16 - a - b,\n"
            + "and such that the corresponding manifold has\n"
            + "appropriate Stiefel-Whitney numbers:\n");
    
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
    System.out.println(
              "\n\nIt turns out these classes are rather abundant.\n"
            + "In fact, by looking at the formulae computed above, we can see\n"
            + "that the condition on the Stiefel-Whitney numbers is satisfied\n"
            + "for any triple (a, b, c) such that a == b mod 2 and c is even.\n\n\n\n");
  }
  
  static void identifyBordismClass() {
        System.out.println(
              "************************************\n"
            + "*                                  *\n"
            + "*   Indentifying Bordism Classes   *\n"
            + "*                                  *\n"
            + "************************************\n");

    
    System.out.println(
            "HP(2) is a closed oriented 8-manifold, so there exist unique\n"
            + "integers a and b such that HP(2) is orientedly bordant to\n"
            + "a * CP(4) + b * (CP(2) x CP(2))\n\n"
            + "Here the software determines a and b with brute force,\n"
            + "by comparing the Pontryagin and Stiefel-Whitney numbers\n"
            + "of HP(2) with a general bordism class.\n\n"
            + "HP(2) has signature 1, so the work is simplified\n"
            + "by the constraint b = 1 - a.  The software scans the values\n"
            + "of a in the interval [-10, 10] until is finds\n"
            + "a suitable pair (a, 1 - a):\n");
    
    PartitionComputer pc = new PartitionComputer();
    
    Manifold hp2 = new HP(2);
    CharNumbers cnHP2 = hp2.getCharNumbers(pc);
    
    Manifold cp4    = new CP(4);
    CharNumbers cnCP4    = cp4.getCharNumbers(pc);
    Manifold cp2cp2 = new Product(Arrays.asList(new CP(2), new CP(2)));
    CharNumbers cnCP2CP2 = cp2cp2.getCharNumbers(pc);
    
    List<Partition> pontList = pc.getPartitions(2);
    List<Partition> swList = pc.getPartitions(8);
    Integer a = null;
    Integer b = null;
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
              "The pair (%d, %d) does not work:",
              i, 
              1 - i));
          System.out.println(String.format(
              "    p(%s)%s = %s,  but p(%s)%s = %s.\n",
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
              "%d * %s + %d * (%s) does not work:",
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
      // If all Stiefel-Whitney numbers pass, you've found it!
      a = i;
      b = 1 - i;
      System.out.println(String.format(
          "The pair (%d, %d) works!\n", a, b));
      break;
    }
    if (a == null || b == null)
      System.out.println("Bordism class not found.");
    else {
    System.out.println(String.format(
        "%s is orientedly bordant to %d%s + %d(%s).\n",
        hp2.toString(),
        a,
        cp4.toString(),
        b,
        cp2cp2.toString()));
    }
    
    Manifold hp3       = new HP(3);
    Manifold cp6       = new CP(6);
    Manifold cp2cp4    = new Product(Arrays.asList(new CP(2), new CP(4)));
    Manifold cp2cp2cp2 = new Product(Arrays.asList(new CP(2), new CP(2), new CP(2)));
    
    System.out.println(
            "\nNext we play a similar game for " + hp3.toString() + ",\n"
            + "where the relevant generators are now\n"
            + String.format(
                    "%s, %s, and %s\n",
                    cp6.toString(),
                    cp2cp4.toString(),
                    cp2cp2cp2.toString()) 
            + "and the signature of " + hp3.toString() + " is 0.\n\n"
            + "This time we suppress output for negative results because\n"
            + "the search is much longer: it tries to scan all values\n"
            + "a in [-30, 30], b in [-30, 30], c = -(a + b).\n");
    
    CharNumbers cHP3       = hp3.getCharNumbers(pc);
    CharNumbers cCP6       = cp6.getCharNumbers(pc);
    CharNumbers cCP2CP4    = cp2cp4.getCharNumbers(pc);
    CharNumbers cCP2CP2CP2 = cp2cp2cp2.getCharNumbers(pc);
    pontList = pc.getPartitions(3);
    swList   = pc.getPartitions(12);
    a = null;
    b = null;
    Integer c = null;
    // Search for triples (a, b, c) with |a|, |b| <= 200 and a + b + c = 0
    for (int i = -30; i < 31; i++) {
      for (int j = -30; j < 31; j++) {
        int k = -(i + j);
      
        boolean pontPass = true;                                               // is set to false if any of the Pontryagin numbers don't work out.
        boolean swPass = true;

        for (Partition part : pontList) {

          BigInt first  = cCP6      .pontryaginNumber(part).times(new BigInt(i));
          BigInt second = cCP2CP4   .pontryaginNumber(part).times(new BigInt(j));
          BigInt third  = cCP2CP2CP2.pontryaginNumber(part).times(new BigInt(k));
          BigInt sum = first.plus(second).plus(third);


          // If the pontryagin classes don't match, break out and try the next value of i.
          if (!cHP3.pontryaginNumber(part).equals(sum)) {
//            System.out.println(String.format(
//                "The triple (%d, %d, %d) does not work.", i, j, k));
//            System.out.println(String.format(
//                "    p(%s)%s = %s,  but p(%s)%s = %s.\n",
//                hp3.toString(),
//                part.toString(), 
//                cHP3.pontryaginNumber(part).toString(),
//                i + cp6.toString() + " + " + j + cp2cp4.toString()
//                                   + " + " + k + cp2cp2cp2.toString(),
//                part.toString(),
//                sum.toString()));
            pontPass = false;
            break;   
          }
        }
        if (!pontPass)
          continue;
        // If all the Pontryagin numbers passed, check Stiefel-Whitney numbers.
        for (Partition part : swList) {
          //System.out.println(part.toString());
          IntMod2 first  = cCP6   .stiefelWhitneyNumber(part).times(new IntMod2(i));
          IntMod2 second = cCP2CP4.stiefelWhitneyNumber(part).times(new IntMod2(j));
          IntMod2 third  = cCP2CP2CP2.stiefelWhitneyNumber(part).times(new IntMod2(k));
          IntMod2 sum = first.plus(second).plus(third);

          if (!cHP3.stiefelWhitneyNumber(part).equals(sum)) {
//            System.out.println(String.format(
//                "The triple (%d, %d, %d) does not work:", i, j, k));
//            System.out.println(String.format(
//                "p(%s)%s = %s,  but p(%s)%s = %s.\n",
//                hp3.toString(),
//                part.toString(), 
//                cHP3.stiefelWhitneyNumber(part).toString(),
//                i + cp6.toString() + " + " + j + cp2cp4.toString()
//                                   + " + " + k + cp2cp2cp2.toString(),
//                part.toString(),
//                sum.toString()));
            swPass = false;
            break;
          }
        }
        if (!swPass)
          continue;
        // If all Stiefel-Whitney numbers pass, you've found it!
        a = i;
        b = j;
        c = k;
        System.out.println(String.format(
            "The triple (%d, %d, %d) works!\n", a, b, c));
        break;
      }
      if (a != null && b != null)
        break;
      }
      if (a == null || b == null)
        System.out.println("Bordism class not found.");
      else {
      System.out.println(String.format(
          "%s is orientedly bordant to %d%s + %d(%s) + %d%s.\n",
          hp3.toString(),
          a,
          cp6.toString(),
          b,
          cp2cp4.toString(),
          c,
          cp2cp2cp2.toString()));

      }
      
      
      System.out.println(
                "(The user should be cautioned that the bordism class\n"
              + "of " + hp2.toString() + " has been verified by hand, but the bordism\n"
              + "class of " + hp3.toString() + " has not.)");
      
      
  }

}
