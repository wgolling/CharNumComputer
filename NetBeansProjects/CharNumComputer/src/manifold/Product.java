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

import java.util.*;
import charnumcomputer.*;

/**
 *
 * @author William Gollinger
 */
public class Product implements Manifold {
  List<Manifold> factors;
  
  private final int rDim;
  private final boolean isComplex;
  private final int cDim;
  private final MultiDegree.Builder mb;
  private final MultiDegree truncation;
  private final Map<String, Polynomial> charClasses;
  
  private final Polynomial.Ring pr;
  
  
  public Product(List<Manifold> factors) {
    this.factors = new ArrayList<>(factors);
    // set up temporary variables
    int tR = 0;
    boolean tIC = true;
    int tC = 0;
    mb = new MultiDegree.Builder();
    MultiDegree tT = mb.build();
    // construct them all iteratively
    for (Manifold m : this.factors) {
      tR += m.rDim();
      if (m.isComplex()) {
        tC += m.cDim();
      } else {
        tIC = false;
      }
      tT = MultiDegree.concat(tT, m.truncation());
    }
    rDim = tR;
    if (tIC) {
      isComplex = true;
      cDim = tC;
    } else {
      isComplex = false;
      cDim = -1;
    }
    truncation = tT;
    charClasses = new HashMap<>();
    pr = new Polynomial.Ring(truncation);
    computeCharClasses();
  }
  
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
  public Map<String, Polynomial> getCharClasses() {
    return new HashMap<>(charClasses);
  }

  private void computeCharClasses() {
    String type = "sw";
    if (isComplex) {
      charClasses.put("chern", pr.one());
      type = "chern";
    }
    charClasses.put("sw", pr.one());
    charClasses.put("pont", pr.one());
    
    int seenVars = 0;
    int remainingVars = truncation.vars();
    for (Manifold m : factors) {
      int mVars = m.truncation().vars();
      remainingVars -= mVars;
      
      Polynomial pont = pr.tensor(
              pr.one(seenVars),
              pr.tensor(
                      m.getCharClasses().get("pont"),
                      pr.one(remainingVars)) );
      pont = pr.times(pont, charClasses.get("pont"));
      charClasses.put("pont", pont);
      
      Polynomial wild = pr.tensor(
              pr.one(seenVars),
              pr.tensor(
                      m.getCharClasses().get(type),
                      pr.one(remainingVars)) );
      wild = pr.times(wild, charClasses.get(type));
      charClasses.put(type, wild);
      
      seenVars += mVars;
    }
    
    if (isComplex) {
      charClasses.put("sw", pr.reduce(charClasses.get("chern"), 2));
    }
  }
  
}
