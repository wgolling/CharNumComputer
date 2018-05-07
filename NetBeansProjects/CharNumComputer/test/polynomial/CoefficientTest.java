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

import java.math.BigInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class CoefficientTest {
  
  Coefficient a;
  Coefficient b;
  public CoefficientTest() {
    a = new Int(6);
    b = new BigInt(6);
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of equals method, of class Coefficient.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    assert(!a.equals(b));
  }

  /**
   * Test of hashCode method, of class Coefficient.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
    assertEquals(a.hashCode(), Integer.valueOf(6).hashCode());
    assertEquals(b.hashCode(), BigInteger.valueOf(6).hashCode());
  }

  /**
   * Test of zero method, of class Coefficient.
   */
  @Test
  public void testZero() {
    System.out.println("zero");
    Coefficient intZero    = Int.ring.zero();
    Coefficient bigIntZero = BigInt.ring.zero();
    assertEquals(((Int)intZero).value(), 0);
    assertEquals(((BigInt)bigIntZero).value(), BigInteger.ZERO);
  }

  /**
   * Test of one method, of class Coefficient.
   */
  @Test
  public void testOne() {
    System.out.println("one");
    Coefficient intOne    = Int.ring.one();
    Coefficient bigIntOne = BigInt.ring.one();
    assertEquals(((Int)intOne).value(), 1);
    assertEquals(((BigInt)bigIntOne).value(), BigInteger.ONE);
  }

  /**
   * Test of plus method, of class Coefficient.
   */
  @Test (expected = ClassCastException.class)
  public void testPlus() {
    System.out.println("plus");
    Coefficient c = new Int(4);
    Coefficient sum = a.plus(c);
    assertEquals(((Int)sum).value(), 10);
    a.plus(b); // throws ClassCastException
  }

  /**
   * Test of times method, of class Coefficient.
   */
  @Test (expected = ClassCastException.class)
  public void testTimes() {
    System.out.println("times");
    Coefficient c = new BigInt(4);
    Coefficient product = b.times(c);
    assertEquals(((BigInt)product).value(), BigInteger.valueOf(24));
    a.times(b); // throws ClassCastException
  }

  /**
   * Test of ring method, of class Coefficient.
   */
  @Test
  public void testRing() {
    System.out.println("ring");
    assert(a.zero().equals(Int.ring.zero()));
    assert(b.zero().equals(BigInt.ring.zero()));
    assert(!a.zero().equals(b.zero()));
  }

  /**
   * Test of toString method, of class Coefficient.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    assert(a.toString().equals(b.toString()));
  }

  /**
   * Test of isZero method, of class Coefficient.
   */
  @Test
  public void testIsZero() {
    System.out.println("isZero");
    assert(a.zero().isZero());
    assert(b.zero().isZero());
  }

  /**
   * Test of intToCoefficient method, of class Coefficient.
   */
  @Test
  public void testIntToCoefficient() {
    System.out.println("intToCoefficient");
    assert(a.equals(a.intToCoefficient(6)));
    assert(b.equals(b.intToCoefficient(6)));
  }

}