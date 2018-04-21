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

import charnumcomputer.*;
import java.util.*;
import java.math.*;

/**
 *
 * @author William Gollinger
 */
public abstract class Manifold {
  
  private Map<String, Polynomial> charClasses;
  CharNumbers charNumbers = new CharNumbers();
    
  /*
  Abstract methods.
  */
  
  public abstract int rDim();
  public abstract boolean isComplex();
  /**
   * Should throw UnsupportedOperationException if isComplex() returns false.
   * @return 
   */
  public abstract int cDim();
  
  public abstract MultiDegree truncation();
  public abstract Polynomial.Ring cohomology(); 
  public abstract Polynomial.Ring mod2Cohomology(); 

  public abstract Map<String, Polynomial> getCharClasses();
  
  
  
  public CharNumbers getCharNumbers() {
    return new CharNumbers(charNumbers);
  }
  
  
  public static class CharNumbers {
    
    Map<String, Map<Partition, BigInteger>> charNums;
    public CharNumbers() {
      charNums = new HashMap<>();
    }
    public CharNumbers(CharNumbers o) {
      this();
      for (String type : o.charNums.keySet()) {
        charNums.put(type, new HashMap<>(o.charNums.get(type)));
      }
    }
    
    /**
     * Assumes m's characteristic classes have been computed.
     * @return 
     */
    public static CharNumbers computeCharNumbers(Map<String, Polynomial> charClasses) {
      CharNumbers charNumbers = new CharNumbers();
      
      //..
      
      return charNumbers;
    }
    
  }
  
}
