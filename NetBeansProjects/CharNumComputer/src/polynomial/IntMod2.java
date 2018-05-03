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
public class IntMod2 extends Coefficient {
  
  
  static IntMod2 ring = new IntMod2();
  private final boolean value;
  
  public IntMod2(int intValue) {
    super(ring);
    this.value = (intValue % 2 == 1);
  }
  public IntMod2(boolean value) {
    super(ring);
    this.value = value;
  }
  public IntMod2() {
    this(0);
  }
  
  
  public int value() {return value ? 1 : 0;}
  static IntMod2 valueOf(int n) {return new IntMod2(n);}
  
  @Override
  public String toString() {
    return String.valueOf(value());
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof IntMod2) {
      return (value == ((IntMod2)o).value);
    }
    return false;
  }
  @Override
  public int hashCode() {
    return value();
  }
  
  @Override
  public boolean isZero() {
    return (value == false);
  }
  @Override
  public IntMod2 intToCoefficient(int a) {
    return valueOf(a);
  }
  
  @Override
  public IntMod2 zero() {
    return new IntMod2(0);
  }
  @Override
  public IntMod2 one() {
    return new IntMod2(1);
  }
  @Override
  public IntMod2 plus(Coefficient b) {
    if (!(b instanceof IntMod2)) throw new IllegalArgumentException();
    return new IntMod2((value != ((IntMod2)b).value));
  }
  @Override
  public IntMod2 times(Coefficient b) {
    if (!(b instanceof IntMod2)) throw new IllegalArgumentException();
    return new IntMod2(value && ((IntMod2)b).value);
  }
  
}