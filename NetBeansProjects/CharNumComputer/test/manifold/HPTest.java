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

import java.util.Arrays;
import lib.*;
import polynomial.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class HPTest {
  
  public HPTest() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }


  /**
   * Test of toString method, of class HP.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    Manifold m = new HP(4);
    assert(m.toString().equals("HP(4)"));
  }
  
  /**
   * Test of characteristic classes of HP(n)
   */
  @Test
  public void testCharacteristicClasses() {
    System.out.println("testCharClasses");
    
    MultiDegree.Builder mb = new MultiDegree.Builder(1);
    
    Manifold m = new HP(2);
    PolyRing<BigInt> ring = m.cohomology();
    PolyRing<IntMod2> mod2Ring = m.mod2Cohomology();
    
    PolyRing<BigInt>.Element knownPont = ring.one();
    knownPont = ring.add(
            knownPont,
            ring.makeElement(mb.set(0, 4).build(), new BigInt(2)));
    knownPont = ring.add(
            knownPont,
            ring.makeElement(mb.set(0, 8).build(), new BigInt(7)));
    assert(m.pontClass().equals(knownPont));
    
    PolyRing<IntMod2>.Element knownSW = m.mod2Cohomology().one();
    knownSW = mod2Ring.add(
            knownSW,
            mod2Ring.makeElement(mb.set(0, 4).build(), new IntMod2(3)));
    knownSW = mod2Ring.add(
            knownSW,
            mod2Ring.makeElement(mb.set(0, 8).build(), new IntMod2(3)));
    assert(m.swClass().equals(knownSW));
    
    
    Manifold m2 = new HP(3);
    ring = m2.cohomology();
    knownPont = ring.one();
    knownPont = ring.add(
            knownPont,
            ring.makeElement(mb.set(0, 4).build(), new BigInt(4)));
    knownPont = ring.add(
            knownPont,
            ring.makeElement(mb.set(0, 8).build(), new BigInt(12)));
    knownPont = ring.add(
            knownPont,
            ring.makeElement(mb.set(0, 12).build(), new BigInt(8)));
    assert(m2.pontClass().equals(knownPont));
    
    assert(m2.swClass().equals(m2.mod2Cohomology().one()));
  }
  /**
   * Test chernClass() UnsupportedOperationException
   */
  @Test (expected = UnsupportedOperationException.class)
  public void testChernClassUnsupportedOpertation() {
    Manifold m = new HP(5);
    m.chernClass();
  }
  
  /**
   * Test of characteristic numbers of class HP(n)
   */
  @Test
  public void testCharacteristicNumbers() {
    System.out.println("testCharNumbers");
    
    Manifold m = new HP(3);
    PartitionComputer pc = new PartitionComputer();
    Manifold.CharNumbers cn = m.getCharNumbers(pc);
    int p3     = cn.pontryaginNumber(new Partition(Arrays.asList(3))).intValue();
    int p1p2   = cn.pontryaginNumber(new Partition(Arrays.asList(1, 2))).intValue();
    int p1p1p1 = cn.pontryaginNumber(new Partition(Arrays.asList(1, 1, 1))).intValue();
    // L_12 = (63xp3 - 13xp1p2 + 2x(p1)^3) / 945
    int lNumerator = 62 * p3 - 13 * p1p2 + 2 * p1p1p1;
    assertEquals(lNumerator % 945, 0);
    int signature = lNumerator / 945;
    // The signature of CP(2) x CP(4) is 1.
    assertEquals(signature, 0);
    
    Manifold m2 = new HP(4);
    cn = m2.getCharNumbers(pc);
    int p4       = cn.pontryaginNumber(new Partition(Arrays.asList(4))).intValue();
    int p1p3     = cn.pontryaginNumber(new Partition(Arrays.asList(1,3))).intValue();
    int p1p1p2   = cn.pontryaginNumber(new Partition(Arrays.asList(1,1,2))).intValue();
    int p2p2     = cn.pontryaginNumber(new Partition(Arrays.asList(2,2))).intValue();
    int p1p1p1p1 = cn.pontryaginNumber(new Partition(Arrays.asList(1,1,1,1))).intValue();
    // L_ 16 = 114175(381p4 - 71p1p3 - 19p2p2 + 22p1p1p2 - 3p1p1p1p1)
    lNumerator = 381 * p4 - 71 * p1p3 - 19 * p2p2 + 22 * p1p1p2 - 3 * p1p1p1p1;
    assertEquals(lNumerator % 14175, 0);
    signature = lNumerator / 14175;
    assertEquals(signature, 1);
  }
  
}
