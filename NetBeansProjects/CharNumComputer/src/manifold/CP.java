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
import java.math.*;

/**
 * Models the complex projective space CP(n).
 * 
 * CP(n) is a complex manifold with dimension n and has real dimension 2n.
 * 
 * The integral and mod-2 cohomology rings are simple polynomial rings:
 *    H^*(CP(n); Z)   =     Z[u]/<u^n+1>   where u has degree 2.
 *    H^*(CP(n); Z/2) = (Z/2)[u]/<u^n+1> = tensor(H^*(CP(n); Z), Z/2).
 * 
 * The Chern and Pontryagin classes are given by
 *    c(CP(n)) = (1 + u)^n+1   
 *             = sum_{i=0}^n binomial(n+1, i) u^i
 *    p(CP(n)) = (1 + u^2)^n+1 
 *             = sum_{i=0}^{floor(n/2)} binomial(n+1, i) u^2i
 * The Stiefel-Whitney class is the mod-2 reduction of the Chern class.
 * 
 * @author William Gollinger
 */
public class CP extends Manifold {
  
  /**
   * Constructs a complex projective space with complex dimension n
   * (and therefore real dimension 2n).
   * @param n 
   */
  public CP(int n) {
    super(makeProperties(n));
  }
  
  private static Properties makeProperties(int n) {
    if (n < 0) 
      throw new IllegalArgumentException("Dimension must be non-negative.");
    Properties p = new Properties();
    // set dimensions
    p.rDim = 2 * n;
    p.isComplex = true;
    p.cDim = n;
    // construct helpful multidegrees
    MultiDegree.Builder mb = new MultiDegree.Builder(1);
    MultiDegree u = mb.set(0, 2).build();
    p.mu = mb.set(0, 2 * n).build();
    // set cohomology
    p.cohomology     = new PolyRing<>(BigInt.ring, u, p.mu);
    p.mod2Cohomology = new PolyRing<>(IntMod2.ring, u, p.mu);
    // set characteristic classes
    computeCharClasses(p);
    return p;
  }

  
  /**
   * Computes the characteristic classes with the standard formulae.
   * @param p
   */
  private static void computeCharClasses(Properties p) {
    // compute Chern class and Pontryagin class at the same time
    PolyRing<BigInt>.Element pontClass  = p.cohomology.zero();
    PolyRing<BigInt>.Element chernClass = p.cohomology.zero();
    MultiDegree.Builder mb = new MultiDegree.Builder(1);
    int bound = p.rDim / 4;                                                    // Integer division is being use here, so it's really floor(rDim/4).
    for (int i = 0; i < p.cDim + 1; i++) {
      BigInt b = new BigInt(binomial(p.cDim + 1, i));
      // computes (1 + u)^{n+1}
      chernClass = p.cohomology.add(
              chernClass, 
              p.cohomology.makeElement(mb.set(0, 2 * i).build(), b));
      // computes (1 + u^)^{n+1}
      if (i <= bound) {
        pontClass = p.cohomology.add(
                pontClass, 
                p.cohomology.makeElement(mb.set(0, 4 * i).build(), b));
      }
    }
    p.pontClass   = pontClass;
    p.chernClass  = chernClass;
    p.swClass     = reduceMod2(chernClass, p);
  }
  /**
   * Returns "n choose k", that is
   * the number of subsets of size k in a set of size n. 
   * @param n
   * @param k
   * @return 
   */
  protected static BigInteger binomial(int n, int k) {
    if (n < 0 || n < k) {
      return BigInteger.ZERO;
    }
    //   * Got this from StackOverflow
    // TODO cite better or write own version
    if (k > n - k)
      k = n - k;

    long b = 1;
    for (int i = 1, m = n; i <= k; i++, m--)
      b = b * m / i;
    return BigInteger.valueOf(b);
  }

  
  /*
  Utility methods.
  */
  
  @Override
  public String toString() {
    return String.format("CP(%d)", super.p.cDim);
  }
  
  
}