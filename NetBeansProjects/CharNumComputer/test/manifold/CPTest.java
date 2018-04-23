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
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class CPTest {
  
  Manifold cp3;
  public CPTest() {
    cp3 = new CP(3);
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of toString method, of class CP.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    assert(cp3.toString().equals("CP(3)"));
  }

  /**
   * Test of rDim method, of class CP.
   */
  @Test
  public void testRDim() {
    System.out.println("rDim");
    assertEquals(cp3.rDim(), 6);
  }

  /**
   * Test of isComplex method, of class CP.
   */
  @Test
  public void testIsComplex() {
    System.out.println("isComplex");
    assert(cp3.isComplex());
  }

  /**
   * Test of cDim method, of class CP.
   */
  @Test
  public void testCDim() {
    System.out.println("cDim");
    assertEquals(cp3.cDim(), 3);
  }

  /**
   * Test of truncation method, of class CP.
   */
  @Test
  public void testTruncation() {
    System.out.println("truncation");
    MultiDegree.Builder mb = new MultiDegree.Builder(1);
    assert(cp3.truncation().equals(mb.set(0, 6).build()));
  }

  /**
   * Test of getCharClasses method, of class CP.
   */
  @Test
  public void testGetCharClasses() {
    System.out.println("getCharClasses");
    
    /*
    Test characteristic classes of CP(3).
    */
    
    Map<String, Polynomial> charClasses = cp3.getCharClasses();
    Polynomial.Ring ring     = cp3.cohomology();
    Polynomial.Ring mod2Ring = cp3.mod2Cohomology();
    Polynomial chernClass = charClasses.get("chern");
    Polynomial swClass    = charClasses.get("sw");
    Polynomial pontClass  = charClasses.get("pont");
    
    MultiDegree.Builder mb = new MultiDegree.Builder(1);
    
    // H^*(CP(3) ; Z) = Z[u]/<u^4> where u has degree 2.
    // c = 1 + c_1 + c_2  + c_3 
    //   = 1 + 4u  + 6u^2 + 4u^3
    MultiDegree d = mb.zero().build();
    assert(chernClass.get(d).equals(BigInteger.ONE));
    d = mb.set(0, 2).build();
    assert(chernClass.get(d).equals(BigInteger.valueOf(4)));
    d = mb.set(0, 4).build();
    assert(chernClass.get(d).equals(BigInteger.valueOf(6)));
    d = mb.set(0, 6).build();
    assert(chernClass.get(d).equals(BigInteger.valueOf(4)));
    Polynomial knownChern = ring.one()
            .addMonomial(mb.set(0, 2).build(), BigInteger.valueOf(4), ring)
            .addMonomial(mb.set(0, 4).build(), BigInteger.valueOf(6), ring)
            .addMonomial(mb.set(0, 6).build(), BigInteger.valueOf(4), ring);
    assert(chernClass.equals(knownChern));
    
    // w = 1 in H^*(CP(3) ; Z/2) = (Z/2)[u]/<u^4>
    Polynomial knownSW = mod2Ring.one();
    assert(swClass.equals(knownSW));
    
    // p = 1 + 4u^2
    Polynomial knownPont = ring.one();
    d = mb.set(0, 4).build();
    knownPont.addMonomial(d, BigInteger.valueOf(4), ring);
    assert(pontClass.equals(knownPont));
  }
  
  /**
   * Test CharNumbers
   */
  @Test
  public void testCharNumbers() {
    
    /*
    Test characteristic number of CP(3).
    */
    
    PartitionComputer pc = new PartitionComputer();
    Manifold.CharNumbers charNums = cp3.getCharNumbers(pc);
    
    BigInteger c3      = charNums.get("chern", new Partition(new Integer[]{3}));
    BigInteger c1c2    = charNums.get("chern", new Partition(new Integer[]{1, 2}));
    BigInteger c1cubed = charNums.get("chern", new Partition(new Integer[]{1, 1, 1}));
    assert(c3.equals(BigInteger.valueOf(4)));
    assert(c1c2.equals(BigInteger.valueOf(24)));
    assert(c1cubed.equals(BigInteger.valueOf(64)));
    
    // rDim(CP(3)) is not divisible by 4 so there are no Pontryagin numbers
    assert(charNums.get("pont", new Partition()) == null);
    
    BigInteger w6      = charNums.get("sw", new Partition(new Integer[]{6}));
    BigInteger w2w4    = charNums.get("sw", new Partition(new Integer[]{2, 4}));
    BigInteger w2cubed = charNums.get("sw", new Partition(new Integer[]{2, 2, 2}));
    assert(w6.equals(BigInteger.ZERO));
    assert(w2w4.equals(BigInteger.ZERO));
    assert(w2cubed.equals(BigInteger.ZERO));
    
    Manifold cp2 = new CP(2);
    charNums = cp2.getCharNumbers(pc);
    Partition v4   = new Partition(new Integer[]{4});
    Partition v2v2 = new Partition(new Integer[]{2, 2});
    assert(charNums.get("sw", v4).equals(BigInteger.ONE));
    assert(charNums.get("sw", v2v2).equals(BigInteger.ONE));
 }
  
}
