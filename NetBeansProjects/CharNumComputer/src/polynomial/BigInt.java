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

/**
 * BigInt is a Coefficient wrapper around BigInteger.
 * @author William Gollinger
 */
public class BigInt extends Coefficient<BigInt> {
  
  /** 
   * A static instance of BigInt.
   */
  public static BigInt ring = new BigInt();
  
  private BigInteger value;

  
  /*
  Constructors.
  */
  
  /**
   * The default BigInt has value 0;
   */
  protected BigInt() {
    this(BigInteger.ZERO);
  }
  /**
   * Constructs a BigInt with the given value.
   * @param value 
   */
  public BigInt(BigInteger value) {
    super(ring);
    this.value = value;
  }
  /**
   * Constructs a BigInt with the given int value converted to BigInteger.
   * @param value 
   */
  public BigInt(int value) {
    super(ring);
    this.value = BigInteger.valueOf(value);
  }
  
  
  /*
  Methods specific to BigInt.
  */
  
  
  /**
   * Returns the BigInteger value.
   * @return 
   */
  public BigInteger value() {
    return value;
  }
  /**
   * Returns the (possibly truncated) int value.
   * @return 
   */
  public int intValue() {
    return value.intValue();
  }
  
  /**
   * Returns the remainder of dividing by another BigInt.
   * @param b
   * @return 
   */
  public BigInt mod(BigInt b) {
    if (b.value.equals(BigInteger.ZERO)) 
      throw new IllegalArgumentException();
    return new BigInt(value.mod(b.value));
  }
  
  
  /*
  Implementation
  */
  
  /**
   * Two BigInts are equal iff their BigInteger values are equal.
   * @param o
   * @return 
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof BigInt) {
      return value.equals(((BigInt)o).value);
    }
    return false;
  }
  @Override
  public int hashCode() {
    return value.hashCode();
  }
  @Override
  public String toString() {
    return value.toString();
  }
  /**
   * A BigInt is zero iff its value is BigInteger.ZERO.
   * @return 
   */
  @Override
  public boolean isZero() {
    return (value == BigInteger.ZERO);
  }
  /**
   * A BigInt is zero iff its value is BigInteger.ONE.
   * @return 
   */
  @Override
  public boolean isOne() {
    return (value == BigInteger.ONE);
  }
  /**
   * Converts an int to a BigInt.
   * @param a
   * @return 
   */
  @Override
  public BigInt intToCoefficient(int a) {
    return new BigInt(BigInteger.valueOf(a));
  }
  /**
   * Returns a BigInt equal to 0.
   * @return 
   */
  @Override
  public BigInt zero() {
    return new BigInt(BigInteger.ZERO);
  }
  /**
   * Returns a BigInt equal to 1.
   * @return 
   */
  @Override
  public BigInt one() {
    return new BigInt(BigInteger.ONE);
  }
  /**
   * Returns a new BigInt which is the sum of this with b.
   * @param b
   * @return 
   */
  @Override
  public BigInt plus(BigInt b) {
    return new BigInt(value.add(b.value));
  }
  /**
   * Returns a new BigInt which is the product of this with b.
   * @param b
   * @return 
   */
  @Override
  public BigInt times(BigInt b) {
    return new BigInt(value.multiply(b.value));
  }
  /**
   * Returns a BigInt with the negative value.
   * @param b
   * @return 
   */
  @Override
  public BigInt negative(BigInt b) {
    return new BigInt(b.value.negate());
  }
}
