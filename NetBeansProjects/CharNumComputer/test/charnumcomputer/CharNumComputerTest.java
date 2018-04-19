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

import java.math.BigInteger;
import java.util.*;
import manifold.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class CharNumComputerTest {
  
  CharNumComputer cnc;
  public CharNumComputerTest() {
    cnc = new CharNumComputer();
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of computeCharNumbers method, of class CharNumComputer.
   */
  @Test
  public void testComputeCharNumbers() {
    System.out.println("computeCharNumbers");
    Map<String, Map<Partition, BigInteger>> charNumbers =
            cnc.computeCharNumbers(new CP(2));
    Partition v1squared = new Partition(new Integer[]{1,1});
    Partition v2        = new Partition(new Integer[]{2});
    assertEquals(charNumbers.get("chern").get(v1squared), BigInteger.valueOf(9));
    assertEquals(charNumbers.get("chern").get(v2), BigInteger.valueOf(3));
    
    charNumbers = cnc.computeCharNumbers(
            new Product(Arrays.asList(new CP(2), new CP(2))));
    int p1squared = charNumbers.get("pont").get(v1squared).intValue();
    assertEquals(p1squared, 18);
    int p2        = charNumbers.get("pont").get(v2).intValue();
    assertEquals(p2, 9);
      
    int num = (7 * p2 - p1squared);
    assertEquals(num % 45, 0);
    int signature = num / 45;
    assertEquals(signature, 1);
    
    // try 12-manifolds now
    
    Partition v1cubed = new Partition(new Integer[]{1,1,1});
    Partition v1v2    = new Partition(new Integer[]{1,2});
    Partition v3      = new Partition(new Integer[]{3});
    charNumbers = cnc.computeCharNumbers(
            new Product(Arrays.asList(new CP(2), new CP(4))));
    Map<Partition, BigInteger> pontNumbers = charNumbers.get("pont");
    int p1cubed = pontNumbers.get(v1cubed).intValue(); 
    int p1p2    = pontNumbers.get(v1v2).intValue(); 
    int p3      = pontNumbers.get(v3).intValue(); 
    num = 62 * p3 - 13 * p1p2 + 2 * p1cubed;
    assertEquals(num % 945, 0);
    signature = num / 945;
    assertEquals(signature, 1);
  }

  /**
   * Test of main method, of class CharNumComputer.
   */
  @Test
  public void testMain() {
    System.out.println("main");
  }
  
}
