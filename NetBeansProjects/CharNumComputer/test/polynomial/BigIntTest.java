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
public class BigIntTest {
  
  public BigIntTest() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of value method, of class BigInt.
   */
  @Test
  public void testValue() {
    System.out.println("value");
    BigInt one = new BigInt(1);
    assert(one.value().equals(BigInteger.ONE));
  }

  /**
   * Test of equals method, of class BigInt.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    BigInt six = new BigInt(6);
    BigInt sixy = new BigInt(BigInteger.valueOf(6));
    assert(six.equals(sixy));
  }

  /**
   * Test of hashCode method, of class BigInt.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
  }

  /**
   * Test of zero method, of class BigInt.
   */
  @Test
  public void testZero() {
    System.out.println("zero");
    BigInt zero = (BigInt)BigInt.ring.zero();
    assert(zero.equals(new BigInt(0)));
  }

  /**
   * Test of one method, of class BigInt.
   */
  @Test
  public void testOne() {
    System.out.println("one");
    BigInt one = (BigInt)BigInt.ring.one();
    assert(one.equals(new BigInt(1)));
  }

  /**
   * Test of plus method, of class BigInt.
   */
  @Test
  public void testPlus() {
    System.out.println("plus");
    BigInt four = new BigInt(4);
    BigInt twelve = new BigInt(12);
    assert(four.plus(twelve).equals(new BigInt(16)));
  }

  /**
   * Test of times method, of class BigInt.
   */
  @Test
  public void testTimes() {
    System.out.println("times");
    BigInt four = new BigInt(4);
    BigInt twelve = new BigInt(12);
    assert(four.times(twelve).equals(new BigInt(48)));
  }

  /**
   * Test of toString method, of class BigInt.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    BigInt six = new BigInt(6);
    assert(six.toString().equals("6"));

  }

  /**
   * Test of isZero method, of class BigInt.
   */
  @Test
  public void testIsZero() {
    System.out.println("isZero");
    assert(BigInt.ring.zero().isZero());
  }

  /**
   * Test of intToCoefficient method, of class BigInt.
   */
  @Test
  public void testIntToCoefficient() {
    System.out.println("intToCoefficient");
    assert(BigInt.ring.intToCoefficient(0).isZero());
  }

  /**
   * Test of mod method, of class BigInt.
   */
  @Test
  public void testMod() {
    System.out.println("mod");
    BigInt seven = new BigInt(7);
    BigInt three = new BigInt(3);
    BigInt mod = seven.mod(three);
    assert(mod.equals(new BigInt(1)));
  }
  
}
