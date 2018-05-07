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
   * 
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
   * 
   * @param cRing
   * @param variables
   * @param truncation 
   */
  public PolyRing(C cRing, MultiDegree variables, MultiDegree truncation) {
    this(cRing, variables, truncation, new MultiDegree.Builder());
  }
  /**
   * 
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
   * 
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
  
  
  public C cRing() {
    return cRing;
  }
  public MultiDegree truncation() {
    return truncation;
  }
  public MultiDegree variables() {
    return variables;
  }
  public int vars() {
    return variables.vars();
  }
  
  public Element zero() {
    return makeElement();
  }
  public Element one() {
    MultiDegree zero = mb.zero().setVars(variables.vars()).build();
    return makeElement(zero, cRing.one());
  }
  
  public Element add(Element p, Element q) {
    if (p.vars != q.vars) 
      throw new IllegalArgumentException();
    Element sum = makeElement(p);
    q.terms.entrySet().stream().forEach(
        entry -> addMonomial(sum, entry.getKey(),entry.getValue()));
    return sum;
  }
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
  
  public Element multiply(Element p, Element q) {
    if (p.vars != q.vars) 
      throw new IllegalArgumentException();
    Element prod = zero();
    for (Map.Entry<MultiDegree, C> entry : q.terms.entrySet()) {
      prod = add(prod, timesMonomial(p, entry.getKey(), entry.getValue()));
    }
    return prod;
  }
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
  
  public Element makeElement() {
    return new Element(this);
  }
  public Element makeElement(MultiDegree d, C a) {
    return new Element(this, d, a);
  }
  public Element makeElement(Element p) {
    return new Element(this, p);
  }
  
  public class Element {
    protected Map<MultiDegree, C> terms;
    private final int vars;
    private final PolyRing<C> domain;
    
    
    private Element(PolyRing<C> ring) {
      this.domain = ring;
      terms = new HashMap<>();
      vars = variables.vars();
    }
    private Element(PolyRing<C> ring, MultiDegree d, C a) {
      this(ring);
      terms.put(d, a);
    }
    private Element(PolyRing<C> ring, Element p) {
      this(ring);
      terms = new HashMap<>(p.terms);
    }
    
    @Override
    public boolean equals(Object o) {
      if (o instanceof PolyRing.Element) 
        return ( vars == ((Element)o).vars && ((Element)o).terms.equals(terms) );
      return false;
    }
    @Override
    public int hashCode() {
      return terms.hashCode();
    }
    @Override
    public String toString() {
      //TODO
      return null;
    }
    
    public PolyRing<C> domain() {
      return domain;
    }
    
    public boolean isZero() {
      return terms.isEmpty();
    }
    public C get(MultiDegree d) {
      C a = terms.get(d);
      return (a == null) ? cRing.zero() : a;
    }
    
    public SortedMap<Integer, Element> getHomogeneousParts() {
      SortedMap<Integer, Element> parts = new TreeMap<>();
      for (MultiDegree d : terms.keySet()) {
        int total = d.total();
        if (parts.get(total) == null) {
          parts.put(total, domain.zero());
        }
        parts.get(total).terms.put(d, terms.get(d));
      }
      return parts;
    }
    
//    public Element addMonomial(MultiDegree d, C a) {
//      return domain.addMonomial(this, d, a);
//    }(
    
  }
  
}
