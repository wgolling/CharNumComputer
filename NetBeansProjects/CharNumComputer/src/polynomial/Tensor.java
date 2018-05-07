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

import java.util.*;
import java.util.stream.*;

/**
 * The Tensor class models a tensor product of a list of polynomial rings
 * with the same coefficient types.
 * 
 * This amounts to simply concatenating together 
 * all of the variables and relations.
 * 
 * It records the factors in the product, and provides functionality for 
 * tensoring together a list of polynomials, one from each factor. 
 * 
 * @author William Gollinger
 * @param <C>
 */
public class Tensor<C extends Coefficient<C>> extends PolyRing<C> {
  
  private final List<PolyRing<C>> factors;
  private final List<Integer> varSums;                                       // Helpfull field for determining how many variables are to the left and right of factors.get(i)
  
  /**
   * Constructs the tensor product of a list of PolyRings with coefficient C.
   * @param cRing
   * @param factors 
   */
  public Tensor(C cRing, List<PolyRing<C>> factors) {
    super(cRing, concatStream(factors.stream().map(ring -> ring.variables())),
                concatStream(factors.stream().map(ring -> ring.truncation()))
    );
    this.factors = factors;
    varSums = new ArrayList<>();
    varSums.add(0);
    for (int i = 0; i < factors.size(); i++) {
      varSums.add(varSums.get(i) + factors.get(i).vars());
    }
  }
  /**
   * Helper method for concatenating a stream of MultiDegrees.
   * @param str
   * @return 
   */
  static MultiDegree concatStream(Stream<MultiDegree> str) {
    return str.reduce(MultiDegree.empty(), MultiDegree::concat);
  }
  
  /**
   * Returns a polynomial which is the tensor product of the list of factors.
   * Will throw an IllegalArgumentException if 
   * polyFactors.get(i) is not an element of the i-th factor of this.
   * @param polyFactors
   * @return 
   */
  public Tensor<C>.Element tensor(List<PolyRing<C>.Element> polyFactors) {
    if (polyFactors.size() != factors.size()
        || ! validateDomains(factors, polyFactors)) 
      throw new IllegalArgumentException();
    Element prod = one();
    for (int i = 0; i < polyFactors.size(); i++) {
      prod = multiply(prod, inject(polyFactors.get(i), i));
    }
    return prod;
  }
  /**
   * Returns a polynomial which is the tensor product of the two factors.
   * Will throw an IllegalArgumentException if this instance
   * of Tensor has something other than two factors, or if
   * p or q is not an element of the first or second factor respectively.
   * @param p
   * @param q
   * @return 
   */
  public Tensor<C>.Element tensor(PolyRing<C>.Element p, PolyRing<C>.Element q) {
    return tensor(Arrays.asList(p, q));
  }
  /**
   * 
   * Assuming the parameter Lists of ring and polynomials have the same length,
   * checks that the polynomials are elements of the respective rings.
   * @param rings
   * @param polys
   * @return 
   */
  private boolean validateDomains(
          List<PolyRing<C>> rings, 
          List<PolyRing<C>.Element> polys) {
    for (int i = 0; i < rings.size(); i++) {
      if (polys.get(i).domain() != rings.get(i)) {
        return false;
      }
    }
    return true;
  }
  /**
   * Assuming domains have been validated, produces and element
   * of the tensor product corresponding to tensoring p with 
   * the appropriate amount of 1's on both sides.
   * @param p
   * @param i
   * @return 
   */
  private Element inject(PolyRing<C>.Element p, int i) {
    Element pPrime = zero();
    for (MultiDegree d : p.terms.keySet()) {
      pPrime.terms.put(injectMultiDegree(d, i), p.terms.get(d));
    }
    return pPrime;
  }
  /**
   * Pads d with the appropriate number of 0s.
   * Assumes  d.vars() == factors.get(i).vars(),
   * and that 0 <= i < factors.size()
   * @param d
   * @param i
   * @return 
   */
  private MultiDegree injectMultiDegree(MultiDegree d, int i) {
    return MultiDegree.pad(
            d, 
            varSums.get(i), 
            varSums.get(factors.size()) - varSums.get(i + 1));
  }
  
}

interface RingToMultiDegree {
  MultiDegree convert(PolyRing ring);
}