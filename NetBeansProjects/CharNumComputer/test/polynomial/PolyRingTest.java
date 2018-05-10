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
  PolyRing<Int> highDegrees;
  
  MultiDegree.Builder mb = new MultiDegree.Builder();
  
  public PolyRingTest() {
    Int ring = new Int(0);
    groundRing = new PolyRing<>(ring, 0);
    twoVars    = new PolyRing<>(ring, 2);
    mb.setVars(3).set(0,2).set(1,3).set(2,4);
    truncated  = new PolyRing<>(ring, mb.build());
    mb.setVars(2);
    highDegrees = new PolyRing<>(ring, 
                                    mb.set(0,2).set(1,3).build(),
                                    mb.maxAll().build());
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
    PolyRing.Element zero2 = groundRing.makeElement();
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
    PolyRing.Element one2 = twoVars.makeElement(mb.zero().setVars(2).build(),
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
    PolyRing.Element p = twoVars.makeElement(v0v1, twoVars.cRing().one());
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
    PolyRing.Element p = truncated.makeElement(mu, new Int(2));
    PolyRing.Element pSquared = truncated.multiply(p, p);
    MultiDegree muSquared = MultiDegree.raise(mu);
    assert(pSquared.get(muSquared).equals(new Int(4)));
    PolyRing.Element pCubed = truncated.multiply(p, pSquared);
    assert(pCubed.isZero());
  }

  /**
   * Test of truncation method, of class PolyRing.
   */
  @Test
  public void testTruncation() {
    System.out.println("truncation");
    MultiDegree expResult = mb.setVars(3).set(0,2).set(1,3).set(2,4).build();
    assert(truncated.truncation().equals(expResult));
  }

  /**
   * Test of variables method, of class PolyRing.
   */
  @Test
  public void testVariables() {
    System.out.println("variables");
    MultiDegree expResult = mb.setVars(2).set(0,2).set(1,3).build();
    assert(highDegrees.variables().equals(expResult));
  }

  /**
   * Test of vars method, of class PolyRing.
   */
  @Test
  public void testVars() {
    System.out.println("vars");
    assertEquals(twoVars.vars(), 2);
    assertEquals(groundRing.vars(), 0);
  }

  /**
   * Test of makeElement method, of class PolyRing.
   */
  @Test
  public void testMakeElement_0args() {
    System.out.println("makeElement");
    assert(groundRing.makeElement().isZero());
    assert(twoVars.makeElement().isZero());
    assert(truncated.makeElement().isZero());
    assert(highDegrees.makeElement().isZero());
  }

  /**
   * Test of makeElement method, of class PolyRing.
   */
  @Test
  public void testMakeElement_MultiDegree_GenericType() {
    System.out.println("makeElement");
    Int one = Int.ring.one();
    assert(groundRing.makeElement(MultiDegree.empty(), one)
            .equals(groundRing.one()));
    assert(twoVars.makeElement(mb.setVars(2).zero().build(), one)
            .equals(twoVars.one()));
    assert(truncated.makeElement(mb.setVars(3).zero().build(), one)
            .equals(truncated.one()));
    assert(highDegrees.makeElement(mb.setVars(2).zero().build(), one)
            .equals(highDegrees.one()));
  }

  /**
   * Test of makeElement method, of class PolyRing.
   */
  @Test
  public void testMakeElement_PolyRingElement() {
    System.out.println("makeElement");
    PolyRing<Int>.Element p = twoVars.makeElement(
            mb.setVars(2).set(0,2).set(0,4).build(), 
            new Int(7));
    assert(p.equals(twoVars.makeElement(p)));
  }
 
  /**
   * Test of getHomogeneousParts method, of class PolyRing.Element
   */
  @Test
  public void testGetHomogeneousParts() {
    
    MultiDegree uv = mb.setVars(2).set(0,1).set(1,1).build();
    assertEquals(uv.total(), 2);
    MultiDegree u2v3 = mb.set(0,2).set(1,3).build();
    assertEquals(u2v3.total(), 5);
    
    PolyRing<Int>.Element a = twoVars.makeElement(uv, new Int(2));
    PolyRing<Int>.Element b = twoVars.makeElement(u2v3, new Int(5));
    PolyRing<Int>.Element c = twoVars.add(a, b);
    PolyRing<Int>.Element minusA = twoVars.makeElement(uv, new Int(-2));
    PolyRing<Int>.Element d = twoVars.add(c, minusA);
    assert(d.equals(b));
    
    assert(!c.getHomogeneousParts().isEmpty());
    
    PolyRing<Int>.Element degree2 = c.getHomogeneousParts().get(2);
    assert(degree2.equals(a));
    PolyRing<Int>.Element degree5 = c.getHomogeneousParts().get(5);
    assert(degree5.equals(b));
  }

  /**
   * Test of negative method, of class PolyRing.
   */
  @Test
  public void testNegative() {
    System.out.println("negative");
    PolyRing<Int>.Element p = twoVars.one();
    MultiDegree d = mb.setVars(2).set(0,1).set(1,2).build();
    p = twoVars.add(p, twoVars.makeElement(d, new Int(-5)));
    PolyRing<Int>.Element minusP = twoVars.negative(p);
    assertEquals(minusP.get(d).value(), 5);
    assert(twoVars.add(p, minusP).isZero());
  }

  /**
   * Test of subtract method, of class PolyRing.
   */
  @Test
  public void testSubtract() {
    System.out.println("subtract");
    PolyRing<Int>.Element p = twoVars.one();
    MultiDegree d = mb.setVars(2).set(0,1).set(1,2).build();
    p = twoVars.add(p, twoVars.makeElement(d, new Int(-5)));
    assert(twoVars.subtract(p, p).isZero());
  }
}
