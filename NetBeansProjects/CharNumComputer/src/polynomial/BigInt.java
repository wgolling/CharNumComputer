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
 *
 * @author William Gollinger
 */
public class BigInt extends Coefficient {
  
  BigInteger value;
  
  static BigInt maker = new BigInt();
  
  private BigInt() {
    this(BigInteger.ZERO);
  }
  public BigInt(BigInteger value) {
    this.value = value;
  }
  
  public BigInteger value() {
    return value;
  }
  static BigInt valueOf(BigInteger a) {
    return new BigInt(a);
  }
  static BigInt valueOf(int a) {
    return new BigInt(BigInteger.valueOf(a));
  }
  
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
  public Coefficient zero() {
    return new BigInt(BigInteger.ZERO);
  }
  @Override
  public Coefficient one() {
    return new BigInt(BigInteger.ONE);
  }
  @Override
  public Coefficient plus(Coefficient b) {
    if (!(b instanceof BigInt)) throw new IllegalArgumentException();
    return new BigInt(value.add(((BigInt)b).value));
  }
  @Override
  public Coefficient times(Coefficient b) {
    if (!(b instanceof BigInt)) throw new IllegalArgumentException();
    return new BigInt(value.multiply(((BigInt)b).value));
  }
}
