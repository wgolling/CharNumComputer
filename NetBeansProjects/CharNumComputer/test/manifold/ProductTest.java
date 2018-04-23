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
    m = new Product(Arrays.asList(new CP(2), new CP(3)));
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
    assert (m.truncation().equals(mb.build()));
  }

  /**
   * Test of getCharClasses method, of class Product.
   */
  @Test
  public void testGetCharClasses() {
    System.out.println("getCharClasses");
    
    Map<String, Polynomial> charClasses = m.getCharClasses();
    Polynomial.Ring ring = m.cohomology();
    Polynomial.Ring mod2Ring = m.mod2Cohomology();
    BigInteger bigOne = BigInteger.ONE;
    
    // H^*(CP(2) x CP(3) ; Z/2) = (Z/2)[u,v]/<u^3, v^4> where u and v have degree 2.
    // It has Stiefel-Whitney class
    // w = 1 + w_2 + w_4 = 1 + u + u^2
    Polynomial swClass = charClasses.get("sw");
    // check each degree
    assert(swClass.getHomogeneousPart(0).equals(mod2Ring.one()));
    assert(swClass.getHomogeneousPart(2).equals(new Polynomial(mb.set(0, 2).build(), bigOne)));
    assert(swClass.getHomogeneousPart(4).equals(new Polynomial(mb.set(0, 4).build(), bigOne)));
    // check whole class
    Polynomial knownSW = mod2Ring.one();
    mb.zero().setVars(2);
    knownSW.addMonomial(mb.set(0, 2).build(), bigOne, mod2Ring);
    knownSW.addMonomial(mb.set(0, 4).build(), bigOne, mod2Ring);
    assert(charClasses.get("sw").equals(knownSW));
    
    // H^*(CP(2) x CP(3) ; Z) = Z[u,v]/<u^3, v^4> where u and v have degree 2.
    // It has Pontryagin class
    // p = 1 + p1 + p2 = 1 + 3u^2 + 4v^2 + 12 u^2v^2
    Polynomial knownPont = ring.one();
    BigInteger bigThree  = BigInteger.valueOf(3);
    BigInteger bigFour   = BigInteger.valueOf(4);
    BigInteger bigTwelve = BigInteger.valueOf(12);
    mb.zero();
    knownPont.addMonomial(mb.set(0, 4).build(), bigThree, ring);
    knownPont.addMonomial(mb.set(1, 4).build(), bigTwelve, ring);
    knownPont.addMonomial(mb.set(0, 0).build(), bigFour, ring);
    assert(charClasses.get("pont").equals(knownPont));
    
    // Chern class is given by
    // c = 1    + 4v     + 6v^2     + 4v^3
    //     3u   + 12uv   + 18uv^2   + 12uv^3
    //     3u^2 + 12u^2v + 18u^2V^2 + 12u^2v^3
    // The homogeneous terms are given by the diagonals.
    Polynomial knownChern = new Polynomial(2);
    mb.zero();
    int[] f1 = new int[]{1, 3, 3};
    int[] f2 = new int[]{1, 4, 6, 4};
    for (int i = 0; i < 3; i++) {
      mb.set(0, 2 * i);
      for (int j = 0; j < 4; j++) {
        mb.set(1, 2 * j);
        knownChern.addMonomial(
            mb.build(),
            BigInteger.valueOf(f1[i] * f2[j]),
            ring);
      }
    }
    assert(charClasses.get("chern").equals(knownChern));
        
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
    assert(charNums.get("pont", v2)  .equals(BigInteger.valueOf(9)));
    assert(charNums.get("pont", v1v1).equals(BigInteger.valueOf(18)));
    
    // m2 is complex of dimension 4, so has c1, c2, c3, c4.
    Partition v4       = Partition.scale(v2, 2);
    Partition v1v3     = new Partition(new Integer[]{1, 3});
    Partition v2v2     = Partition.scale(v1v1, 2);
    Partition v1v1v2   = new Partition(new Integer[]{1, 1, 2});
    Partition v1v1v1v1 = new Partition(new Integer[]{1, 1, 1, 1});
    assert(charNums.get("chern", v4)      .equals(BigInteger.valueOf(9)));
    assert(charNums.get("chern", v1v3)    .equals(BigInteger.valueOf(54)));
    assert(charNums.get("chern", v2v2)    .equals(BigInteger.valueOf(99)));
    assert(charNums.get("chern", v1v1v2)  .equals(BigInteger.valueOf(216)));
    assert(charNums.get("chern", v1v1v1v1).equals(BigInteger.valueOf(486)));
    
    // w(m2) = c(m2) % 2, so w_i = 0 for i odd.
    // Therefore we only need to check partitions containing only even classes.
    Partition v8       = Partition.scale(v4, 2);
    Partition v2v6     = Partition.scale(v1v3, 2);
    Partition v4v4     = Partition.scale(v2v2, 2);
    Partition v2v2v4   = Partition.scale(v1v1v2, 2);
    Partition v2v2v2v2 = Partition.scale(v1v1v1v1, 2);
    assert(charNums.get("sw", v8)      .equals(BigInteger.ONE));
    assert(charNums.get("sw", v2v6)    .equals(BigInteger.ZERO));
    assert(charNums.get("sw", v4v4)    .equals(BigInteger.ONE));
    assert(charNums.get("sw", v2v2v4)  .equals(BigInteger.ZERO));
    assert(charNums.get("sw", v2v2v2v2).equals(BigInteger.ZERO));
    
    /*
    Test Hirzebruch's Signature Theorem on CP(2) x CP(4).
    */ 
    
    Manifold cp2cp4 = new Product(Arrays.asList(new CP(2), new CP(4)));
    charNums = cp2cp4.getCharNumbers(pc);
    
    // cp2cp4 has real-dimension 12, so has p1, p2, p3.
    Partition v3     = new Partition(new Integer[]{3});
    Partition v1v2   = new Partition(new Integer[]{1, 2});
    Partition v1v1v1 = new Partition(new Integer[]{1, 1, 1});
    int p3     = charNums.get("pont", v3).intValue();
    int p1p2   = charNums.get("pont", v1v2).intValue();
    int p1p1p1 = charNums.get("pont", v1v1v1).intValue();
    
    // L_12 = (63xp3 - 13xp1p2 + 2x(p1)^3) / 945
    int lNumerator = 62 * p3 - 13 * p1p2 + 2 * p1p1p1;
    assertEquals(lNumerator % 945, 0);
    int signature = lNumerator / 945;
    // The signature of CP(2) x CP(4) is 1.
    assertEquals(signature, 1);
  }
  
  
}
