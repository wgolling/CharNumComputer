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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class PolyRingTest {
  
  PolyRing<Int> groundRing;
  PolyRing<Int> twoVars;
  PolyRing<Int> truncated;
  
  MultiDegree.Builder mb = new MultiDegree.Builder();
  
  public PolyRingTest() {
    Int ring = new Int(0);
    groundRing = new PolyRing<>(ring, 0);
    twoVars    = new PolyRing<>(ring, 2);
    mb.setVars(3).set(0,2).set(1,3).set(2,4);
    truncated  = new PolyRing<>(ring, mb.build());
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of zero method, of class PolyRing.
   */
  @Test
  public void testZero() {
    System.out.println("zero");
    PolyRing.Element zero = groundRing.zero();
    assert(zero.isZero());
    PolyRing.Element zero2 = groundRing.new Element();
    assert(zero.equals(zero2));
    PolyRing.Element zero3 = twoVars.zero();
    assert(zero3.isZero());
    assert(!zero3.equals(zero));
  }

  /**
   * Test of one method, of class PolyRing.
   */
  @Test
  public void testOne() {
    System.out.println("one");
    PolyRing.Element one  = twoVars.one();
    PolyRing.Element one2 = twoVars.new Element(mb.zero().setVars(2).build(),
                                                new Int(1));
    assert(one.equals(one2));
  }

  /**
   * Test of add method, of class PolyRing.
   */
  @Test
  public void testAdd() {
    System.out.println("plus");
    MultiDegree v0v1 = mb.zero().setVars(2).set(0,1).set(1,1).build();
    PolyRing.Element p = twoVars.new Element(v0v1, twoVars.cRing().one());
    PolyRing.Element twoP = twoVars.add(p, p);
    Int two = new Int(2);
    assert((twoP.get(v0v1).equals(two)));
  }

  /**
   * Test of cRing method, of class PolyRing.
   */
  @Test
  public void testCRing() {
    System.out.println("cRing");
    assert(twoVars.cRing() instanceof Int);
  }

  /**
   * Test of multiply method, of class PolyRing.
   */
  @Test
  public void testMultiply() {
    System.out.println("multiply");
    MultiDegree mu = mb.zero().setVars(3).set(0,1).set(1,1).set(2,1).build();
    PolyRing.Element p = truncated.new Element(mu, new Int(2));
    PolyRing.Element pSquared = truncated.multiply(p, p);
    MultiDegree muSquared = MultiDegree.raise(mu);
    assert(pSquared.get(muSquared).equals(new Int(4)));
    PolyRing.Element pCubed = truncated.multiply(p, pSquared);
    assert(pCubed.isZero());
  }

  /**
   * Test of tensor method, of class PolyRing.
   */
  @Test
  public void testTensor() {
    System.out.println("tensor");
    PolyRing r = null;
    PolyRing s = null;
    PolyRing<? extends Coefficient> expResult = null;
    PolyRing<? extends Coefficient> result = PolyRing.tensor(r, s);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  
}
