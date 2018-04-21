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
package charnumcomputer;

import java.util.*;
import java.math.BigInteger;

/**
 * Polynomial represents a graded polynomial, and supports arithmetic in
 * truncated polynomial rings with integer or finite cyclic coefficients.
 * @author William Gollinger
 */
public class Polynomial {
  /*
  
  */
  private final SortedMap<Integer, Map<MultiDegree, BigInteger>> terms;                    // some of the coefficients can get quite large.
  private final int vars;
  
  public Polynomial(int vars) {
    if (vars < 0) {
      throw new IllegalArgumentException();
    }
    terms = new TreeMap<>();
    this.vars = vars;
  }
  public Polynomial(MultiDegree d, BigInteger a) {
    this(d.vars());
    if (d == null || a == null) {
      throw new NullPointerException();
    }
    Map<MultiDegree, BigInteger> monomial = new HashMap<>();
    monomial.put(d, a);
    terms.put(d.total(), monomial);
  }
  public Polynomial(Polynomial p) {
    terms = new TreeMap<>();
    for (Integer i : p.terms.keySet()) {
      terms.put(i, new HashMap<>(p.terms.get(i)));
    }
    vars = p.vars;
  }
  
  
  /**
   * Two Polynomials are equal if they have 
   * the same coefficient for each MultiDegree.
   * @param o
   * @return 
   */
  @Override
  public boolean equals(Object o) {
    return (o instanceof Polynomial 
            && ((Polynomial)o).terms.equals(terms)
            && ((Polynomial)o).vars == vars);
  }
  @Override
  public int hashCode() {
    return terms.hashCode();
  }
  
  /**
   * A Polynomial is zero if it has no terms.
   * @return 
   */
  public boolean isZero() {
    return terms.isEmpty();
  }
  
  
  /*
  Getting methods.
  */
  
  /**
   * Returns the coefficient of d as a BigInteger.
   * @param d
   * @return 
   */
  public BigInteger get(MultiDegree d) {
    if (terms.get(d.total()) == null) {
      return BigInteger.ZERO;
    }
    BigInteger a = terms.get(d.total()).get(d);
    return (a == null) ? BigInteger.ZERO : a;  
  }
  
  /**
   * Returns a new polynomial consisting of the degree i terms of p.
   * @param d
   * @return 
   */
  public Polynomial getHomogeneousPart(int d) {
    Polynomial p = new Polynomial(vars);
    if (terms.get(d) != null) {
      p.terms.put(d, new HashMap<>(terms.get(d)));
    }
    return p;
  }
  public Map<Integer, Polynomial> getGraded() {
    Map<Integer, Polynomial> graded = new HashMap<>();
    for (Integer i : terms.keySet()) {
      graded.put(i, getHomogeneousPart(i));
    }
    return graded;
  }
  public int vars() {
    return vars;
  }
  
  
  /*
  Modifying methods.
  */
  
  public void addMonomial(MultiDegree d, BigInteger a, Ring pr) {
    pr.addMonomial(this, d, a);
  } 
  
  
  
  /*
      Polynomial.Ring
  */
  
  /**
   * Ring contains the arithmetic operations for polynomials.
   * There is a field corresponding to degree truncation,
   * and one indicating the modulus when the coefficient ring is a cyclic group.
   */
  static public class Ring {
    
    // ring parameters
    private MultiDegree variables;                                                   // The degrees of the generating variables                             
    private MultiDegree truncation;                                                  // If the exponent of x_i is at least truncation.get(i), a term is set to 0
    private Integer modulus;                                                             // The modulus won't need to be as large as BigInteger
    // helper builder
    private MultiDegree.Builder mb;
    
