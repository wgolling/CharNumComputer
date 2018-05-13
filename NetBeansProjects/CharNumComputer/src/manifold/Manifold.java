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

/**
 * The Manifold class is meant to model certain properties of
 * the mathematical object.
 * 
 * Every manifold has a real dimension.  If it is complex, 
 * its real dimension must be even and its complex dimension
 * is half as much.
 * 
 * At the moment, all manifolds considered have cohomology rings
 * which are isomorphic to polynomial rings.
 * 
 * The integral cohomology ring contains a Pontryagin class, 
 * and a Chern Class if the manifold is complex.
 * The mod-2 cohomology ring contains a Stiefel-Whitney class.
 * 
 * @author William Gollinger
 */
public abstract class Manifold {
  
  /**
   * A Manifold is initialized with a Properties object, 
   * which is validated by the constructor.
   * 
   * A subclass of Manifold only needs to provide a static method
   * which produces an appropriate Properties object 
   * for the Manifold constructor.
   */
  protected static class Properties {
    protected int                       rDim = -1;
    protected boolean                   isComplex;
    protected int                       cDim = -1;
    protected MultiDegree               mu;                                  // mu represents the cohomological fundamental class of the manifold
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
  
  
  /*
  Utility methods.
  */
  
  @Override
  public abstract String toString();
  
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
  
  
  /*
  Getter methods.
  */
  
  /**
   * Returns the manifold's Properties object.
   * @return 
   */
  protected Properties getProperties() {
    return p;
  }

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
      throw new UnsupportedOperationException("Manifold is not complex.");
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
      throw new UnsupportedOperationException("Manifold is not complex.");
    return p.cohomology.makeElement(p.chernClass);
  }
  /**
   * Returns a copy of the manifold's Stiefel-Whitney class.
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
   * 
   * Each characteristic class is divided into homogeneous components.
   * Partitions are used as a recipe for multiplying these components
   * together to get a homogenous polynomial of degree rDim,
   * and the associated characteristic number is the coefficient of mu.
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
    
    
    /*
    Utility methods.
    */
    
    /**
     * Two CharNumbers objects are the same iff all of their
     * characteristic number Maps agree.
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof CharNumbers))
        return false;
      CharNumbers c = (CharNumbers)o;
      boolean pontEquals = 
          (pontNums == null && c.pontNums == null)
          || (pontNums != null && c.pontNums != null && pontNums.equals(c.pontNums));
      boolean chernEquals = 
          (chernNums == null && c.chernNums == null)
          || (chernNums != null && c.chernNums != null && chernNums.equals(c.chernNums));
      boolean swEquals = 
          (swNums == null && c.swNums == null)
          || (swNums != null && c.swNums != null && swNums.equals(c.swNums));
      return pontEquals && chernEquals && swEquals;
    }
    @Override
    public int hashCode() {
      return pontNums.hashCode() + chernNums.hashCode() + swNums.hashCode();
    }
    
    /**
     * Returns a CharNumbers object whose values are all the negation
     * of the values of this.
     * @return 
     */
    public CharNumbers negate() {
      CharNumbers c = new CharNumbers();
      c.pontNums = BigInt.ring.negateMap(pontNums);
      c.chernNums = BigInt.ring.negateMap(chernNums);
      c.swNums = swNums;
      return c;
    }
    
    
    /*
    Getter methods.
    */
    
    /**
     * Returns the Pontryagin number for the given Partition.
     * Throws UnsupportedOperationException if pontNums is null, such as 
     * the case where the manifold's dimension is not divisible by 4.
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
    /**
     * Returns the Chern number for the given Partition.
     * Throws UnsupportedOperationException if chernNums is null, such as 
     * the case where the manifold is not complex.
     * @param part
     * @return 
     */
    public BigInt chernNumber(Partition part) {
      if (chernNums == null) {
        throw new UnsupportedOperationException("No Chern numbers.");
      }
      if (chernNums.get(part) == null) 
        throw new IllegalArgumentException("Invalid partition.");
      return chernNums.get(part);
    }
    /**
     * Returns the Stiefel-Whitney number for the given Partition.
     * @param part
     * @return 
     */
    public IntMod2 stiefelWhitneyNumber(Partition part) {
      if (swNums == null) {
        throw new UnsupportedOperationException("No Stiefel-Whitney numbers.");
      }
      if (swNums.get(part) == null) 
        throw new IllegalArgumentException("Invalid partition.");
      return swNums.get(part);
    }
    /** 
     * Returns the hash table of Pontryagin numbers.
     * @return 
     */
    public Map<Partition, BigInt> getPontryaginNumbers() {
      return (pontNums == null) ? null : new HashMap<>(pontNums);
    }
    /** 
     * Returns the hash table of Chern numbers.
     * @return 
     */
    public Map<Partition, BigInt> getChernNumbers() {
      return (chernNums == null) ? null : new HashMap<>(chernNums);
    }
    /** 
     * Returns the hash table of Stiefel-Whitney numbers.
     * @return 
     */
    public Map<Partition, IntMod2> getStiefelWhitneyNumbers() {
      return (swNums == null) ? null : new HashMap<>(swNums);
    }
    
    
    /*
    Computing CharNumbers
    */
    
    /**
     * Constructs the CharNumbers object for a manifold.
     * Assumes m's characteristic classes have already been computed.
     * @return 
     */
    private static CharNumbers computeCharNumbers(
            Manifold m,
            PartitionComputer pc) {
      
      Map<Partition, BigInt> pontNums;
      // There are Pontryagin numbers iff rDim is divisible by 4.
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
      // There are Chern numbers iff m is complex.
      if (m.isComplex()) {
        chernNums = CharNumbers.<BigInt>genericComputeCharNumbers(
                m.cohomology(),
                m.chernClass(),
                m.mu(),
                2,
                pc);
        swNums = chernToSW(chernNums, pc);
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
    /** 
     * Computes the characteristic numbers for a given polynomial
     * in a given ring with given fundamental class.
     * The int parameter scale is used because the indices of the various
     * characteristic classes range over different dimensions:
     *  Stiefel-Whitney has scale 1
     *  Chern has scale 2.
     *  Pontryagin has scale 4.
     * 
     * Assumes:
     *  poly is an element of ring, and
     *  m.total() % scale == 0
     * 
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
      List<Partition> parts = pc.getPartitions(mu.total() / scale);
      
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
            
    /**
     * Derives a complex manifold's Stiefel-Whitney numbers from its Chern numbers.
     * 
     * If a manifold is complex, its Stiefel-Whitney class is just 
     * the mod-2 reduction of the Chern class, and consequently
     * the analog is true for all Stiefel-Whitney numbers.
     * 
     * @param chern
     * @return 
     */
    private static Map<Partition, IntMod2> chernToSW(
            Map<Partition, BigInt> chern,
            PartitionComputer pc) {
      Map<Partition, IntMod2> sw = new HashMap<>();
      if (chern.isEmpty())
        return sw;
      // Fill map with 0s to avoid errors.
      int n = 0;
      for (Partition part : chern.keySet()) {
        n = part.sum();
        break;
      }
      List<Partition> parts = pc.getPartitions(2 * n);
      IntMod2 zero = IntMod2.ring.zero();
      parts.stream().forEach(part -> sw.put(part, zero));
      // Add potentially non-zero values.
      chern.entrySet()
              .stream()
              .forEach(e -> sw.put(
                      Partition.scale(e.getKey(), 2),
                      new IntMod2(e.getValue())));
      return sw;
    }
    
  }
  
}
