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
import java.math.*;

/**
 *
 * @author William Gollinger
 */
public abstract class Manifold {
  
  protected static class Properties {
    protected int                     rDim = -1;
    protected boolean                 isComplex;
    protected int                     cDim = -1;
    protected Polynomial.Ring         cohomology;
    protected Polynomial.Ring         mod2Cohomology;
    protected Map<String, Polynomial> charClasses;
    protected CharNumbers             charNumbers;
  }
  
  private final Properties p;
  protected Manifold(Properties p) {
    this.p = p;
  }
  
  protected Properties getProperties() {
    return p;
  }
  
    
  /*
  Getter methods.
  */
  
  /**
   * Returns the manifold's Real dimension.
   * @return 
   */
  public int rDim() {return p.rDim;}
  /**
   * Returns true if the manifold has a Complex structure.
   * @return 
   */
  public boolean isComplex() {return p.isComplex;}
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
  public MultiDegree truncation() {return p.cohomology.truncation();}
  /**
   * Returns the manfiold's cohomology ring with Integer coefficients.
   * @return 
   */
  public Polynomial.Ring cohomology() {return p.cohomology;}
  /**
   * Returns the manifolds' cohomology ring with Z/2 coefficients.
   * @return 
   */
  public Polynomial.Ring mod2Cohomology() {return p.mod2Cohomology;}
  /**
   * Returns a hash table of characteristic classes, keyed by their type.
   * All manifolds have "sw" and "pont" types, and
   * complex manifolds have "chern" type as well.
   * @return 
   */
  public Map<String, Polynomial> getCharClasses() {
    Map<String, Polynomial> answer = new HashMap<>();
    p.charClasses.entrySet()
            .stream()
            .forEach(entry -> answer.put(entry.getKey(), entry.getValue()));
    return answer;
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
    
    private Map<String, Map<Partition, BigInteger>> charNums;
    
    public CharNumbers() {
      charNums = new HashMap<>();
    }
    public CharNumbers(CharNumbers o) {
      this();
      for (String type : o.charNums.keySet()) {
        charNums.put(type, new HashMap<>(o.charNums.get(type)));
      }
    }
    
    /**
     * Returns null if there are no characteristic numbers of that type.
     * @param type
     * @param p
     * @return 
     */
    public BigInteger get(String type, Partition p) {
      if (charNums.get(type) == null) {
        return null;
      }
      BigInteger n = charNums.get(type).get(p);
      return (n == null) ? BigInteger.ZERO : n;
    }
    
    /**
     * Constructs the CharNumbers object for a manifold.
     * Assumes m's characteristic classes have already been computed.
     * @return 
     */
    private static CharNumbers computeCharNumbers(
            Manifold m,
            PartitionComputer pc) {
      
      Map<String, Polynomial> charClasses = m.getCharClasses();
      MultiDegree trunc = m.truncation();
      CharNumbers charNumbers = new CharNumbers();
      
      Map<String, Integer> degrees = new HashMap<>();
      degrees.put("sw", 1);
      degrees.put("chern", 2);
      degrees.put("pont", 4);
      
      Map<String, Polynomial.Ring> rings = new HashMap<>();
      rings.put("sw", m.mod2Cohomology());
      rings.put("chern", m.cohomology());
      rings.put("pont", m.cohomology());
      Polynomial.Ring pr;
      
      for (String type : charClasses.keySet()) {
        if (m.rDim() % degrees.get(type) != 0) continue;
        int quasiDim = m.rDim() / degrees.get(type);
        
        charNumbers.charNums.put(type, new HashMap<>());
        
        Polynomial charClass = charClasses.get(type);
        
        List<Polynomial> charList = new ArrayList<>();
        for (int i = 0; i < quasiDim + 1; i ++) {
          charList.add(charClass.getHomogeneousPart(i * degrees.get(type)));
        }
        pr = rings.get(type);
        
        List<Partition> parts = pc.getPartitions(quasiDim);
        for (Partition part : parts) {
          // For some reason this makes the code not work for sw classes
          // check if any factors are 0
          //TODO sort this out
//          boolean exit = false;
//          for (Integer i : part.getNumbers()) {
//            if (charList.get(i).isZero()) {
//              exit = true;
//              break;
//            }
//          }
//          if (exit) break;
          
          Polynomial p = pr.one();
          for (Integer i : part.getNumbers()) {
            p = pr.times(p, charList.get(i));
          }
          
          BigInteger n = p.get(trunc);
          if (n.equals(BigInteger.ZERO)) continue;
          charNumbers.charNums.get(type).put(part, n);
        } 
      }      
      return charNumbers;
    }
    
  }
  
}
