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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class PartitionTest {
  
  Partition p;
  Partition empty;
  
  public PartitionTest() {
    List<Integer> test = Arrays.asList(new Integer[]{3, 6, 2, 4, 3});
    p = new Partition(test);
    empty = new Partition();
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of getNumbers method, of class Partition.
   */
  @Test
  public void testGetNumbers() {
    System.out.println("getNumbers");
    List<Integer> numbers = p.getNumbers();
    assert(2 == numbers.get(0));
    assert(3 == numbers.get(1));
    assert(3 == numbers.get(2));
    assert(4 == numbers.get(3));
    assert(6 == numbers.get(4));
    numbers = empty.getNumbers();
    assert(numbers.isEmpty());
  }

  /**
   * Test of toString method, of class Partition.
   */
  @Test
  public void testToString() {
  }

  /**
   * Test of equals method, of class Partition.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    Partition q = new Partition(Arrays.asList(new Integer[]{2, 3, 3, 4, 6}));
    assert(p.equals(q));
  }

  /**
   * Test of hashCode method, of class Partition.
   */
  @Test
  public void testHashCode() {
  }

  /**
   * Test of sum method, of class Partition.
   */
  @Test
  public void testSum() {
    System.out.println("sum");
    assertEquals(18, p.sum());
    assertEquals(0, empty.sum());
  }

  /**
   * Test of max method, of class Partition.
   */
  @Test
  public void testMax() {
    System.out.println("max");
    assertEquals(6, p.max());
    assertEquals(0, empty.max());
  }

  /**
   * Test of merge method, of class Partition.
   */
  @Test
  public void testMerge() {
    System.out.println("merge");
    Partition q = new Partition(new Integer[]{6, 3, 1});
    q = Partition.merge(p, q);
    assert(1 == q.getNumbers().get(0));
    assert(q.equals(new Partition(
        new Integer[]{1, 2, 3, 3, 3, 4, 6, 6})));
    assert(Partition.merge(p, empty).equals(p));
  }
  
}
