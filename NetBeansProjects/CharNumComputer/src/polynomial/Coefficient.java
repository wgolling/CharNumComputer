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
 * @param <C>
 */
public abstract class Coefficient<C extends Coefficient<C>> {
  
  private final C ring;
  
  protected Coefficient(C ring) {
    this.ring = ring;
  }
  
  public C ring() {
    return ring;
  }
  
  @Override
  public abstract boolean equals(Object o);
  @Override
  public abstract int hashCode();
  @Override
  public abstract String toString();
  
  public abstract boolean isZero();
  public abstract C intToCoefficient(int a);
  
  public abstract C zero();
  public abstract C one();
  public abstract C plus(C b);
  public abstract C times(C b);  
}
