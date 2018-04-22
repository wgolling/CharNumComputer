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

import lib.Polynomial;
import lib.MultiDegree;
import java.util.*;
import java.math.*;
import charnumcomputer.*;

/**
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
  
  Map<String, Polynomial> charClasses;
  
  public CP(int n) {
    if (n < 0) {
      throw new IllegalArgumentException();
    }
    cDim = n;
    rDim = 2 * n;
    mb = new MultiDegree.Builder(1);
    truncation = mb.set(0, rDim).build();
    cohomology     = new Polynomial.Ring(mb.setAll(2).build(), truncation);
    mod2Cohomology = new Polynomial.Ring(mb.setAll(2).build(), truncation, 2);
    charClasses = new HashMap<>();
    computeCharClasses();
  }
  
  @Override
  public String toString() {
    return String.format("CP(%d)", cDim);
  }
  
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
  
  private void computeCharClasses() {
    // compute Chern class and Pontryagin class at the same time
    charClasses.put("chern", new Polynomial(1));
    Polynomial chernClass = charClasses.get("chern");
    charClasses.put("pont", new Polynomial(1));
    Polynomial pontClass = charClasses.get("pont");
    
    int bound = (int)Math.floor((float)rDim / 4);
    for (int i = 0; i < cDim + 1; i++) {
      BigInteger b = binomial(cDim + 1, i);
      chernClass.addMonomial(mb.set(0, 2 * i).build(), b, cohomology);
      if (i <= bound) {
        pontClass.addMonomial(mb.set(0, 4 * i).build(), b, cohomology);
      }
    }
    // reduce mod 2 to get Stiefel-Whitney class
    charClasses.put("sw", mod2Cohomology.reduce(chernClass));
  }
  /**
   * 
   * @param n
   * @param k
   * @return 
   */
  private static BigInteger binomial(int n, int k) {
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
