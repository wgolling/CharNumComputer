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

/**
 *
 * @author William Gollinger
 * @param <C>
 */
public class PolyRing<C extends Coefficient<C>> {
  
  private final C cRing;
  
  private MultiDegree variables;
  private MultiDegree truncation;
  protected MultiDegree.Builder mb;
  
  /**
   * Constructs a PolyRing with given coefficient ring, variables,
   * and truncation, with the option to pass a MultiDegree.Builder.
   * @param cRing
   * @param variables
   * @param truncation
   * @param mb 
   */
  public PolyRing(C cRing, MultiDegree variables, MultiDegree truncation,
                  MultiDegree.Builder mb) {
    if (variables.vars() != truncation.vars())
      throw new IllegalArgumentException();
    if (cRing == null || variables == null || truncation == null || mb == null)
      throw new NullPointerException();
    this.cRing = cRing;
    this.variables = variables;
    this.truncation = truncation;
    this.mb = mb;
  }
  /**
   * Constructs a PolyRing with given coefficient ring, variables, and truncation.
   * @param cRing
   * @param variables
   * @param truncation
   */
  public PolyRing(C cRing, MultiDegree variables, MultiDegree truncation) {
    this(cRing, variables, truncation, new MultiDegree.Builder());
  }
  /**
   * Constructs a PolyRing with the given coefficient ring and truncation.
   * The degrees of the variables are 1 by default.
   * @param cRing
   * @param truncation 
   */
  public PolyRing(C cRing, MultiDegree truncation) {
    if (cRing == null || truncation == null)
      throw new NullPointerException();
    this.cRing = cRing;
    mb = new MultiDegree.Builder(truncation.vars());
    variables  = mb.increment().build();
    this.truncation = truncation;
  }
  /**
   * Constructs a PolyRing with the given coefficient ring.
   * The degrees of the variables are 1 by default, and
   * the truncations are Integer.MAX_VALUE.
   * @param cRing
   * @param vars 
   */
  public PolyRing(C cRing, int vars) {
    if (cRing == null) 
      throw new NullPointerException();
    this.cRing = cRing;
    mb = new MultiDegree.Builder(vars);
    variables  = mb.increment().build();
    truncation = mb.maxAll().build();
  }
  
  
  /**
   * Returns coefficient ring.
   * @return 
   */
  public C cRing() {
    return cRing;
  }
  /**
   * Returns truncation.
   * @return 
   */
  public MultiDegree truncation() {
    return truncation;
  }
  /**
   * Returns a MultiDegree representing the degrees of the ring's variables. 
   * @return 
   */
  public MultiDegree variables() {
    return variables;
  }
  /**
   * Returns the number of variables.
   * @return 
   */
  public int vars() {
    return variables.vars();
  }
  
  /**
   * Returns the 0 polynomial.
   * @return 
   */
  public Element zero() {
    return makeElement();
  }
  /**
   * Returns the 1 polynomial.
   * @return 
   */
  public Element one() {
    MultiDegree zero = mb.zero().setVars(variables.vars()).build();
    return makeElement(zero, cRing.one());
  }
  /**
   * Returns a polynomial which is the sum of p and q.
   * @param p
   * @param q
   * @return 
   */
  public Element add(Element p, Element q) {
    if (p.vars != q.vars) 
      throw new IllegalArgumentException();
    Element sum = makeElement(p);
    q.terms.entrySet().stream().forEach(
        entry -> addMonomial(sum, entry.getKey(),entry.getValue()));
    return sum;
  }
  /**
   * Alters p by adding the term (d, a).
   * Assumes d and p have the same number of variables.
   * @param p
   * @param d
   * @param a 
   */
  private void addMonomial(Element p, MultiDegree d, C a) {
    C b = p.terms.get(d);
    if (b == null) {
      p.terms.put(d, a);
      return;
    }
    C sum = a.plus(b);
    if (sum.isZero()) {
      p.terms.remove(d);
      return;
    }
    p.terms.put(d, sum);    
  }
  
  /**
   * Returns a polynomial which is the products of p and q.
   * @param p
   * @param q
   * @return 
   */
  public Element multiply(Element p, Element q) {
    if (p.vars != q.vars) 
      throw new IllegalArgumentException();
    Element prod = zero();
    for (Map.Entry<MultiDegree, C> entry : q.terms.entrySet()) {
      prod = add(prod, timesMonomial(p, entry.getKey(), entry.getValue()));
    }
    return prod;
  }
  /**
   * Returns a polynomial with is the product of p and the term (d, a).
   * Assumes d and p have the same number of variables.
   * @param p
   * @param d
   * @param a
   * @return 
   */
  private Element timesMonomial(Element p, MultiDegree d, C a) {
    Element scaled = zero();
    for (Map.Entry<MultiDegree, C> entry : p.terms.entrySet()) {
      MultiDegree newDegree = MultiDegree.add(d, entry.getKey());
      C newC = a.times(entry.getValue());
      if (newC.isZero() || newDegree.exceeds(truncation)) 
        continue;
      scaled.terms.put(newDegree, newC);
    }
    return scaled;
  }  
  
