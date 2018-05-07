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
public class IntTest {
  
  public IntTest() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of value method, of class Int.
   */
  @Test
  public void testValue() {
    System.out.println("value");
    Int zero = Int.ring.zero();
    Int one  = Int.ring.one();
    assertEquals(zero.value(), 0);
    assertEquals(one.value(), 1);
  }

  /**
   * Test of toString method, of class Int.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    Int six = new Int(6);
    assert(six.toString().equals("6"));
  }

  /**
   * Test of plus method, of class Int.
   */
  @Test
  public void testPlus() {
    System.out.println("plus");
    Int four = new Int(4);
    Int twelve = new Int(12);
    assertEquals(four.plus(twelve).value(), 16);
  }

  /**
   * Test of times method, of class Int.
   */
  @Test
  public void testTimes() {
    System.out.println("times");
    Int four = new Int(4);
    Int twelve = new Int(12);
    assertEquals(four.times(twelve).value(), 48);
  }

  /**
   * Test of valueOf method, of class Int.
   */
  @Test
  public void testValueOf() {
    System.out.println("valueOf");
  }

  /**
   * Test of equals method, of class Int.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    Int four = new Int(4);
    Int twelve = new Int(12);
    assert(four.plus(twelve).equals(new Int(16)));
  }

  /**
   * Test of hashCode method, of class Int.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
    assertEquals(new Int(6).value(), 6);
  }

  /**
   * Test of zero method, of class Int.
   */
  @Test
  public void testZero() {
    System.out.println("zero");
    assert(Int.ring.zero()).equals(new Int(0));
  }

  /**
   * Test of one method, of class Int.
   */
  @Test
  public void testOne() {
    System.out.println("one");
    assert(Int.ring.one()).equals(new Int(1));
  }

  /**
   * Test of isZero method, of class Int.
   */
  @Test
  public void testIsZero() {
    System.out.println("isZero");
    assert(Int.ring.zero().isZero());
  }

  /**
   * Test of intToCoefficient method, of class Int.
   */
  @Test
  public void testIntToCoefficient() {
    System.out.println("intToCoefficient");
    Int.ring.intToCoefficient(6).equals(new Int(6));
  }

  /**
   * Test of mod method, of class Int.
   */
  @Test
  public void testMod() {
    System.out.println("mod");
    Int seven = new Int(7);
    Int three = new Int(3);
    Int mod = seven.mod(three);
    assert(mod.equals(new Int(1)));
  }
  
}
