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
 *
 * @author William Gollinger
 */
public class Int extends Integers {
  
  private final int value;
  
  public static Int ring = new Int();
  
  private Int () {
    this(0);
  }
  public Int(int value) {
    super(ring);
    this.value = value;
  }
  
  public int value() {return value;}
  static Int valueOf(int n) {return new Int(n);}
  
  @Override
  public String toString() {
    return String.valueOf(value);
  }
  
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
  
  @Override
  public boolean isZero() {
    return (value == 0);
  }
  @Override
  public Int intToCoefficient(int a) {
    return new Int(a);
  }
  @Override
  public Int mod(Integers b) {
    if (b instanceof Int) return new Int(value % ((Int)b).value);
    throw new IllegalArgumentException();
  }
  
  @Override
  public Int zero() {
    return new Int(0);
  }
  @Override
  public Int one() {
    return new Int(1);
  }
  @Override
  public Int plus(Coefficient b) {
    if (!(b instanceof Int)) throw new IllegalArgumentException();
    return new Int(value + ((Int)b).value);
  }
  @Override
  public Int times(Coefficient b) {
    if (!(b instanceof Int)) throw new IllegalArgumentException();
    return new Int(value * ((Int)b).value);
  }
}
