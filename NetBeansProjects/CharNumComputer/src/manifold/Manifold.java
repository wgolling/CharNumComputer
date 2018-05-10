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
import lib.*;
import java.util.*;
import java.util.stream.*;

/**
 *
 * @author William Gollinger
 */
public abstract class Manifold {
  
  protected static class Properties {
    protected int                       rDim = -1;
    protected boolean                   isComplex;
    protected int                       cDim = -1;
    protected MultiDegree               mu;
    protected PolyRing<BigInt>          cohomology;
    protected PolyRing<IntMod2>         mod2Cohomology;
    protected PolyRing<BigInt>.Element  chernClass;
    protected PolyRing<BigInt>.Element  pontClass;
    protected PolyRing<IntMod2>.Element swClass;
    protected CharNumbers               charNumbers;
  }
  
  protected final Properties p;
  
  protected Manifold(Properties p) {
    if (   p.isComplex && p.chernClass.domain() != p.cohomology
        || p.pontClass.domain()  != p.cohomology
        || p.swClass.domain()    != p.mod2Cohomology)
      throw new IllegalArgumentException("Characteristic classes in wrong domains.");
    if (!p.cohomology.truncation().isBounded())
      throw new IllegalArgumentException("Cohomology not of finite type.");
    if (!(p.mu.total() == p.rDim))
      throw new IllegalArgumentException("Fundamental class has wrong degree.");
    this.p = p;
  }
    
  protected Properties getProperties() {
    return p;
  }
  
  @Override
  public abstract String toString();
  
  /*
  Getter methods.
  */
  
  /**
   * Returns the manifold's Real dimension.
   * @return 
   */
  public int rDim() {
    return p.rDim;
  }
  /**
   * Returns true if the manifold has a Complex structure.
   * @return 
   */
  public boolean isComplex() {
    return p.isComplex;
  }
  /**
   * Returns the manifold's Complex dimension if the manifold is Complex.
   * Otherwise throws UnsupportedOperationException.
   * @return 
   */
  public int cDim() {
    if (!isComplex()) {
      throw new UnsupportedOperationException();
    }
    return p.cDim;
  }
  /**
   * Returns the manifold's cohomological fundamental class.
   * @return 
   */
  public MultiDegree mu() {
    return p.mu;
  }
  /**
   * Returns the manfiold's cohomology ring with Integer coefficients.
   * @return 
   */
  public PolyRing<BigInt> cohomology() {
    return p.cohomology;
  }
  /**
   * Returns the manifolds' cohomology ring with Z/2 coefficients.
   * @return 
   */
  public PolyRing<IntMod2> mod2Cohomology() {
    return p.mod2Cohomology;
  }
  /**
   * Takes an element of cohomology and returns 
   * its Mod-2 reduction in mod2Cohomology.
   * @param q
   * @param p
   * @return 
   */
  public static PolyRing<IntMod2>.Element reduceMod2(PolyRing<BigInt>.Element q, Properties p) {
    if (q.domain() != p.cohomology)
      throw new IllegalArgumentException("Wrong domain.");
    PolyRing<IntMod2>.Element answer = p.mod2Cohomology.zero();
    for (Map.Entry<MultiDegree, BigInt> e : q.getTerms().entrySet()) {
      answer = 
          p.mod2Cohomology.add(
              answer,
              p.mod2Cohomology.makeElement(
                  e.getKey(), 
                  new IntMod2(e.getValue())));
    }
    return answer;
  }
  
  /**
   * Returns a copy of the manifold's Pontryagin class.
   * @return 
   */
  public PolyRing<BigInt>.Element pontClass() {
    return p.cohomology.makeElement(p.pontClass);
  }
  /**
   * Returns a copy of the manifold's Chern class if it is complex,
   * and otherwise throws an Unsupported Operation exception.
   * @return 
   */
  public PolyRing<BigInt>.Element chernClass() {
    if (!isComplex())
      throw new UnsupportedOperationException();
    return p.cohomology.makeElement(p.chernClass);
  }
  /**
   * Returns a copy of the manifolds Stiefel-Whitney class.
   * @return 
   */
  public PolyRing<IntMod2>.Element swClass() {
    return p.mod2Cohomology.makeElement(p.swClass);
  }
  
  
  
  /*
  CharNumbers
  */
  
  /**
   * Returns a copy of a manifold's CharNumbers object.
   * @param pc
   * @return 
   */
  public CharNumbers getCharNumbers(PartitionComputer pc) {
    if (p.charNumbers == null) {
      p.charNumbers = CharNumbers.computeCharNumbers(this, pc);
    }
    return new CharNumbers(p.charNumbers);
  }
  
  /**
   * CharNumbers is a collection of functions from partitions to integers.
   */
  public static class CharNumbers {
        
