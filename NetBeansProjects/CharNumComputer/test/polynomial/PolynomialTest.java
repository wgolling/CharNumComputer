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
package polynomial;

import polynomial.MultiDegree;
import polynomial.Polynomial;
import java.math.BigInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class PolynomialTest {
  
  MultiDegree.Builder mb;
  Polynomial.Ring pr;
  public PolynomialTest() {
    mb = new MultiDegree.Builder();
    pr = new Polynomial.Ring(2);
  }
  
  @Before
  public void setUp() {
    mb.reset();
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of get method, of class Polynomial.
   */
  @Test
  public void testGet() {
    System.out.println("get");
    MultiDegree d = mb.setVars(2).set(0,1).set(1,3).build();
    Polynomial p = new Polynomial(d, BigInteger.TEN);
    assertEquals(p.get(d), BigInteger.TEN);
    assertEquals(p.get(mb.zero().build()), BigInteger.ZERO);
  }
  

  /**
   * Test of equals method, of class Polynomial.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
  }

  /**
   * Test of hashCode method, of class Polynomial.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
  }

  /**
   * Test of isZero method, of class Polynomial.
   */
  @Test
  public void testIsZero() {
    System.out.println("isZero");
  }

  /**
   * Test of vars method, of class Polynomial.
   */
  @Test
  public void testVars() {
    System.out.println("vars");
  }
  
  /**
   * Test of plus method, of class Polynomial.Ring
   */
  @Test
  public void testPlus() {
    System.out.println("plus");
    
    MultiDegree d = mb.setVars(2).set(0,1).set(1,3).build();
    Polynomial p = new Polynomial(d, BigInteger.valueOf(3));
    
    Polynomial q = new Polynomial(d, BigInteger.valueOf(3));
    assert(p.equals(q));
    
    Polynomial twoP = pr.plus(p, p);
    assertEquals(twoP.get(d), BigInteger.valueOf(6));
    Polynomial pPlusQ = pr.plus(p, q);
    assert(twoP.equals(pPlusQ));

    Polynomial minusP = new Polynomial(d, BigInteger.valueOf(3).negate());
    Polynomial diff = pr.plus(p, minusP);
    assert(pr.plus(p, minusP).isZero());

    Polynomial zero = new Polynomial(2);
    assert(diff.equals(zero));
    Polynomial pPrime = pr.plus(p, zero);
    assert(p.equals(pPrime));
    
    Polynomial zero3 = new Polynomial(3);
    assert(!zero.equals(zero3));
  }
  
  /**
   * Test of times method, of class Polynomial.Ring
   */
  @Test
  public void testTimes() {
    System.out.println("times");
    MultiDegree d = mb.setVars(2).set(0,1).set(1,3).build();
    Polynomial p = new Polynomial(d, BigInteger.valueOf(3));
    MultiDegree e = mb.set(0,2).set(1,2).build();
    Polynomial q = new Polynomial(e, BigInteger.valueOf(5));
    Polynomial prod = pr.times(p, q);
    assertEquals(BigInteger.valueOf(15), prod.get(MultiDegree.add(d, e)));
    
    Polynomial pPrime = pr.times(p, pr.one());
    assert(p.equals(pPrime));
    
    pPrime = pr.times(p, pr.zero());
    assert(pPrime.isZero());
  }
  
  /**
   * Test of tensor method, of class Polynomial.Ring
   */
  @Test
  public void testTensor() {
    System.out.println("tensor");
    MultiDegree d = mb.setVars(3).set(0,1).set(1,3).set(2,2).build();
    Polynomial p = new Polynomial(d, BigInteger.valueOf(3));
    MultiDegree e = mb.setVars(2).set(0,2).set(1,2).build();
    Polynomial q = new Polynomial(e, BigInteger.valueOf(5));
    
    Polynomial tensor = pr.tensor(p, q);
    assertEquals(tensor.get(MultiDegree.concat(d, e)), BigInteger.valueOf(15));
    
    Polynomial.Ring pr3 = new Polynomial.Ring(3);
    Polynomial.Ring pr5 = new Polynomial.Ring(5);
    assert(pr.tensor(pr.one(), pr3.one()).equals(pr5.one()));
  }
  
  /**
   * Test of Ring.tensor method
   */
  @Test
  public void ringTensor() {
    System.out.println("ringTensor");
  
    mb.setVars(2);
    MultiDegree variables1 = mb.setAll(1).build();
    MultiDegree trunc1 = mb.set(0, 2).set(1, 3).build();
    Integer modulus1 = null;
    Polynomial.Ring r1 = new Polynomial.Ring(variables1, trunc1);
    
    mb.setVars(3);
    MultiDegree variables2 = mb.set(0,1).set(1,2).set(2,2).build();
    MultiDegree trunc2 = mb.setAll(3).build();
    Integer modulus2 = 6;
    Polynomial.Ring r2 = new Polynomial.Ring(variables2, trunc2, modulus2);
    
    Polynomial.Ring tensor1 = Polynomial.Ring.tensor(r1, r2);
    assert(tensor1.variables().equals(MultiDegree.concat(variables1, variables2)));
    assert(tensor1.truncation().equals(MultiDegree.concat(trunc1, trunc2)));
    assert(tensor1.modulus().equals(6));
    
    mb.setVars(0);
    Polynomial.Ring r3 = new Polynomial.Ring(mb.build(), mb.build(), 15);
    
    Polynomial.Ring tensor2 = Polynomial.Ring.tensor(r2, r3);
    assert(tensor2.variables().equals(variables2));
    assert(tensor2.truncation().equals(trunc2));
    assert(tensor2.modulus().equals(3));
    
    Polynomial.Ring r4 = new Polynomial.Ring(0);
    Polynomial.Ring tensor3 = Polynomial.Ring.tensor(r2, r4);
    assert(tensor3.variables().equals(r2.variables()));
    assert(tensor3.truncation().equals(r2.truncation()));
    assert(tensor3.modulus().equals(6));
  }
}
