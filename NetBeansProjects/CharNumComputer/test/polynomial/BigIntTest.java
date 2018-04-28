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
    BigInt one = BigInt.valueOf(1);
    assert(one.value.equals(BigInteger.ONE));
  }

  /**
   * Test of valueOf method, of class BigInt.
   */
  @Test
  public void testValueOf_BigInteger() {
    System.out.println("valueOf");
    BigInteger six = BigInteger.valueOf(6);
    assert(BigInt.valueOf(six).value.equals(six));
  }

  /**
   * Test of valueOf method, of class BigInt.
   */
  @Test
  public void testValueOf_int() {
    System.out.println("valueOf");
    BigInt six = BigInt.valueOf(6);
    assert(six.value.equals(BigInteger.valueOf(6)));
  }

  /**
   * Test of equals method, of class BigInt.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    BigInt six = BigInt.valueOf(6);
    BigInt sixy = BigInt.valueOf(BigInteger.valueOf(6));
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
    BigInt zero = (BigInt)BigInt.maker.zero();
    assert(zero.equals(BigInt.valueOf(0)));
  }

  /**
   * Test of one method, of class BigInt.
   */
  @Test
  public void testOne() {
    System.out.println("one");
    BigInt one = (BigInt)BigInt.maker.one();
    assert(one.equals(BigInt.valueOf(1)));
  }

  /**
   * Test of plus method, of class BigInt.
   */
  @Test
  public void testPlus() {
    System.out.println("plus");
    BigInt four = BigInt.valueOf(4);
    BigInt twelve = BigInt.valueOf(12);
    assert(four.plus(twelve).equals(BigInt.valueOf(16)));
  }

  /**
   * Test of times method, of class BigInt.
   */
  @Test
  public void testTimes() {
    System.out.println("times");
    BigInt four = BigInt.valueOf(4);
    BigInt twelve = BigInt.valueOf(12);
    assert(four.times(twelve).equals(BigInt.valueOf(48)));
  }
  
}
