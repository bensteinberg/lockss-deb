/*
 * $Id: TestTimeInterval.java,v 1.1 2003/12/12 02:38:27 eaalto Exp $
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

import java.util.*;
import org.lockss.test.*;

/**
 * This is the test class for org.lockss.util.TimeInterval
 */
public class TestTimeInterval extends LockssTestCase {
  TimeInterval int1;

  public void setUp() throws Exception {
    int1 = new TimeInterval(125, 150);
  }

  public void testGetTime() {
    assertEquals(125, int1.getBeginTime());
    assertEquals(150, int1.getEndTime());
  }

  public void testGetTotalTime() {
    assertEquals(25, int1.getTotalTime());

    // list
    TimeInterval int2 = new TimeInterval(200, 225);
    assertEquals(50, TimeInterval.getTotalTime(ListUtil.list(int1, int2)));
  }

}
