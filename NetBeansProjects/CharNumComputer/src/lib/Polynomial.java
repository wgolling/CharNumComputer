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
package lib;

import java.util.*;
import java.math.BigInteger;

/**
 * Polynomial represents a graded polynomial, and supports arithmetic in
 * truncated polynomial rings with integer or finite cyclic coefficients.
 * @author William Gollinger
 */
public class Polynomial {
  /*
  terms represents a graded polynomial
  TODO Probably don't need SortedMap, I don't think I ever use that it's sorted.
  */
  private final SortedMap<Integer, Map<MultiDegree, BigInteger>> terms;      // some of the coefficients can get quite large.
  private final int vars;
  
  
  /*
  Constructors
  */
  
  /**
   * Returns a zero polynomial with vars variables.
   * @param vars 
   */
  public Polynomial(int vars) {
    if (vars < 0) {
      throw new IllegalArgumentException();
    }
    terms = new TreeMap<>();
    this.vars = vars;
  }
  /**
   * Returns a polynomial with one term, 
   * having MultiDegree d and coefficient a.
   * @param d
   * @param a 
   */
  public Polynomial(MultiDegree d, BigInteger a) {
    this(d.vars());
    if (d == null || a == null) {
      throw new NullPointerException();
    }
    Map<MultiDegree, BigInteger> monomial = new HashMap<>();
    monomial.put(d, a);
    terms.put(d.total(), monomial);
  }
  /**
   * Returns a copy of polynomial p.
   * @param p 
   */
  public Polynomial(Polynomial p) {
    terms = new TreeMap<>();
    for (Integer i : p.terms.keySet()) {
      terms.put(i, new HashMap<>(p.terms.get(i)));
    }
    vars = p.vars;
  }
  
  
  /*
  Utiliy methods.
  */
  
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
  
  public Polynomial addMonomial(MultiDegree d, BigInteger a, Ring pr) {
    return pr.addMonomial(this, d, a);
  } 
  
  
  
  /*
  Nested class Polynomial.Ring
  */
  
  /**
   * Ring contains the arithmetic operations for polynomials.
   * There are MultiDegree fields for the degrees of the variables and the truncation,
   * and an Integer indicating the modulus when the coefficient ring is a cyclic group.
   */
  static public class Ring {
    
    // ring parameters
    private MultiDegree variables;                                                   // The degrees of the generating variables                             
    private MultiDegree truncation;                                                  // If the exponent of x_i is at least truncation.get(i), a term is set to 0
    private Integer modulus;                                                             // The modulus won't need to be as large as BigInteger
    // helper builder
    private MultiDegree.Builder mb;
    
    
    /*
    Constructors.
    */
    
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
    
    
    /*
    Getting methods.
    */
    
    /**
     * Returns a reference to the variables MultiDegree.
     * @return 
     */
    public MultiDegree variables() {
      return variables;
    }
    /**
     * Returns a reference to the truncation MultiDegree.
     * @return 
     */
    public MultiDegree truncation() {
      return truncation;
    }
    /**
     * Returns the number of variables.
     * @return 
     */
    public int vars() {
      return variables.vars();
    }
    /**
     * Returns the modulus of the coefficient ring.  
     * Returns null if the coefficients are the Integers.
     * @return 
     */
    public Integer modulus() {
      return modulus;
    }
    
    /*
    Reduction methods.
    These take objects and conform them to the ring's parameters.
    */
    
    /**
     * If the ring's modulus is not null it reduces the input,
     * otherwise leaves it alone.
     * @param a
     * @return 
     */
    public BigInteger reduceCoefficient(BigInteger a) {
      return (modulus == null) ? a : a.mod(BigInteger.valueOf(modulus));
    }
    /**
     * A MultiDegree is valid iff it is divisible by the variables field
     * and does not exceed the truncation.
     * @param d
     * @return 
     */
    public boolean validateMultiDegree(MultiDegree d) {
      return (!(d.exceeds(truncation)) && variables.divides(d));
    }
    /**
     * Returns a new polynomial which is the term-wise reduction of p.
     * @param p
     * @return 
     */
    public Polynomial reduce(Polynomial p) {
      Polynomial q = new Polynomial(p.vars);
      for (Map<MultiDegree, BigInteger> homTerms : p.terms.values()) {
        for (Map.Entry<MultiDegree, BigInteger> entry : homTerms.entrySet()) {
          BigInteger r = reduceCoefficient(entry.getValue());
          if (r.equals(BigInteger.ZERO) || !validateMultiDegree(entry.getKey())) {
            continue;
          }
          addMonomial(q, entry.getKey(), r);
        }
      }
      return q;
    }
    
    /*
    Ring constants.
    */
    
    /**
     * Returns a Polynomial with no terms.
     * @return 
     */
    public Polynomial zero() {
      return new Polynomial(variables.vars());
    }
    /**
     * Returns a Polynomial with a single term: the zero MultiDegree
     * with coefficient 1.
     * @return 
     */
    public Polynomial one() {
      return new Polynomial(mb.zero().build(), BigInteger.ONE);
    }
    /**
     * Returns a One polynomial with a specified number of variables.
     * @param n
     * @return 
     */
    public Polynomial one(int n) {
      Polynomial one = new Polynomial(
              mb.zero().setVars(n).build(),
              BigInteger.ONE);
      mb.setVars(truncation.vars());
      return one;
    }
    
