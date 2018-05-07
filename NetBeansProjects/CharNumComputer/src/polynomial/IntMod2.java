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
 * IntMod2 is a Coefficient representation of Z/2. 
 * @author William Gollinger
 */
public class IntMod2 extends Coefficient<IntMod2> {
  
  /**
   * A static instance of IntMod2.
   */
  static IntMod2 ring = new IntMod2();
  
  private final boolean value;
  
  /**
   * Constructs an IntMod2 with the mod-2 residue of the value.
   * @param intValue 
   */
  public IntMod2(int intValue) {
    super(ring);
    this.value = (intValue % 2 == 1);
  }
  /**
   * Constructs an IntMod2 with the given boolean.
   * @param value 
   */
  public IntMod2(boolean value) {
    super(ring);
    this.value = value;
  }
  /**
   * The default IntMod2 is 0.
   */
  public IntMod2() {
    this(0);
  }
  
  
  /**
   * Returns the int value of IntMod2.
   * @return 
   */
  public int value() {return value ? 1 : 0;}
  
  @Override
  public String toString() {
    return String.valueOf(value());
  }
  /**
   * Two IntMod2s are equal iff they have the same value.
   * @param o
   * @return 
   */
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
  
  /**
   * An IntMod2 is zero iff its value is false.
   * @return 
   */
  @Override
  public boolean isZero() {
    return (value == false);
  }
  /**
   * Returns an IntMod2 whose value is a % 2.
   * @param a
   * @return 
   */
  @Override
  public IntMod2 intToCoefficient(int a) {
    return new IntMod2(a);
  }
  
  /**
   * Returns an IntMod2 equal to 0.
   * @return 
   */
  @Override
  public IntMod2 zero() {
    return new IntMod2(0);
  }
  /**
   * Returns an IntMod2 equal to 1.
   * @return 
   */
  @Override
  public IntMod2 one() {
    return new IntMod2(1);
  }
  @Override
  public IntMod2 plus(IntMod2 b) {
    return new IntMod2((value != b.value));
  }
  /**
   * Returns an IntMod2 equals to the sum of this with b.
   * @param b
   * @return 
   */
  @Override
  public IntMod2 times(IntMod2 b) {
    return new IntMod2(value && b.value);
  }
  
}
