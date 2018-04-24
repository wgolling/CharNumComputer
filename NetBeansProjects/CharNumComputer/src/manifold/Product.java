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
  
  private final int rDim;
  private final boolean isComplex;
  private final int cDim;
  private final MultiDegree truncation;
  private final Map<String, Polynomial> charClasses;
  
  private final Polynomial.Ring cohomology;
  private final Polynomial.Ring mod2Cohomology;
  
  /**
   * Constructs a manfiold which models the product of factors.
   * @param factors 
   */
  public Product(List<Manifold> factors) {
    this.factors = new ArrayList<>(factors);
    // set up temporary variables
    int tR = 0;
    boolean tIC = true;
    int tC = 0;
    Polynomial.Ring tCo  = new Polynomial.Ring(0);
    Polynomial.Ring t2Co = new Polynomial.Ring(0);
    for (Manifold m : this.factors) {
      // The product's rDim is the sum of the factors' rDims.
      tR += m.rDim();
      // The product is complex if all of the factors are.
      if (m.isComplex()) {
        tC += m.cDim();
      } else {
        tIC = false;
      }
      // At the moment we are only dealing with CP(n)'s,
      // so their cohomology rings are just tensored together.
      tCo  = Polynomial.Ring.tensor(tCo,  m.cohomology());
      t2Co = Polynomial.Ring.tensor(t2Co, m.mod2Cohomology());
    }
    
    // set final fields
    rDim = tR;
    if (tIC) {
      isComplex = true;
      cDim = tC;
    } else {
      isComplex = false;
      cDim = -1;
    }
    cohomology = tCo;
    truncation = cohomology.truncation();
    mod2Cohomology = t2Co;
    
    // compute characteristic classes
    charClasses = new HashMap<>();
    computeCharClasses();
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
  /*
  implementation
  */
  
  @Override
  public int rDim() {
    return rDim;
  }
  @Override
  public boolean isComplex() {
    return isComplex;
  }
  @Override
  public int cDim() {
    if (!isComplex) {
      throw new UnsupportedOperationException();
    }
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
   * Computes the characteristic classes of a product 
   * using the Whitney Product formula.
   */
  private void computeCharClasses() {
    
    // If the product is complex, we can compute the Chern class and then
    // the Stiefel-Whitney class is the mod-2 reduction.
    // Otherwise we don't compute Chern class and compute SW instead.
    // We specify which type we will compute with the type variable.
    String type = "sw";
    // initialize characteristic classes
    if (isComplex) {
      charClasses.put("chern", cohomology.one());
      type = "chern";
    }
    charClasses.put("sw", mod2Cohomology.one());
    charClasses.put("pont", cohomology.one());
    
    Map<String, Polynomial.Ring> rings = new HashMap<>();
    rings.put("sw", mod2Cohomology);
    rings.put("chern", cohomology);
    Polynomial.Ring wildRing = rings.get(type);

    int seenVars = 0;
    int remainingVars = cohomology.vars();
    
    for (Manifold m : factors) {
//      Map<String, Polynomial> tempCharClasses = m.getCharClasses();
//      charClasses.put("pont", cohomology.tensor(charClasses.get("pont"), 
//                                                tempCharClasses.get("pont")));
//      charClasses.put(type, wildRing.tensor(charClasses.get(type), 
//                                            tempCharClasses.get(type)));

      //TODO clean this up
      int mVars = m.cohomology().vars();
      remainingVars -= mVars;
      
      Polynomial pont = cohomology.tensor(
              cohomology.one(seenVars),
              cohomology.tensor(
                      m.getCharClasses().get("pont"),
                      cohomology.one(remainingVars)) );
      pont = cohomology.times(pont, charClasses.get("pont"));
      charClasses.put("pont", pont);
      
      Polynomial wild = wildRing.tensor(
              wildRing.one(seenVars),
              wildRing.tensor(
                      m.getCharClasses().get(type),
                      wildRing.one(remainingVars)) );
      wild = wildRing.times(wild, charClasses.get(type));
      charClasses.put(type, wild);
      
      seenVars += mVars;
    }
    
    if (isComplex) {
      charClasses.put("sw", mod2Cohomology.reduce(charClasses.get("chern")));
    }
  }
  
}