    /*
    Ring operations.
    */
    
    /**
     * Adds two BigIntegers (modulo the ring's modulus if it is not null).
     * @param a
     * @param b
     * @return 
     */
    private BigInteger addCoefficients(BigInteger a, BigInteger b) {
      BigInteger c = a.add(b);
      if (modulus != null) c = c.mod(BigInteger.valueOf(modulus));
      return c;
    }
    /**
     * Multiplies two BigIntegers (modulo the ring's modulus if it is not null).
     * @param a
     * @param b
     * @return 
     */
    private BigInteger multiplyCoefficient(BigInteger a, BigInteger b) {
      BigInteger c = a.multiply(b);
      if (modulus != null) c = c.mod(BigInteger.valueOf(modulus));
      return c;
    }
    /** 
     * Returns a new polynomial whose coefficients 
     * are those of p multiplied by a.
     * @param p
     * @param a
     * @return 
     */
    public Polynomial scale(Polynomial p, BigInteger a) {
      Polynomial constant = new Polynomial(mb.zero().build(), a);
      return times(p, constant);
    }
    
    /**
     * Adds the MultiDegree d with coefficient a to the polynomial p. 
     * This method alters p before returning it.
     * Assumes none of the inputs are null, that p.vars = d.vars, and that a != 0.
     * @param p
     * @param d
     * @param a
     * @return 
     */
    private Polynomial addMonomial(Polynomial p, MultiDegree d, BigInteger a) {
      
      int degree = d.total();
      // If p has no terms of the right degree, it needs a receptical for them.
      if (p.terms.get(degree) == null) {
        p.terms.put(degree, new HashMap<>());
      }
      
      BigInteger b = p.terms.get(degree).get(d);
      // If p.terms does not have an entry with key d, add the entry (d, a).
      if (b == null) {
        p.terms.get(degree).put(d, a);
        return p;
      }
      // If the coefficient of d is non-zero, add it to a.
      BigInteger c = addCoefficients(a, b);
      // If the sum is 0 we need to remove the term from p, and if 
      // there are no more terms of that degree we remove their receptical.
      if (c.equals(BigInteger.ZERO)) {
        p.terms.get(degree).remove(d);
        if (p.terms.get(degree).isEmpty()) p.terms.remove(degree);
        return p;
      }
      
      // Otherwise, in the "ideal" situation, you just add the coefficients. 
      p.terms.get(degree).put(d, c);
      return p;
    }    
    /**
     * Returns a new polynomial which represents the sum of p and q.
     * Throws IllegalArgumentException if 
     * p and q do not have the same number of variables as the ring.
     * @param p
     * @param q
     * @return 
     */
    public Polynomial plus(Polynomial p, Polynomial q) {
      if (p.vars != truncation.vars() || q.vars != truncation.vars()) {
        throw new IllegalArgumentException();
      }
      if (p.isZero()) return q;
      if (q.isZero()) return p;
      
      Polynomial sum = new Polynomial(p);
      for (Integer i : q.terms.keySet()) {
        for (MultiDegree d : q.terms.get(i).keySet()) {
          addMonomial(sum, d, q.terms.get(i).get(d));
        }
      }
      return sum;
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
          BigInteger b = multiplyCoefficient(a, entry.getValue());
          if (b.equals(BigInteger.ZERO) || !validateMultiDegree(d)) continue;
          addMonomial(prod, newDegree, b);
        }
      }
      return prod;
    }
    /**
     * Returns a new polynomial whose value is p times q.
     * Throws IllegalArgumentException if 
     * p and q do not have the same number of variables as the ring.
     * @param p
     * @param q
     * @return 
     */
    public Polynomial times(Polynomial p, Polynomial q) {
      if (p.vars != truncation.vars() || q.vars != truncation.vars()) {
        throw new IllegalArgumentException();
      }
      Polynomial prod = new Polynomial(p.vars);
      if (p.isZero() || q .isZero()) return prod;

      for (Map<MultiDegree, BigInteger> homTerms : p.terms.values()) {
        for (Map.Entry<MultiDegree, BigInteger> entry : homTerms.entrySet()) {
          prod = plus(prod, timesMonomial(q, entry.getKey(), entry.getValue()));
        }
      }
      return prod;
    }
    
    /**
     * Returns a new polynomial which is the "tensor product" of p and q.
     * The MultiDegrees from p and q are simply concatenated so the
     * variables and truncation fields of the ring play no role, but 
     * the coefficients are reduced using the modulus of the ring.
     * @param p
     * @param q
     * @return 
     */
    public Polynomial tensor(Polynomial p, Polynomial q) {
      Polynomial temp = new Polynomial(p.vars + q.vars);
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
                  temp, 
                  MultiDegree.concat(entryP.getKey(), entryQ.getKey()),
                  c
              );
            }
          }
        }
      }
      return temp;
    }
    
    /** 
     * Produces a new polynomial ring which is the
     * tensor product (over the integers) of r and s.
     * In effect, the variables and truncations are concatenated and
     * the coefficient ring is computed using the rules
     *    tensor(Z, G) = G, for G = Z or Z/n
     *    tensor(Z/n, Z/m) = Z/gcd(n,m)
     * @param r
     * @param s
     * @return 
     */
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
