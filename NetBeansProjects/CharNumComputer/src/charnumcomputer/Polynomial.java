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

import java.util.*;
import java.math.BigInteger;

/**
 * 
 * @author William Gollinger
 */
public class Polynomial {
  List<Map<MultiDegree, BigInteger>> terms;                                  // some of the coefficients can get quite large.
  
  
  /**
   * Ring contains the arithmetic operations for polynomials.
   * There is a field corresponding to degree truncation,
   * and one indicating the modulus when the coefficient ring is a cyclic group.
   */
  public class Ring {
    MultiDegree truncation;                                                  // if the exponent of x_i is at least truncation.get(i), a term is set to 0
    int modulus;                                                             // the modulus won't need to be as large as BigInteger
    
    public Ring(int vars) {
      int[] trunc = new int[vars];
      Arrays.fill(trunc, Integer.MAX_VALUE);
      // make multidegree out of trunc
      modulus = Integer.MAX_VALUE;
    }
    
  }
}
