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
 * HP models the quaternionic projective space HP(n), whose real dimension is 4n.
 * HP(n) is never complex, so in particular there is no Chern class or numbers.
 * 
 * Its cohomology is
 *  H^*(HP(n) ; Z) = Z[u]/<u^{n+1}>  where u has degree 4.
 *  H^*(HP(n) ; Z/2) = (Z/2)[u]/<u^{n+1}>
 * 
 * Its Stiefel-Whitney class is easily described as
 *  w(HP(n)) = (1 + u)^{n+1} 
 *           = sum_{i=0}^n binom(n+1, i) u^i  mod 2
 * 
 * Its Pontryagin class is
 *  p(HP(n) = (1 + u)^{2n+2} x (1 + 4u)^{-1}
 * where
 *  (1 - x)^{-1} = 1 + x + x^2 + x^3 + ...
 * Since u is truncated, (1 + 4u)^{-1} has a finite expression, and so
 *  p(HP(n) = (sum_{i=0}^n binom(2n+2, i) u^i) x (sum_{i=0}^n (-4)^i u^i)
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
    // set dimension
    p.rDim = 4 * n;
    p.isComplex = false;
    // make polynomial rings
    MultiDegree.Builder mb = new MultiDegree.Builder(1);
    p.mu = mb.set(0, p.rDim).build();
    MultiDegree variables = mb.set(0, 4).build();
    p.cohomology     = new PolyRing<>(BigInt.ring,  variables, p.mu);
    p.mod2Cohomology = new PolyRing<>(IntMod2.ring, variables, p.mu);
    // compute characteristic classes
    p.chernClass = null;
    setCharClasses(p, n, mb);
    return p;
  }

  /**
   * setCharClasses implements the formulae for 
   * the Pontryagin and Stiefel-Whitney classes of HP(n)
   * @param p
   * @param n
   * @param mb 
   */
  private static void setCharClasses(Properties p, int n, MultiDegree.Builder mb) {
    
    PolyRing<BigInt>.Element pontLeft  = p.cohomology.zero();                // represents the (1 + u)^{2n+1} term of pontClass
    PolyRing<BigInt>.Element pontRight = p.cohomology.zero();                // represents the (1 + 4u)^{-1} term of pontClass
    PolyRing<IntMod2>.Element sw       = p.mod2Cohomology.zero();
    
    mb.setVars(1).zero();
    int powerOfMinus4 = 1;                                                   // used to compute the coefficients in pontRight
    
    for (int i = 0; i < n + 1; i++) {
      MultiDegree d = mb.set(0, 4 * i).build();
      // compute (1 + u)^{2n+2}
      pontLeft = p.cohomology.add(
              pontLeft, 
              p.cohomology.makeElement(
                      d, 
                      new BigInt(CP.binomial((2 * n) + 2, i))));
      // compute (1 + 4u)^{-1}
      pontRight = p.cohomology.add(
              pontRight, 
              p.cohomology.makeElement(
                      d, 
                      new BigInt(powerOfMinus4)));
      powerOfMinus4 *= -4;
      // compute (1 + u)^{n+1}  mod 2
      sw = p.mod2Cohomology.add(
              sw, 
              p.mod2Cohomology.makeElement(
                      d, 
                      new IntMod2(CP.binomial(n + 1, i).intValue())));
    }
    p.pontClass = p.cohomology.multiply(pontLeft, pontRight);
    p.swClass = sw;
  }
  
  
  /*
  Utility methods.
  */
  
  @Override
  public String toString() {
    return "HP(" + (super.p.rDim / 4) + ")";
  }
}
