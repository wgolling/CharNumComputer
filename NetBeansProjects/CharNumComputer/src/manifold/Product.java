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
public class Product extends Manifold {
  List<Manifold> factors;
  
  private final int rDim;
  private final boolean isComplex;
  private final int cDim;
  private final MultiDegree.Builder mb;
  private final MultiDegree truncation;
  private final Map<String, Polynomial> charClasses;
  
  private final Polynomial.Ring cohomology;
  private final Polynomial.Ring mod2Cohomology;
  
  
  public Product(List<Manifold> factors) {
    this.factors = new ArrayList<>(factors);
    // set up temporary variables
    int tR = 0;
    boolean tIC = true;
    int tC = 0;
    mb = new MultiDegree.Builder();
    Polynomial.Ring tCo  = new Polynomial.Ring(0);
    Polynomial.Ring t2Co = new Polynomial.Ring(0);
    // construct them all iteratively
    for (Manifold m : this.factors) {
      tR += m.rDim();
      if (m.isComplex()) {
        tC += m.cDim();
      } else {
        tIC = false;
      }
      tCo  = Polynomial.Ring.tensor(tCo,  m.cohomology());
      t2Co = Polynomial.Ring.tensor(t2Co, m.mod2Cohomology());
    }
    
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
    charClasses = new HashMap<>();
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
    
    // compute pont class
    // if isComplex, compute chern classes and take mod-2 to get SW
    // else compute SW
    String type = "sw";
    if (isComplex) {
      charClasses.put("chern", cohomology.one());
      type = "chern";
    }
    charClasses.put("sw", mod2Cohomology.one());
    charClasses.put("pont", cohomology.one());
    
    int seenVars = 0;
    int remainingVars = cohomology.vars();
    Map<String, Polynomial.Ring> rings = new HashMap<>();
    rings.put("sw", mod2Cohomology);
    rings.put("chern", cohomology);
    Polynomial.Ring wildRing = rings.get(type);
    for (Manifold m : factors) {
//      Map<String, Polynomial> tempCharClasses = m.getCharClasses();
//      charClasses.put("pont", cohomology.tensor(charClasses.get("pont"), 
//                                                tempCharClasses.get("pont")));
//      charClasses.put(type, rings.get(type).tensor(
//                                                charClasses.get(type), 
//                                                tempCharClasses.get(type)));
      
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
