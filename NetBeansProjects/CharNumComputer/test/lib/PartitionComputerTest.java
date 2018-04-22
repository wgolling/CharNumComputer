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

import lib.PartitionComputer;
import lib.Partition;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class PartitionComputerTest {
  
  public PartitionComputerTest() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of getPartitions method, of class PartitionComputer.
   */
  @Test
  public void testGetPartitions() {
    System.out.println("getPartitions");
    PartitionComputer pc = new PartitionComputer();
    assertEquals(pc.getPartitions(0).size(), 1);
    assertEquals(pc.getPartitions(1).size(), 1);
    assertEquals(pc.getPartitions(6).size(), 11);
    assertEquals(pc.getPartitions(12).size(), 77);
  }

  /**
   * Test of getOccurrences method, of class PartitionComputer.
   */
  @Test
  public void testGetOccurrences() {
    System.out.println("getOccurrences");
    PartitionComputer pc = new PartitionComputer(12);
    Map<Integer, Set<Partition>> occ6 = pc.getOccurrences(6);
    assertEquals(occ6.get(4).size(), 2);
  }

  /**
   * Test of dynamicCountPartitions method, of class PartitionComputer.
   */
  @Test
  public void testDynamicCountPartitions() {
    System.out.println("dynamicCountPartitions");
    PartitionComputer pc = new PartitionComputer();
    assertEquals(pc.dynamicCountPartitions(100), 190569292); //too long for getPartitions
  }
  
}
