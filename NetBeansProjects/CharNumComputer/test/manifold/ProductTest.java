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
package manifold;

import polynomial.MultiDegree;
import polynomial.*;
import lib.*;
import java.math.BigInteger;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class ProductTest {
  
  Manifold m;
  MultiDegree.Builder mb;
  PartitionComputer pc;
  public ProductTest() {
    m  = new Product(Arrays.asList(new CP(2), new CP(3)));
    mb = new MultiDegree.Builder(2);
    pc = new PartitionComputer();
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of rDim method, of class Product.
   */
  @Test
  public void testRDim() {
    System.out.println("rDim");
    assertEquals(m.rDim(), 10);
  }

  /**
   * Test of isComplex method, of class Product.
   */
  @Test
  public void testIsComplex() {
    System.out.println("isComplex");
    assert(m.isComplex());
  }

  /**
   * Test of cDim method, of class Product.
   */
  @Test
  public void testCDim() {
    System.out.println("cDim");
    assertEquals(m.cDim(), 5);
  }

  /**
   * Test of truncation method, of class Product.
   */
  @Test
  public void testTruncation() {
    System.out.println("truncation");
    mb.set(0, 4). set(1, 6);
    assert (m.mu().equals(mb.build()));
  }

  /**
   * Test of getCharClasses method, of class Product.
   */
  @Test
  public void testGetCharClasses() {
    System.out.println("getCharClasses");
    
    //Map<String, Polynomial> charClasses = m.getCharClasses();
    PolyRing<BigInt> ring = m.cohomology();
    PolyRing<IntMod2> mod2Ring = m.mod2Cohomology();
    BigInt bigOne = BigInt.ring.one();
    IntMod2 littleOne = IntMod2.ring.one();
    
    // H^*(CP(2) x CP(3) ; Z/2) = (Z/2)[u,v]/<u^3, v^4> where u and v have degree 2.
    // It has Stiefel-Whitney class
    // w = 1 + w_2 + w_4 = 1 + u + u^2
    Map<Integer, PolyRing<IntMod2>.Element> gradedSWClass = m.swClass().getHomogeneousParts();
    // check each degree
    assert(gradedSWClass.get(0).equals(mod2Ring.one()));
    assert(gradedSWClass.get(2).equals(mod2Ring.makeElement(mb.set(0, 2).build(), littleOne)));
    assert(gradedSWClass.get(4).equals(mod2Ring.makeElement(mb.set(0, 4).build(), littleOne)));
    // check whole class
    PolyRing<IntMod2>.Element knownSW = mod2Ring.one();
    mb.zero().setVars(2);
    knownSW = mod2Ring.add(
            knownSW, 
            mod2Ring.makeElement(mb.set(0, 2).build(), IntMod2.ring.one()));
    knownSW = mod2Ring.add(
            knownSW, 
            mod2Ring.makeElement(mb.set(0, 4).build(), IntMod2.ring.one()));
    assert(m.swClass().equals(knownSW));
    
    // H^*(CP(2) x CP(3) ; Z) = Z[u,v]/<u^3, v^4> where u and v have degree 2.
    // It has Pontryagin class
    // p = 1 + p1 + p2 = 1 + 3u^2 + 4v^2 + 12 u^2v^2
    PolyRing<BigInt>.Element knownPont = ring.one();
    BigInt bigThree  = new BigInt(3);
    BigInt bigFour   = new BigInt(4);
    BigInt bigTwelve = new BigInt(12);
    mb.zero();
    knownPont = ring.add(
            knownPont, 
            ring.makeElement(mb.set(0, 4).build(), bigThree));
    knownPont = ring.add(
            knownPont, 
            ring.makeElement(mb.set(1, 4).build(), bigTwelve));
    knownPont = ring.add(
            knownPont, 
            ring.makeElement(mb.set(0, 0).build(), bigFour));
    assert(m.pontClass().equals(knownPont));
    
    // Chern class is given by
    // c = 1    + 4v     + 6v^2     + 4v^3
    //     3u   + 12uv   + 18uv^2   + 12uv^3
    //     3u^2 + 12u^2v + 18u^2V^2 + 12u^2v^3
    // The homogeneous terms are given by the diagonals.
    PolyRing<BigInt>.Element knownChern = ring.zero();
    mb.zero();
    int[] f1 = new int[]{1, 3, 3};
    int[] f2 = new int[]{1, 4, 6, 4};
    for (int i = 0; i < 3; i++) {
      mb.set(0, 2 * i);
      for (int j = 0; j < 4; j++) {
        mb.set(1, 2 * j);
        knownChern = ring.add(
                knownChern, 
                ring.makeElement(mb.build(), new BigInt(f1[i] * f2[j])));
      }
    }
    assert(m.chernClass().equals(knownChern));
        
  }
  
  /**
   * Test CharNumbers
   */
  @Test
  public void testCharNumbers() {    
    
    /*
    Test char numbers of m2 = CP(2) x CP(2).
    */
    
    Manifold m2 = new Product(Arrays.asList(new CP(2), new CP(2)));
    Manifold.CharNumbers charNums = m2.getCharNumbers(pc);
    
    // m2 has real-dimension 8, so has pontryagin classes p1 and p2.
    Partition v2   = new Partition(new Integer[]{2});
    Partition v1v1 = new Partition(new Integer[]{1, 1});
    assert(charNums.pontryaginNumber(v2)  .equals(new BigInt(9)));
    assert(charNums.pontryaginNumber(v1v1).equals(new BigInt(18)));
    
    // m2 is complex of dimension 4, so has c1, c2, c3, c4.
    Partition v4       = Partition.scale(v2, 2);
    Partition v1v3     = new Partition(new Integer[]{1, 3});
    Partition v2v2     = Partition.scale(v1v1, 2);
    Partition v1v1v2   = new Partition(new Integer[]{1, 1, 2});
    Partition v1v1v1v1 = new Partition(new Integer[]{1, 1, 1, 1});
    assert(charNums.chernNumber(v4)      .equals(new BigInt(9)));
    assert(charNums.chernNumber(v1v3)    .equals(new BigInt(54)));
    assert(charNums.chernNumber(v2v2)    .equals(new BigInt(99)));
    assert(charNums.chernNumber(v1v1v2)  .equals(new BigInt(216)));
    assert(charNums.chernNumber(v1v1v1v1).equals(new BigInt(486)));
    
    // w(m2) = c(m2) % 2, so w_i = 0 for i odd.
    // Therefore we only need to check partitions containing only even classes.
    Partition v8       = Partition.scale(v4, 2);
    Partition v2v6     = Partition.scale(v1v3, 2);
    Partition v4v4     = Partition.scale(v2v2, 2);
    Partition v2v2v4   = Partition.scale(v1v1v2, 2);
    Partition v2v2v2v2 = Partition.scale(v1v1v1v1, 2);
    assert(charNums.stiefelWhitneyNumber(v8)      .equals(IntMod2.ring.one()));
    assert(charNums.stiefelWhitneyNumber(v2v6)    .equals(IntMod2.ring.zero()));
    assert(charNums.stiefelWhitneyNumber(v4v4)    .equals(IntMod2.ring.one()));
    assert(charNums.stiefelWhitneyNumber(v2v2v4)  .equals(IntMod2.ring.zero()));
    assert(charNums.stiefelWhitneyNumber(v2v2v2v2).equals(IntMod2.ring.zero()));
    
    /*
    Test Hirzebruch's Signature Theorem on CP(2) x CP(4).
    */ 
    
    Manifold cp2cp4 = new Product(Arrays.asList(new CP(2), new CP(4)));
    charNums = cp2cp4.getCharNumbers(pc);
    
    // cp2cp4 has real-dimension 12, so has p1, p2, p3.
    Partition v3     = new Partition(new Integer[]{3});
    Partition v1v2   = new Partition(new Integer[]{1, 2});
    Partition v1v1v1 = new Partition(new Integer[]{1, 1, 1});
    int p3     = charNums.pontryaginNumber(v3).value().intValue();
    int p1p2   = charNums.pontryaginNumber(v1v2).value().intValue();
    int p1p1p1 = charNums.pontryaginNumber(v1v1v1).value().intValue();
    
    // L_12 = (63xp3 - 13xp1p2 + 2x(p1)^3) / 945
    int lNumerator = 62 * p3 - 13 * p1p2 + 2 * p1p1p1;
    assertEquals(lNumerator % 945, 0);
    int signature = lNumerator / 945;
    // The signature of CP(2) x CP(4) is 1.
    assertEquals(signature, 1);
    
    /*
    Test Hirzebruch's Signature Theorem on HP(2) x CP(2)
    */
    Manifold m3 = new Product(Arrays.asList(new HP(2), new CP(2)));
    mb.setVars(2).zero();
    PolyRing<BigInt> ring = m3.cohomology();
    PolyRing<BigInt>.Element knownPont = ring.one();
    knownPont = ring.add(
            knownPont, 
            ring.makeElement(mb.set(0,4).build(), new BigInt(2)));
    knownPont = ring.add(
            knownPont, 
            ring.makeElement(mb.set(0,8).build(), new BigInt(7)));
    knownPont = ring.add(
            knownPont, 
            ring.makeElement(mb.set(0,0).set(1,4).build(), new BigInt(3)));
    knownPont = ring.add(
            knownPont, 
            ring.makeElement(mb.set(0,4).build(), new BigInt(6)));
    knownPont = ring.add(
            knownPont, 
            ring.makeElement(mb.set(0,8).build(), new BigInt(21)));
    assert(m3.pontClass().equals(knownPont));
    
    Manifold.CharNumbers cn = m3.getCharNumbers(pc);
    p3     = cn.pontryaginNumber(new Partition(Arrays.asList(3))).intValue();
    p1p2   = cn.pontryaginNumber(new Partition(Arrays.asList(1, 2))).intValue();
    p1p1p1 = cn.pontryaginNumber(new Partition(Arrays.asList(1, 1, 1))).intValue();
    // L_12 = (63xp3 - 13xp1p2 + 2x(p1)^3) / 945
    lNumerator = 62 * p3 - 13 * p1p2 + 2 * p1p1p1;
    assertEquals(lNumerator % 945, 0);
    signature = lNumerator / 945;
    // The signature of CP(2) x CP(4) is 1.
    assertEquals(signature, 1);
    
  }
  
  
}
