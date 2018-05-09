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

/**
 * Int is a Coefficient wrapper around int.
 * @author William Gollinger
 */
public class Int extends Coefficient<Int> {
  
  /**
   * A static instance of Int.
   */
  public static Int ring = new Int();
  
  private final int value;
  
  /**
   * The default constructor returns an Int with value 0.
   */
  protected Int() {
    this(0);
  }
  /**
   * Constructs an Int with the given value.
   * @param value 
   */
  public Int(int value) {
    super(ring);
    this.value = value;
  }
  
  /**
   * Returns the int value.
   * @return 
   */
  public int value() {
    return value;
  }
  
  @Override
  public String toString() {
    return String.valueOf(value);
  }
  
  /**
   * Two Ints are equal iff their values are the same.
   * @param o
   * @return 
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Int) {
      return (value == ((Int)o).value);
    }
    return false;
  }
  @Override
  public int hashCode() {
    return value;
  }
  
  /**
   * An Int is zero iff its value is 0.
   * @return 
   */
  @Override
  public boolean isZero() {
    return (value == 0);
  }
  /**
   * An Int is zero iff its value is 1.
   * @return 
   */
  @Override
  public boolean isOne() {
    return (value == 1);
  }
  /**
   * Returns an Int with the given value.
   * @param a
   * @return 
   */
  @Override
  public Int intToCoefficient(int a) {
    return new Int(a);
  }
  /**
   * Returns the remainder of division by another Int.
   * @param b
   * @return 
   */
  public Int mod(Int b) {
    if (b.value == 0) 
      throw new IllegalArgumentException();
    return new Int(value % b.value);
  }
  
  /**
   * Returns an Int equal to 0.
   * @return 
   */
  @Override
  public Int zero() {
    return new Int(0);
  }
  /**
   * Returns and Int equal to 1.
   * @return 
   */
  @Override
  public Int one() {
    return new Int(1);
  }
  /**
   * Returns an Int equal to the sum of this and b.
   * @param b
   * @return 
   */
  @Override
  public Int plus(Int b) {
    return new Int(value + b.value);
  }
  /**
   * Returns an Int equal to the product of this and b.
   * @param b
   * @return 
   */
  @Override
  public Int times(Int b) {
    return new Int(value * b.value);
  }
}
