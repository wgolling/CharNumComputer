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
  private final Map<MultiDegree, BigInteger> terms;      // some of the coefficients can get quite large.
  private final int vars;
  //private final Ring domain;
  
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
    terms = new HashMap<>();
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
    terms.put(d, a);
  }
  /**
   * Returns a copy of polynomial p.
   * @param p 
   */
  public Polynomial(Polynomial p) {
    terms = new HashMap<>(p.terms);
    vars = p.vars;
  }
  
  /*
  Utiliy methods.
  */
  
  @Override
  public String toString() {
    if (this.isZero()) return "0";
    String answer = "";
    
    Map<Integer, Polynomial> hParts = getGraded();
    
    boolean first = true;
    for (Integer i : hParts.keySet()) {
      if (!first) answer += "\n+ ";
      else first = false;
      answer += homogeneousTermToString(hParts.get(i).terms);
    }
    return answer;
  }
  /**
   * Assumes homTerm is non-empty.
   * @param homTerm
   * @return 
   */
  private String homogeneousTermToString(Map<MultiDegree, BigInteger> homTerm) {
    String answer = "";
    
    boolean first = true;
    for (Map.Entry<MultiDegree, BigInteger> entry : homTerm.entrySet()) {
      if (!first) answer += " + ";
      else first = false;
      answer += monomialToString(entry.getKey(), entry.getValue());
    }
    return answer;  
  }
  private String monomialToString(MultiDegree d, BigInteger a) {
    String answer = (a.equals(BigInteger.ONE)) ? "" : a.toString();
    for (Integer i = 0; i < d.vars(); i++) {
      if (d.get(i) == 0) continue;
      answer += "v" + subscript(i.toString()) + superscript(String.valueOf(d.get(i)));
    }
    return (answer.equals("")) ? "1" : answer;
  }
  private String superscript(String str) {
    str = str.replaceAll("0", "⁰");
    str = str.replaceAll("1", "¹");
    str = str.replaceAll("2", "²");
    str = str.replaceAll("3", "³");
    str = str.replaceAll("4", "⁴");
    str = str.replaceAll("5", "⁵");
    str = str.replaceAll("6", "⁶");
    str = str.replaceAll("7", "⁷");
    str = str.replaceAll("8", "⁸");
    str = str.replaceAll("9", "⁹");         
    return str;
  }
  private String subscript(String str) {
    str = str.replaceAll("0", "₀");
    str = str.replaceAll("1", "₁");
    str = str.replaceAll("2", "₂");
    str = str.replaceAll("3", "₃");
    str = str.replaceAll("4", "₄");
    str = str.replaceAll("5", "₅");
    str = str.replaceAll("6", "₆");
    str = str.replaceAll("7", "₇");
    str = str.replaceAll("8", "₈");
    str = str.replaceAll("9", "₉");
    return str;
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


  public int vars() {
    return vars;
  }

  /**
   * Returns the coefficient of d as a BigInteger.
   * @param d
   * @return 
   */
  public BigInteger get(MultiDegree d) {
    BigInteger a = terms.get(d);
    return (a == null) ? BigInteger.ZERO : a;  
  }
  
  /**
   * Returns a new polynomial consisting of the degree i terms of p.
   * @param d
   * @return 
   */
  public Polynomial getHomogeneousPart(int d) {
    return getGraded().get(d);
  }
  public Map<Integer, Polynomial> getGraded() {
    Map<Integer, Polynomial> graded = new HashMap<>();
    for (Map.Entry<MultiDegree, BigInteger> entry : terms.entrySet()) {
      Integer degree = entry.getKey().total();
      Polynomial homPart = graded.get(degree);
      if (homPart == null) {
        graded.put(entry.getKey().total(), new Polynomial(vars));
      }
      graded.get(degree).addMonomial(entry.getKey(), entry.getValue(), new Ring(vars));
    }

    return graded;
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
    
    public void setMudulus(Integer m) {
      this.modulus = m;
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
      
      for (Map.Entry<MultiDegree, BigInteger> entry : p.terms.entrySet()) {
        BigInteger r = reduceCoefficient(entry.getValue());
        if (r.equals(BigInteger.ZERO) || ! validateMultiDegree(entry.getKey())) continue;
        addMonomial(q, entry.getKey(), r);
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
    private Polynomial addMonomial(Polynomial p, 
                                    MultiDegree d, 
                                    BigInteger a) {
            
      BigInteger b = p.terms.get(d);
      // If p.terms does not have an entry with key d, add the entry (d, a).
      if (b == null) {
        p.terms.put(d, a);
        return p;
      }
      // If the coefficient of d is non-zero, add it to a.
      BigInteger c = a.add(b);
      if (modulus != null) {
        c = c.mod(BigInteger.valueOf(modulus));
      }
      // If the sum is 0 we need to remove the term from p, and if 
      // there are no more terms of that degree we remove their receptical.
      if (c.equals(BigInteger.ZERO)) {
        p.terms.remove(d);
        return p;
      }
      
      // Otherwise, in the "ideal" situation, you just add the coefficients. 
      p.terms.put(d, c);
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
    public Polynomial plus(Polynomial p, 
                            Polynomial q) {
      if (p.vars != truncation.vars() || q.vars != truncation.vars()) {
        throw new IllegalArgumentException();
      }
      if (p.isZero()) return q;
      if (q.isZero()) return p;
      
      Polynomial sum = new Polynomial(p);
      for (Map.Entry<MultiDegree, BigInteger> entry : q.terms.entrySet()) {
        addMonomial(sum, entry.getKey(), entry.getValue());
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
    private Polynomial timesMonomial(Polynomial p, 
                                                    MultiDegree d, 
                                                    BigInteger a) {
      Polynomial prod = new Polynomial(p.vars);
      for (Map.Entry<MultiDegree, BigInteger> entry : p.terms.entrySet()) {
        MultiDegree newDegree = MultiDegree.add(entry.getKey(), d);
        BigInteger b = a.multiply(entry.getValue());
        if (b.equals(BigInteger.ZERO) || !validateMultiDegree(d)) continue;
        addMonomial(prod, newDegree, b);
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
      if (p.vars != truncation.vars() || q.vars != truncation.vars()) 
        throw new IllegalArgumentException();
      Polynomial prod = new Polynomial(p.vars);
      if (p.isZero() || q.isZero()) 
        return prod;
      for (Map.Entry<MultiDegree, BigInteger> entry : p.terms.entrySet()) {
        prod = plus(prod, timesMonomial(q, entry.getKey(), entry.getValue()));
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
      for (Map.Entry<MultiDegree, BigInteger> pTerm : p.terms.entrySet()) {
        for (Map.Entry<MultiDegree, BigInteger> qTerm : q.terms.entrySet()) {
          BigInteger c = pTerm.getValue().multiply(qTerm.getValue());
          if (c.equals(BigInteger.ZERO)) continue;
          addMonomial(temp, 
                      MultiDegree.concat(pTerm.getKey(), qTerm.getKey()),
                      c);
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
      if (r.modulus == null ) {}
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
