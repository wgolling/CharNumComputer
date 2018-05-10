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
package manifold;

import polynomial.*;

/**
 *
 * @author William Gollinger
 */
public class HP extends Manifold {
  
  public HP(int n) {
    super(makeProperties(n));
  }
  
  private static Properties makeProperties(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("Dimension must be non-negative.");
    }
    Properties p = new Properties();
    p.rDim = 4 * n;
    p.isComplex = false;
    MultiDegree.Builder mb = new MultiDegree.Builder(1);
    p.mu = mb.set(0, p.rDim).build();
    MultiDegree variables = mb.set(0, 4).build();
    p.cohomology     = new PolyRing<>(BigInt.ring,  variables, p.mu);
    p.mod2Cohomology = new PolyRing<>(IntMod2.ring, variables, p.mu);
    p.chernClass = null;
    setCharClasses(p, n, mb);
    return p;
  }

  private static void setCharClasses(Properties p, int n, MultiDegree.Builder mb) {
    PolyRing<BigInt>.Element pontLeft  = p.cohomology.zero();
    PolyRing<BigInt>.Element pontRight = p.cohomology.zero();
    PolyRing<IntMod2>.Element sw       = p.mod2Cohomology.zero();
    mb.setVars(1).zero();
    int powerOfMinus4 = 1;
    for (int i = 0; i < n + 1; i++) {
      MultiDegree d = mb.set(0, 4 * i).build();
      pontLeft = p.cohomology.add(
              pontLeft, 
              p.cohomology.makeElement(
                      d, 
                      new BigInt(CP.binomial((2 * n) + 2, i))));
      pontRight = p.cohomology.add(
              pontRight, 
              p.cohomology.makeElement(
                      d, 
                      new BigInt(powerOfMinus4)));
      powerOfMinus4 *= -4;
      sw = p.mod2Cohomology.add(
              sw, 
              p.mod2Cohomology.makeElement(
                      d, 
                      new IntMod2(CP.binomial(n + 1, i).intValue())));
    }
    p.pontClass = p.cohomology.multiply(pontLeft, pontRight);
    p.swClass = sw;
  }
  
  @Override
  public String toString() {
    return "HP(" + (super.p.rDim / 4) + ")";
  }
}
