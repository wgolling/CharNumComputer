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
    Int zero = Int.maker.zero();
    Int one  = Int.maker.one();
    assertEquals(zero.value(), 0);
    assertEquals(one.value(), 1);
  }

  /**
   * Test of toString method, of class Int.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    Int six = Int.valueOf(6);
    assert(six.toString().equals("6"));
  }

  /**
   * Test of plus method, of class Int.
   */
  @Test
  public void testPlus() {
    System.out.println("plus");
    Int four = Int.valueOf(4);
    Int twelve = Int.valueOf(12);
    assertEquals(four.plus(twelve).value(), 16);
  }

  /**
   * Test of times method, of class Int.
   */
  @Test
  public void testTimes() {
    System.out.println("times");
    Int four = Int.valueOf(4);
    Int twelve = Int.valueOf(12);
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
    Int four = Int.valueOf(4);
    Int twelve = Int.valueOf(12);
    assert(four.plus(twelve).equals(new Int(16)));
  }

  /**
   * Test of hashCode method, of class Int.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
    assertEquals(Int.valueOf(6).value(), 6);
  }

  /**
   * Test of zero method, of class Int.
   */
  @Test
  public void testZero() {
    System.out.println("zero");
  }

  /**
   * Test of one method, of class Int.
   */
  @Test
  public void testOne() {
    System.out.println("one");
  }

  /**
   * Test of checkType method, of class Int.
   */
  @Test
  public void testCheckType() {
    System.out.println("checkType");
  }
  
}
