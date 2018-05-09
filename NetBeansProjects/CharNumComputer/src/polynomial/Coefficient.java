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
 * The Coefficient abstract class is used as a generic parameter in 
 * polynomial rings.  An instance of Coefficient has the ring operations
 * plus and times, and has methods for constructing 0 and 1.
 * Since Coefficient is unital, there is an intToCoefficient method 
 * representing the unit map.
 * @author William Gollinger
 * @param <C>
 */
public abstract class Coefficient<C extends Coefficient<C>> {
  
  /**
   * Requiring an instance of C in the constructor forces subclasses
   * to provide a static way of obtaining an instance of that type.
   * This is typically done with a static field named "ring".
   * @param ring 
   */
  protected Coefficient(C ring) {
  }
  
  
  /**
   * Two Coefficients are equal iff they are the same type,
   * and their values are equal in the appropriate sense.
   * @param o
   * @return 
   */
  @Override
  public abstract boolean equals(Object o);
  @Override
  public abstract int hashCode();
  @Override
  public abstract String toString();
  
  public abstract boolean isZero();
  public abstract boolean isOne();
  public abstract C intToCoefficient(int a);
  
  public abstract C zero();
  public abstract C one();
  public abstract C plus(C b);
  public abstract C times(C b);  
  
}