    private Map<Partition, BigInt>  pontNums;
    private Map<Partition, BigInt>  chernNums;
    private Map<Partition, IntMod2> swNums;
    
    public CharNumbers() {
      
      pontNums  = new HashMap<>();
      chernNums = new HashMap<>();
      swNums    = new HashMap<>();
    }
    public CharNumbers(CharNumbers o) {
      this(o.pontNums, o.chernNums, o.swNums);
    }
    private CharNumbers(
            Map<Partition, BigInt>  pontNums,
            Map<Partition, BigInt>  chernNums,
            Map<Partition, IntMod2> swNums) {
      this.pontNums  = pontNums;
      this.chernNums = chernNums;
      this.swNums    = swNums;
    }
    
    /**
     * 
     * @param part
     * @return 
     */
    public BigInt pontryaginNumber(Partition part) {
      if (pontNums == null) {
        throw new UnsupportedOperationException("No Pontryagin numbers.");
      }
      if (pontNums.get(part) == null) 
        throw new IllegalArgumentException("Invalid partition.");
      return pontNums.get(part);
    }
    public BigInt chernNumber(Partition part) {
      if (chernNums == null) {
        throw new UnsupportedOperationException("No Chern numbers.");
      }
      if (chernNums.get(part) == null) 
        throw new IllegalArgumentException("Invalid partition.");
      return chernNums.get(part);
    }
    public IntMod2 stiefelWhitneyNumber(Partition part) {
      if (swNums == null) {
        throw new UnsupportedOperationException("No Stiefel-Whitney numbers.");
      }
      if (swNums.get(part) == null) 
        throw new IllegalArgumentException("Invalid partition.");
      return swNums.get(part);
    }
    
    public Map<Partition, BigInt> getPontryaginNumbers() {
      return (pontNums == null) ? null : new HashMap<>(pontNums);
    }
    public Map<Partition, BigInt> getChernNumbers() {
      return (chernNums == null) ? null : new HashMap<>(chernNums);
    }
    public Map<Partition, IntMod2> getStiefelWhitneyNumbers() {
      return (swNums == null) ? null : new HashMap<>(swNums);
    }
    
    /** 
     * Assumes:
     *  poly is an element of ring
     *  m.total() % scale == 0
     * @param <C>
     * @param ring
     * @param poly
     * @param mu
     * @param scale
     * @param pc
     * @return 
     */
    private static <C extends Coefficient<C>> Map<Partition, C> genericComputeCharNumbers(
            PolyRing<C> ring, 
            PolyRing<C>.Element poly, 
            MultiDegree mu,
            int scale,
            PartitionComputer pc) {
      
      Map<Partition, C> genericCharNums = new HashMap<>();
      Map<Integer, PolyRing<C>.Element> gradedPoly = poly.getHomogeneousParts();
      int n = mu.total() / scale;
      List<Partition> parts = pc.getPartitions(n);
      
      for (Partition part : parts) {
        PolyRing<C>.Element prod = ring.one();
        for (Integer i : part.getNumbers()) {
          if (gradedPoly.get(scale * i) != null) {
            prod = ring.multiply(prod, gradedPoly.get(scale * i));
          } else {
            prod = ring.zero();
          }
        }
        genericCharNums.put(part, prod.get(mu));
      }
      return genericCharNums;
    }
            
    private static Map<Partition, IntMod2> chernToSW(Map<Partition, BigInt> chern) {
      return chern.entrySet()
              .stream()
              .collect(Collectors.toMap(
                  e -> Partition.scale(e.getKey(), 2),
                  e -> new IntMod2(e.getValue())));
    }
    /**
     * Constructs the CharNumbers object for a manifold.
     * Assumes m's characteristic classes have already been computed.
     * @return 
     */
    private static CharNumbers computeCharNumbers(
            Manifold m,
            PartitionComputer pc) {
      
      Map<Partition, BigInt> pontNums;
      if (m.rDim() % 4 != 0) {
        pontNums = null;
      } else {
        pontNums = CharNumbers.<BigInt>genericComputeCharNumbers(
                m.cohomology(),
                m.pontClass(),
                m.mu(),
                4,
                pc);
      }
      Map<Partition, BigInt> chernNums;
      Map<Partition, IntMod2> swNums;
      if (m.isComplex()) {
        chernNums = CharNumbers.<BigInt>genericComputeCharNumbers(
                m.cohomology(),
                m.chernClass(),
                m.mu(),
                2,
                pc);
        swNums = chernToSW(chernNums);
      } else {
        chernNums = null;
        swNums = CharNumbers.<IntMod2>genericComputeCharNumbers(
                m.mod2Cohomology(),
                m.swClass(),
                m.mu(),
                1,
                pc);
      }
      
      return new CharNumbers(pontNums, chernNums, swNums);
    }
    
  }
  
}