    /**
     * Constructs a free integral polynomial ring with vars variables.
     * @param vars 
     */
    public Ring(int vars) {
      mb = new MultiDegree.Builder(vars);
      this.variables = mb.setAll(1).build();
      List<Integer> trunc = new ArrayList<>();
      for (int i = 0; i < vars; i++) {
        trunc.add(Integer.MAX_VALUE);
      }
      truncation = mb.set(trunc).build();
      modulus = null;
    }
    /**
     * Constructs an integral polynomial ring with vars variables,
     * truncated above truncation.
     * @param truncation 
     */
    public Ring(MultiDegree truncation) {
      mb = new MultiDegree.Builder(truncation.vars());
      variables = mb.setAll(1).build();
      this.truncation = truncation;
      modulus = null;
    }
    /**
     * Constructs an integral polynomial ring 
     * where the variables have the specified degrees and
     * are truncated above truncation.
     * @param variables
     * @param truncation 
     */
    public Ring(MultiDegree variables, MultiDegree truncation) {
      this(variables, truncation, null);
    }
    /**
     * Constructs a polynomial ring over Z/modulus, 
     * where the variables have the specified degrees and
     * are truncated above truncation.
     * @param variables
     * @param truncation
     * @param modulus 
     */
    public Ring(MultiDegree variables, MultiDegree truncation, Integer modulus) {
      this.variables = variables;
      this.truncation = truncation;
      this.modulus = modulus;
      mb = new MultiDegree.Builder(variables.vars());
    }
    
    public void setModulus(int m) {
      if (m <= 0) {
        throw new IllegalArgumentException();
      }
      this.modulus = m;
    }
    
    public MultiDegree variables() {
      return variables;
    }
    public MultiDegree truncation() {
      return truncation;
    }
    public int vars() {
      return variables.vars();
    }
    public Integer modulus() {
      return modulus;
    }
    
    /*
    Ring constants.
    */
    
    /**
     * Returns a Polynomial with a single term: the zero MultiDegree
     * with coefficient 1.
     * @return 
     */
    public Polynomial one() {
      return new Polynomial(mb.zero().build(), BigInteger.ONE);
    }
    public Polynomial one(int n) {
      Polynomial one = new Polynomial(
              mb.zero().setVars(n).build(),
              BigInteger.ONE);
      mb.setVars(truncation.vars());
      return one;
    }
    /**
     * Returns a Polynomial with no terms.
     * @return 
     */
    public Polynomial zero() {
      return new Polynomial(truncation.vars());
    }
    
    
    public Polynomial reduce(Polynomial p) {
      Polynomial q = new Polynomial(p.vars);
      for (Map<MultiDegree, BigInteger> homTerms : p.terms.values()) {
        for (Map.Entry<MultiDegree, BigInteger> entry : homTerms.entrySet()) {
          BigInteger r = entry.getValue();
          if (modulus != null) r = r.mod(BigInteger.valueOf(modulus));
          if (entry.getKey().exceeds(truncation) || r.equals(BigInteger.ZERO)) {
            continue;
          }
          addMonomial(q, entry.getKey(), r);
        }
      }
      return q;
    }
    
    /*
    Ring arithmetic.
    */
    