  /**
   * Makes the 0 polynomial.
   * @return 
   */
  public Element makeElement() {
    return new Element(this);
  }
  /**
   * Makes the monomial with MultiDegree d and Coefficient a.
   * @param d
   * @param a
   * @return 
   */
  public Element makeElement(MultiDegree d, C a) {
    if (!variables.divides(d))
      throw new IllegalArgumentException();
    if (a.isZero())
      return makeElement();
    
    return new Element(this, d, a);
  }
  /**
   * Make a duplicate of the polynomial p.
   * @param p
   * @return 
   */
  public Element makeElement(Element p) {
    return new Element(this, p);
  }
  
  
  /*
  Inner class Element.
  */
  
  /**
   * Represent a polynomial, specifically an element of an 
   * instance of PolyRing.
   */
  public class Element {
    
    protected Map<MultiDegree, C> terms;
    private final int vars;
    private final PolyRing<C> domain;
    
    /**
     * Constructs the 0 element of ring.
     * @param ring 
     */
    private Element(PolyRing<C> ring) {
      this.domain = ring;
      terms = new HashMap<>();
      vars = variables.vars();
    }
    /**
     * Constucts the monomial in ring given by (d, a).
     * @param ring
     * @param d
     * @param a 
     */
    private Element(PolyRing<C> ring, MultiDegree d, C a) {
      this(ring);
      terms.put(d, a);
    }
    /**
     * Makes a copy of the ring element p.
     * @param ring
     * @param p 
     */
    private Element(PolyRing<C> ring, Element p) {
      this(ring);
      terms = new HashMap<>(p.terms);
    }
    /**
     * Two polynomials are equal if they are in the same ring
     * and they have the same terms.
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o) {
      if (o instanceof PolyRing.Element) 
        return (domain == ((Element)o).domain() 
                && ((Element)o).terms.equals(terms) );
      return false;
    }
    @Override
    public int hashCode() {
      return terms.hashCode();
    }
    @Override
    public String toString() {
      return StringMaker.polyToString(this);
    }
    
    /**
     * Returns the PolyRing that this is a member of.
     * @return 
     */
    public PolyRing<C> domain() {
      return domain;
    }
    /**
     * A polynomial is zero if it has no terms.
     * @return 
     */
    public boolean isZero() {
      return terms.isEmpty();
    }
    /**
     * Returns the coefficient of the MultiDegree d.
     * @param d
     * @return 
     */
    public C get(MultiDegree d) {
      C a = terms.get(d);
      return (a == null) ? cRing.zero() : a;
    }
    public Map<MultiDegree, C> getTerms() {
      return new HashMap<>(terms);
    }
    /**
     * Returns a sorted map of homogeneous parts.
     * @return 
     */
    public SortedMap<Integer, Element> getHomogeneousParts() {
      SortedMap<Integer, Element> parts = new TreeMap<>();
      for (MultiDegree d : this.terms.keySet()) {
        int total = d.total();
        if (parts.get(total) == null) {
          parts.put(total, domain.zero());
        }
        parts.put(total, domain.add(
                parts.get(total), 
                domain.makeElement(d, terms.get(d))));
        }
      return parts;
    }
        
  }
  
}

class StringMaker {
  
  static String polyToString(PolyRing.Element p) {
    if (p.isZero()) 
      return "0";
    SortedMap<Integer, PolyRing.Element> homParts = p.getHomogeneousParts();
    Iterator<PolyRing.Element> itr = homParts.values().iterator();
    String answer = homPartToString(itr.next());
    while(itr.hasNext()) {
      answer += "\n+ " + homPartToString(itr.next());
    }
    return answer;
  }
  /**
   * Assumes hom is non-zero and homogenous.
   * @param hom
   * @return 
   */
  private static String homPartToString(PolyRing.Element hom) {
    if (hom.isZero())
      return "";
    Iterator<Map.Entry<MultiDegree, Coefficient>> itr = hom.terms.entrySet().iterator();
    MultiDegree variables = hom.domain().variables();
    String answer = monomialToString(itr.next(), variables);
    while(itr.hasNext()) {
      answer += " + " + monomialToString(itr.next(), variables);
    }
    return answer;
  }
  /**
   * Assumes variables.divides(entry.getKey())
   * @param entry
   * @param variables
   * @return 
   */
  private static String monomialToString(
          Map.Entry<MultiDegree, Coefficient> entry, 
          MultiDegree variables) {
    if (entry.getValue().isZero())
      return "";
    MultiDegree d = entry.getKey();
    String vars = "";
    for (Integer i = 0; i < d.vars(); i++) {
      if(d.get(i) == 0)
        continue;
      vars += "u" 
              + subscript(i.toString()) 
              + superscript(String.valueOf(d.get(i) / variables.get(i)));
    }
    Coefficient c = entry.getValue();
    if (!vars.isEmpty() && c.isOne())
      return vars;
    return c.toString() + vars;
  }
  private static String superscript(String str) {
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
  private static String subscript(String str) {
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
}
