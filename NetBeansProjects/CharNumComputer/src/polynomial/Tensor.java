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
 *
 * @author William Gollinger
 * @param <C>
 */
public class Tensor<C extends Coefficient<C>> extends PolyRing<C> {
  
  private final List<PolyRing<C>> factors;
  private final List<Integer> varSums;
  
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
  static MultiDegree concatStream(Stream<MultiDegree> str) {
    return str.reduce(MultiDegree.empty(), MultiDegree::concat);
  }
  
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
  public Tensor<C>.Element tensor(PolyRing<C>.Element p, PolyRing<C>.Element q) {
    return tensor(Arrays.asList(p, q));
  }
  /**
   * Assumes the parameter Lists have the same length.
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
   * Assumes domains have been validated.
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
