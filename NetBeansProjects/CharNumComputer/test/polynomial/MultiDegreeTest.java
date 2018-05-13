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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class MultiDegreeTest {
  
  MultiDegree.Builder mb;
  MultiDegree empty;
  MultiDegree test1;
  MultiDegree test2;
  
  public MultiDegreeTest() {
    mb = new MultiDegree.Builder();
    empty = mb.build();
    test1 = mb.setVars(3).set(0, 1).set(1, 3).set(2, 2).build();
    test2 = mb.setVars(2).set(0, 5).set(1, 4).build();
  }
  
  @Before
  public void setUp() {
    mb.reset();
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of equals method, of class MultiDegree.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    List<Integer> d = new ArrayList<>();
    d.add(1);
    d.add(3);
    d.add(2);
    assert(test1.equals(mb.set(d).build()));
    mb.setVars(2);
    assertEquals(2, mb.build().vars());
    assert(test2.equals(mb.set(0, 5).set(1, 4).build()));
  }

  /**
   * Test of vars method, of class MultiDegree.
   */
  @Test
  public void testVars() {
    System.out.println("vars");
    assertEquals(test1.vars(), 3);
    assertEquals(test2.vars(), 2);
  }

  /**
   * Test of lessThan method, of class MultiDegree.
   */
  @Test
  public void testLessThan() {
    System.out.println("lessThan");
    mb.setVars(3);
    assert(test1.exceeds(mb.set(0, 2).set(1, 4).set(2, 1).build()));
    assert(!(test1.exceeds(test1)));
  }

  /**
   * Test of concat method, of class MultiDegree.
   */
  @Test
  public void testConcat() {
    System.out.println("concat");
    MultiDegree d = MultiDegree.concat(test1, test2);
    assertEquals(d.vars(), 5);
    List<Integer> degrees = new ArrayList<>();
    degrees.add(1);
    degrees.add(3);
    degrees.add(2);
    degrees.add(5);
    degrees.add(4);
    assert(d.equals(mb.set(degrees).build()));
  }

  /**
   * Test of get method, of class MultiDegree.
   */
  @Test
  public void testGet() {
    System.out.println("get");
    assertEquals(1, test1.get(0));
    assertEquals(3, test1.get(1));
    assertEquals(2, test1.get(2));
  }

  /**
   * Test of add method, of class MultiDegree.
   */
  @Test
  public void testAdd() {
    System.out.println("add");
    MultiDegree test3 = mb.setVars(3).setAll(2).build();
    List<Integer> sum = new ArrayList<>();
    sum.add(3);
    sum.add(5);
    sum.add(4);
    assert(mb.set(sum).build().equals(MultiDegree.add(test1, test3)));
  }

  /**
   * Test of toString method, of class MultiDegree.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    MultiDegree d = mb.setVars(3).set(0, 1).set(1,2).set(2,3).build();
    assert(d.toString().equals("[1, 2, 3]"));
  }

  /**
   * Test of hashCode method, of class MultiDegree.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
  }

  /**
   * Test of copy method, of class MultiDegree.
   */
  @Test
  public void testCopy() {
    System.out.println("copy");
    MultiDegree d = mb.build();
    assert(d.copy().equals(d));
  }

  /**
   * Test of isZero method, of class MultiDegree.
   */
  @Test
  public void testIsZero() {
    System.out.println("isZero");
    MultiDegree d = mb.setVars(3).zero().build();
    assert(d.isZero());
    MultiDegree e = mb.setVars(0).build();
    assert(e.isZero());
    MultiDegree f  = mb.setVars(8).set(3, 2).build();
    assert(!f.isZero());
  }

  /**
   * Test of total method, of class MultiDegree.
   */
  @Test
  public void testTotal() {
    System.out.println("total");
    assertEquals(test1.total(), 6);
    assertEquals(mb.setVars(0).build().total(), 0);
  }

  /**
   * Test of exceeds method, of class MultiDegree.
   */
  @Test
  public void testExceeds() {
    System.out.println("exceeds");
    MultiDegree d = mb.setVars(3).zero().set(2, 3).build();
    assert(d.exceeds(test1));
  }

  /**
   * Test of divides method, of class MultiDegree.
   */
  @Test
  public void testDivides() {
    System.out.println("divides");
    MultiDegree d = mb.setVars(3).set(0, 2).set(1, 6).set(2, 4).build();
    assert(test1.divides(d));
  }

  /**
   * Test of zeros method, of class MultiDegree.
   */
  @Test
  public void testZeros() {
    System.out.println("zeros");
    assert(MultiDegree.zeros(20).isZero());
  }

  /**
   * Test of pad method, of class MultiDegree.
   */
  @Test
  public void testPad() {
    System.out.println("pad");
    MultiDegree result = MultiDegree.pad(test2, 2, 3);
    MultiDegree expResult = mb.zero().setVars(7).set(2,5).set(3,4).build();
    assert(expResult.equals(result));
  }

  /**
   * Test of raise method, of class MultiDegree.
   */
  @Test
  public void testRaise() {
    System.out.println("raise");
    MultiDegree result = MultiDegree.raise(test2);
    MultiDegree expResult = mb.zero().setVars(2).set(0,6).set(1,5).build();
    assert(expResult.equals(result));
  }

  /**
   * Test of lower method, of class MultiDegree.
   */
  @Test
  public void testLower() {
    System.out.println("lower");
    MultiDegree result = MultiDegree.lower(test2);
    MultiDegree expResult = mb.zero().setVars(2).set(0,4).set(1,3).build();
    assert(expResult.equals(result));
  }

  /**
   * Test of empty method, of class MultiDegree.
   */
  @Test
  public void testEmpty() {
    System.out.println("empty");
    MultiDegree empty = MultiDegree.empty();
    assert(empty.vars() == 0);
  }

  /**
   * Test of isBounded method, of class MultiDegree.
   */
  @Test
  public void testIsBounded() {
    System.out.println("isBounded");
    mb.setVars(2);
    assert(mb.set(0,4).set(1,4).build().isBounded());
    assert(!mb.set(0, Integer.MAX_VALUE).build().isBounded());
  }
  
}
