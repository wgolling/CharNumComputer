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
public class IntMod2Test {
  IntMod2 zero;
  IntMod2 one;
  public IntMod2Test() {
    zero = new IntMod2(4);
    one = new IntMod2(5);
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of value method, of class IntMod2.
   */
  @Test
  public void testValue() {
    System.out.println("value");
    assertEquals(0, zero.value());
    assertEquals(1, one.value());
  }

  /**
   * Test of valueOf method, of class IntMod2.
   */
  @Test
  public void testValueOf() {
    System.out.println("valueOf");
    IntMod2 bit = new IntMod2(3);
    assertEquals(bit.value(), 1);
  }

  /**
   * Test of toString method, of class IntMod2.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    assert(zero.toString().equals("0"));
  }

  /**
   * Test of equals method, of class IntMod2.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    zero.equals(new IntMod2(0));
    zero.equals(new IntMod2(new BigInt(4)));
    one.equals(new IntMod2(1));
    one.equals(new IntMod2(new BigInt(7)));
  }

  /**
   * Test of hashCode method, of class IntMod2.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
  }

  /**
   * Test of isZero method, of class IntMod2.
   */
  @Test
  public void testIsZero() {
    System.out.println("isZero");
    assert(zero.isZero());
    assert(!one.isZero());
  }

  /**
   * Test of intToCoefficient method, of class IntMod2.
   */
  @Test
  public void testIntToCoefficient() {
    System.out.println("intToCoefficient");
    IntMod2 eight = IntMod2.ring.intToCoefficient(8);
    assert(eight.isZero());
  }

  /**
   * Test of zero method, of class IntMod2.
   */
  @Test
  public void testZero() {
    System.out.println("zero");
    assert(IntMod2.ring.zero().isZero());
  }

  /**
   * Test of one method, of class IntMod2.
   */
  @Test
  public void testOne() {
    System.out.println("one");
    assert(!IntMod2.ring.one().isZero());
  }

  /**
   * Test of plus method, of class IntMod2.
   */
  @Test
  public void testPlus() {
    System.out.println("plus");
    assert(zero.plus(zero).equals(zero));
    assert(zero.plus(one).equals(one));
    assert(one.plus(zero).equals(one));
    assert(one.plus(one).equals(zero));
  }

  /**
   * Test of times method, of class IntMod2.
   */
  @Test
  public void testTimes() {
    System.out.println("times");
    assert(zero.times(zero).equals(zero));
    assert(zero.times(one).equals(zero));
    assert(one.times(zero).equals(zero));
    assert(one.times(one).equals(one));
  }
  
}
