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
 * For manifolds M and N, the Kunneth exact sequence is
 *    0 -> tensor_R(H^*(M; A), H^*(N; A)) 
 *                    -> H^*(M x N; A) 
 *                                      -> Tor_R(H^*(M; A), H^*(N; A)) -> 0
 * 
 * In particular, if the mysterious Tor term vanishes then 
 * the cohomology of the product is the tensor product of the cohomologies.
 * For example the Tor term vanishes for products of CP(n)'s, so we can
 * tensor their cohomology rings together without fear.
 * 
 * Characteristic classes satisfy the Whitney Product Formula.
 * Stiefel-Whitney and Chern classes (when they are defined) are multiplicative:
 *    w(M x N) = tensor(w(M), w(N))
 *    c(M x N) = tensor(c(M), c(N))
 * Pontryagin classes however have a quirk, they are multiplicative modulo 2-torsion:
 *    2 * ( p(M x N) - tensor(p(M), p(N)) ) = 0
 * Since H^*(CP(n) x CP(m); Z) has no 2-torsion, all characteristic classes
 * can be computed with the product rule.
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
  
  private static Properties makeProperties(List<Manifold> factors) {
    Properties p = new Properties();
    p.rDim = 0;
    p.isComplex = true;
    p.cDim = 0;
    p.cohomology = new Polynomial.Ring(0);
    p.mod2Cohomology = new Polynomial.Ring(0);
    p.mod2Cohomology.setModulus(2);
    for (Manifold m : factors) {
      p.rDim += m.rDim();
      if (p.isComplex && m.isComplex()) {
        p.cDim += m.cDim();
      } else {
        p.isComplex = false;
      }
      p.cohomology = Polynomial.Ring.tensor(p.cohomology, m.cohomology());
      p.mod2Cohomology = Polynomial.Ring.tensor(p.mod2Cohomology, m.cohomology());
    }
    computeCharClasses(factors, p);
    return p;
  }
  
  /**
   * Implements the Whitney Product Formula to compute 
   * the characteristic classes of a product.
   * Only adds entry for Chern class if the isComplex property is true.
   * @param factors
   * @param p 
   */
  private static void computeCharClasses(List<Manifold> factors, Properties p) {
    p.charClasses = new HashMap<>();
    
    Polynomial.Ring Z = new Polynomial.Ring(0);
    p.charClasses.put("sw", Z.one());
    p.charClasses.put("pont", Z.one());
    if (p.isComplex) {
      p.charClasses.put("chern", Z.one());
    }
    
    for (Manifold m : factors) {
      p.charClasses.put("pont", 
                        p.cohomology.tensor(p.charClasses.get("pont"), 
                                            m.getCharClasses().get("pont")));
      if (p.isComplex) {
        p.charClasses.put("chern",
                          p.cohomology    .tensor(p.charClasses.get("chern"),
                                                  m.getCharClasses().get("chern")));
      } else {
        p.charClasses.put("sw",
                          p.mod2Cohomology.tensor(p.charClasses.get("sw"),
                                                  m.getCharClasses().get("sw")));
      }
    }
    if (p.isComplex) {
      p.charClasses.put("sw", p.mod2Cohomology.reduce(p.charClasses.get("chern")));
    }
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
