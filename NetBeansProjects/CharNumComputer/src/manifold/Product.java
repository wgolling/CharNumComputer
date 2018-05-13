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
import java.util.*;
import java.util.stream.*;

/**
 * Models a product of two or more manifolds.
 * 
 * The real dimension is the sum of the real dimensions of the factors.  
 * The product is complex if all of the factors are, and
 * the complex dimension is the sum of their complex dimensions.
 * 
 * The cohomology of a product is described by the Kunneth exact sequence.
 * For a commutative ring R and an R-module A, cohomology groups 
 * with coefficients in A form a graded R-module, so in particular 
 * they can be tensored over R and the so-called "Tor groups" can be formed, 
 * all in a purely algebraic way via Homological Algebra.
 * 
 * For manifolds M and N, the Kunneth Theorem says there is 
 * the following exact sequence:
 *    0 -> tensor_R(H^*(M; A), H^*(N; A)) 
 *                    -> H^*(M x N; A) 
 *                                      -> Tor_R(H^*(M; A), H^*(N; A)) -> 0
 * (where the final map increases the degree by one).
 * 
 * For example, if the mysterious Tor term vanishes then 
 * the cohomology of the product is just the tensor product of the cohomologies.
 * In particular the Tor term vanishes for products of CP(n)'s and HP(n)'s 
 * so we can tensor their cohomology rings together without fear.
 * 
 * Characteristic classes satisfy the Whitney Product Formula.
 * Chern (when they are defined) and Stiefel-Whitney classes are multiplicative:
 *    w(M x N) = tensor(w(M), w(N))
 *    c(M x N) = tensor(c(M), c(N))
 * Pontryagin classes however have a quirk, they are multiplicative modulo 2-torsion:
 *    2 * ( p(M x N) - tensor(p(M), p(N)) ) = 0
 * Since products of CP(n) and HP(m) have no 2-torsion, in their integral cohomologies, 
 * all characteristic classes can be computed with the product rule.
 * 
 * @author William Gollinger
 */
public class Product extends Manifold {
  
  List<Manifold> factors;
  
  /**
   * Constructs a manfiold which models the product of factors.
   * @param factors 
   */
  public Product(List<Manifold> factors) {
    super(makeProperties(factors));
    this.factors = factors;
  }
  /**
   * Convenient constructor when there are only two manifold factors.
   * @param m1
   * @param m2 
   */
  public Product(Manifold m1, Manifold m2) {
    this(Arrays.asList(m1, m2));
  }
  
  private static Properties makeProperties(List<Manifold> factors) {
    Properties p = new Properties();
    // Set dimensions.
    p.rDim = 0;
    p.isComplex = true;
    p.cDim = 0;
    for (Manifold m : factors) {
      p.rDim += m.rDim();
      if (p.isComplex && m.isComplex()) {
        p.cDim += m.cDim();
      } else {
        p.isComplex = false;
      }
    }
    // Tensor the cohomology rings together.
    p.cohomology = new Tensor<>(BigInt.ring, factors
            .stream()
            .map(m -> m.cohomology())
            .collect(Collectors.toList())
    );
    p.mu = p.cohomology.truncation();
    p.mod2Cohomology = new Tensor<>(IntMod2.ring, factors
            .stream()
            .map(m -> m.mod2Cohomology())
            .collect(Collectors.toList())
    );
    // Apply the Whitney Product Formula.
    p.pontClass = ((Tensor)p.cohomology).tensor(factors
            .stream()
            .map(m -> m.pontClass())
            .collect(Collectors.toList()));
    if (p.isComplex) {
      // If the product is complex, compute the chern class
      // and reduce modulo 2 to get the Stiefel-Whiteney class.
      p.chernClass = ((Tensor)p.cohomology).tensor(factors
              .stream()
              .map(m -> m.chernClass())
              .collect(Collectors.toList()));
      p.swClass = reduceMod2(p.chernClass, p);
    } else {
      p.chernClass = null;
      p.swClass = ((Tensor)p.mod2Cohomology).tensor(factors
              .stream()
              .map(m -> m.swClass())
              .collect(Collectors.toList()));
    }
    
    return p;
  }
  
  
  /*
  Utility methods.
  */
  
  @Override 
  public String toString() {
    if (factors.isEmpty()) {
      return "(empty)";
    }
    String answer = factors.get(0).toString();
    if (factors.size() == 1) return answer;
    for (int i = 1; i < factors.size(); i++) {
      answer += " x " + factors.get(i).toString();
    }
    return answer; 
  }

}
