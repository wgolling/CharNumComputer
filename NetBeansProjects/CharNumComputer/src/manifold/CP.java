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

import lib.*;
import java.util.*;
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
 *    c(CP(n)) = (1 + u)^n+1   = sum_{i=0}^n            binomial(n+1, i) u^i
 *    p(CP(n)) = (1 + u^2)^n+1 = sum_{i=0}^{floor(n/2)} binomial(n+1, i) u^2i
 * The Stiefel-Whitney class is the mod-2 reduction of the Chern class.
 * 
 * @author William Gollinger
 */
public class CP extends Manifold {
  
  private final int rDim;
  private final int cDim;
  private final MultiDegree.Builder mb;
  private final MultiDegree truncation;
  private final Polynomial.Ring cohomology;
  private final Polynomial.Ring mod2Cohomology;
  private final Map<String, Polynomial> charClasses;
  
  /**
   * Constructs a complex projective space with complex dimension n
   * (and therefore real dimension 2n).
   * @param n 
   */
  public CP(int n) {
    if (n < 0) {
      throw new IllegalArgumentException();
    }
    // Set dimensions.
    cDim = n;
    rDim = 2 * n;
    // H^*(CP(n) ; Z) = Z[u]/<u^n+1> where u has degree 2.
    // H^*(CP(n) ; Z/2) = tensor(H^*(CP(n);Z), Z/2) = (Z/2)[u]<u^n+1>.
    mb = new MultiDegree.Builder(1);
    truncation = mb.set(0, rDim).build();
    mb.set(0, 2);
    cohomology     = new Polynomial.Ring(mb.build(), truncation);
    mod2Cohomology = new Polynomial.Ring(mb.build(), truncation, 2);
    // Compute characteristic classes.
    charClasses = new HashMap<>();
    computeCharClasses();
  }
  
  
  /*
  Utility methods.
  */
  
  @Override
  public String toString() {
    return String.format("CP(%d)", cDim);
  }
  
  
  /*
  implementation
  */
  
  @Override
  public int rDim() {
    return rDim;
  }
  @Override
  public boolean isComplex() {
    return true;
  }
  @Override
  public int cDim() {
    return cDim;
  }
  @Override
  public MultiDegree truncation() {
    return truncation.copy();
  }
  @Override
  public Polynomial.Ring cohomology() {
    return cohomology;
  }
  @Override
  public Polynomial.Ring mod2Cohomology() {
    return mod2Cohomology;
  }
  @Override
  public Map<String, Polynomial> getCharClasses() {
    return new HashMap<>(charClasses);
  }
  
  /**
   * Computes the characteristic classes with the standard formulae.
   */
  private void computeCharClasses() {
    // compute Chern class and Pontryagin class at the same time
    // c(CP(n)) = (1 + u)^n+1   = sum_{i=0}^n            binomial(n+1, i) u^i
    // p(CP(n)) = (1 + u^2)^n+1 = sum_{i=0}^{floor(n/2)} binomial(n+1, i) u^2i
    Polynomial chernClass = new Polynomial(1);
    Polynomial pontClass  = new Polynomial(1);
    int bound = rDim / 4;                                                    // Integer division is being use here, so it's really floor(rDim/4).
    for (int i = 0; i < cDim + 1; i++) {
      BigInteger b = binomial(cDim + 1, i);
      chernClass.addMonomial(mb.set(0, 2 * i).build(), b, cohomology);
      if (i <= bound) {
        pontClass.addMonomial(mb.set(0, 4 * i).build(), b, cohomology);
      }
    }
    charClasses.put("chern", chernClass);
    charClasses.put("pont" , pontClass);
    // Reduce the Chern class modulo 2 to get Stiefel-Whitney class.
    charClasses.put("sw", mod2Cohomology.reduce(chernClass));
  }
  /**
   * Returns "n choose k", that is
   * the number of subsets of size k in a set of size n. 
   * @param n
   * @param k
   * @return 
   */
  private static BigInteger binomial(int n, int k) {
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

}