    /**
     * Returns a new polynomial which represents the sum of p and q.
     * Requires p and q to have the same number of variables as the Ring.
     * @param p
     * @param q
     * @return 
     */
    public Polynomial plus(Polynomial p, Polynomial q) {
      if (p.vars != truncation.vars() || q.vars != truncation.vars()) {
        throw new IllegalArgumentException();
      }
      Polynomial sum = new Polynomial(p);
      for (Integer i : q.terms.keySet()) {
        for (MultiDegree d : q.terms.get(i).keySet()) {
          addMonomial(sum, d, q.terms.get(i).get(d));
        }
      }
      return sum;
    }
    /**
     * alters p and returns it.  
     * Assumes none of the inputs are null, that p.vars = d.vars, and that a != 0.
     * @param p
     * @param d
     * @param a
     * @return 
     */
    private Polynomial addMonomial(Polynomial p, MultiDegree d, BigInteger a) {
      int degree = d.total();
      // If p has no terms of the right degree, it needs a receptical for them
      if (p.terms.get(degree) == null) {
        Map<MultiDegree, BigInteger> homTerms = new HashMap<>();
        p.terms.put(degree, homTerms);
      }
      // If p does not have a d term, we only need to put a there.
      BigInteger b = p.terms.get(degree).get(d);
      if (b == null) {
        p.terms.get(degree).put(d, a);
        return p;
      }
      // If p's coefficient is non-zero, consider its sum with a (modulo modulus).
      BigInteger c = addCoefficients(a, b);
      // If the sum is 0 we need to remove the term from p, 
      // and remove the holder for homogeoneous terms of this degree if there are none left.
      if (c.equals(BigInteger.ZERO)) {
        p.terms.get(degree).remove(d);
        if (p.terms.get(degree).isEmpty()) p.terms.remove(degree);
        return p;
      }
      // Otherwise, in the "ideal" situation, you just add the coefficients. 
      p.terms.get(degree).put(d, c);
      return p;
    }
    private BigInteger addCoefficients(BigInteger a, BigInteger b) {
      BigInteger c = a.add(b);
      if (modulus != null) c = c.mod(BigInteger.valueOf(modulus));
      return c;
    }
    /**
     * Returns a new polynomial whose value is p times q, with truncation.
     * Requires p and q to have the same number of variables as the Ring.
     * @param p
     * @param q
     * @return 
     */
    public Polynomial times(Polynomial p, Polynomial q) {
      if (p.vars != truncation.vars() || q.vars != truncation.vars()) {
        throw new IllegalArgumentException();
      }
      Polynomial prod = new Polynomial(p.vars);
      if (p.isZero() || q .isZero()) {
        return prod;
      }
      for (Map<MultiDegree, BigInteger> homTerms : p.terms.values()) {
        for (Map.Entry<MultiDegree, BigInteger> entry : homTerms.entrySet()) {
          prod = plus(prod, timesMonomial(q, entry.getKey(), entry.getValue()));
        }
      }
      return prod;
    }
    /**
     * Returns a new polynomial whose entries are those of p multiplied
     * by the monomial given by d and a.
     * @param p
     * @param d
     * @param a
     * @return 
     */
    private Polynomial timesMonomial(Polynomial p, MultiDegree d, BigInteger a) {
      Polynomial prod = new Polynomial (p.vars);
      for (Map<MultiDegree, BigInteger> homTerm : p.terms.values()) {
        for (Map.Entry<MultiDegree, BigInteger> entry : homTerm.entrySet()) {
          MultiDegree newDegree = MultiDegree.add(entry.getKey(), d);
          if (truncation != null && newDegree.exceeds(truncation)) {
            continue;
          }
          addMonomial(prod, newDegree, multiplyCoefficient(a, entry.getValue()));
        }
      }
      return prod;
    }
    private BigInteger multiplyCoefficient(BigInteger a, BigInteger b) {
      BigInteger c = a.multiply(b);
      if (modulus != null) c = c.mod(BigInteger.valueOf(modulus));
      return c;
    }
    
    public Polynomial tensor(Polynomial p, Polynomial q) {
      Polynomial tensor = new Polynomial(p.vars + q.vars);
      //TODO make this iteration better
      for (Map<MultiDegree, BigInteger> homTermP : p.terms.values()) {
        for (Map.Entry<MultiDegree, BigInteger> entryP : homTermP.entrySet()) {
          for (Map<MultiDegree, BigInteger> homTermQ : q.terms.values()) {
            for (Map.Entry<MultiDegree, BigInteger> entryQ : homTermQ.entrySet()) {
              BigInteger c = multiplyCoefficient(entryP.getValue(), entryQ.getValue());
              if (c.equals(BigInteger.ZERO)) {
                continue;
              }
              addMonomial(
                  tensor, 
                  MultiDegree.concat(entryP.getKey(), entryQ.getKey()),
                  c
              );
            }
          }
        }
      }
      return tensor;
    }
    
    public Polynomial scale(Polynomial p, BigInteger a) {
      Polynomial q = new Polynomial(mb.zero().build(), a);
      return times(p, q);
    }
    
    public static Ring tensor(Ring r, Ring s) {
      MultiDegree newVariables  = MultiDegree.concat(r.variables,  s.variables);
      MultiDegree newTruncation = MultiDegree.concat(r.truncation, s.truncation);
      Integer newModulus;
      if      (r.modulus == null) newModulus = s.modulus;
      else if (s.modulus == null) newModulus = r.modulus;
      else {
        // compute gcd, trick from StackOverflow
        BigInteger b1 = BigInteger.valueOf(s.modulus);
        BigInteger b2 = BigInteger.valueOf(r.modulus);
        BigInteger gcd = b1.gcd(b2);
        newModulus = gcd.intValue();
      }
      
      return new Ring(newVariables, newTruncation, newModulus);
    }
    
  }
}
