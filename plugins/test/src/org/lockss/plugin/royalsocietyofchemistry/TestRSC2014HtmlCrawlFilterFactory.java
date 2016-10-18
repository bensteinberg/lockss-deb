/*
 * $Id: TestRSC2014HtmlCrawlFilterFactory.java,v 1.1 2014/06/23 16:14:11 etenbrink Exp $
 */
/*

Copyright (c) 2000-2014 Board of Trustees of Leland Stanford Jr. University,
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

package org.lockss.plugin.royalsocietyofchemistry;

import java.io.*;
import org.lockss.util.*;
import org.lockss.test.*;

public class TestRSC2014HtmlCrawlFilterFactory extends LockssTestCase {
  static String ENC = Constants.DEFAULT_ENCODING;
  
  private RSC2014HtmlCrawlFilterFactory fact;
  private MockArchivalUnit mau;
  
  public void setUp() throws Exception {
    super.setUp();
    fact = new RSC2014HtmlCrawlFilterFactory();
    mau = new MockArchivalUnit();
  }
  
  private static final String withRef = "" +
  		"<li><span id=\"cit1\">\n" + 
  		"  (<i>a</i>) Stuff</span>\n" + 
  		"</li>";
  
  private static final String withoutRef = "" +
      "<li>\n</li>";
  
  
  public void testFiltering() throws Exception {
    InputStream inA;
    
    // reference
    inA = fact.createFilteredInputStream(mau, new StringInputStream(withRef),
        Constants.DEFAULT_ENCODING);
    assertEquals(withoutRef, StringUtil.fromInputStream(inA));
    
  }
  
}
