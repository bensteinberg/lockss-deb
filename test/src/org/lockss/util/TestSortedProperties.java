/*
 * $Id: TestSortedProperties.java,v 1.1 2003/07/23 06:42:00 tlipkis Exp $
 */

/*

Copyright (c) 2000-2003 Board of Trustees of Leland Stanford Jr. University,
all rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
STANFORD UNIVERSITY BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Except as contained in this notice, the name of Stanford University shall not
be used in advertising or otherwise to promote the sale, use or other dealings
in this Software without prior written authorization from Stanford University.

*/

package org.lockss.util;

import junit.framework.TestCase;
import java.io.*;
import java.util.*;
import org.lockss.util.*;
import org.lockss.test.*;

/**
 * This is the test class for org.lockss.util.SortedProperties
 */

public class TestSortedProperties extends LockssTestCase {
  public static Class testedClasses[] = {
    org.lockss.util.SortedProperties.class
  };

  public void testSorted() {
    SortedProperties p = new SortedProperties();
    p.put("a", "1");
    p.put("zz", "33");
    p.put("m", "2");
    p.put("j", "3");
    String exp[] = {"a", "j", "m", "zz"};
    // test keySet() iterator
    assertIsomorphic(exp, p.keySet().iterator());
    // test Enumeration
    assertIsomorphic(exp, new EnumerationIterator(p.keys()));
  }

  public void testFromProperties() {
    Properties p = new Properties();
    p.put("a", "1");
    p.put("zz", "33");
    p.put("m", "2");
    p.put("j", "3");
    SortedProperties sp = SortedProperties.fromProperties(p);
    String exp[] = {"a", "j", "m", "zz"};
    assertIsomorphic(exp, sp.keySet().iterator());
    assertIsomorphic(exp, new EnumerationIterator(sp.keys()));
  }

}
