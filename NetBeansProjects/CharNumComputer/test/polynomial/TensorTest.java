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
import java.util.stream.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author William Gollinger
 */
public class TensorTest {
  
  MultiDegree.Builder mb;
  PolyRing<Int> twoVars;
  PolyRing<Int> threeVars;
  Tensor<Int> fiveVars;
  
  public TensorTest() {
    Int dummy = new Int();
    mb        = new MultiDegree.Builder();
    twoVars   = new PolyRing<>(dummy, mb.setVars(2).set(0,4).set(1,5).build());
    threeVars = new PolyRing<>(dummy, 3);
    fiveVars  = new Tensor<>(dummy, Arrays.asList(twoVars, threeVars));
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of concatStream method, of class Tensor.
   */
  @Test
  public void testConcatStream() {
    System.out.println("concatStream");
    List<MultiDegree> list = new ArrayList<>();
    Stream<MultiDegree> empty = list.stream();
    assert(Tensor.concatStream(empty).equals(MultiDegree.empty()));
    list.add(mb.setVars(2).set(0,4).set(1,5).build());
    list.add(mb.setVars(3).maxAll().build());
    Stream<MultiDegree> s = list.stream();
    MultiDegree result = Tensor.concatStream(s);
    MultiDegree expResult = mb.setVars(5).maxAll().set(0,4).set(1,5).build();
    assert(result.equals(expResult));
  }


  /**
   * Test of tensor method, of class Tensor.
   */
  @Test
  public void testTensor_List() {
    System.out.println("tensor");
    PolyRing.Element p = twoVars.one();
    PolyRing.Element q = threeVars.one();
    PolyRing.Element r = fiveVars.tensor(Arrays.asList(p, q));
    assert(r.equals(fiveVars.one()));
  }

  /**
   * Test of tensor method, of class Tensor.
   */
  @Test
  public void testTensor_PolyRingElement_PolyRingElement() {
    System.out.println("tensor");
    PolyRing.Element p = twoVars.one();
    PolyRing.Element q = threeVars.one();
    PolyRing.Element r = fiveVars.tensor(p, q);
    assert(r.equals(fiveVars.one()));
  }
  /**
   * Test of tensor method with wrong domains
   */
  @Test (expected = IllegalArgumentException.class)
  public void testTensorWrongDomains() {
    System.out.println("tensor with wrong domains");
    fiveVars.tensor(twoVars.one(), twoVars.one());
  }

}
