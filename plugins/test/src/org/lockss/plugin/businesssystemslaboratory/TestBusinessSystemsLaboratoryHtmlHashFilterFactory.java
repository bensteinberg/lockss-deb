/*
 * $Id: TestBusinessSystemsLaboratoryHtmlHashFilterFactory.java,v 1.1 2013/11/01 18:06:41 ldoan Exp $
 */

/*

Copyright (c) 2000-2013 Board of Trustees of Leland Stanford Jr. University,
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

package org.lockss.plugin.businesssystemslaboratory;

import java.io.*;

import org.lockss.util.*;
import org.lockss.test.*;

public class TestBusinessSystemsLaboratoryHtmlHashFilterFactory extends LockssTestCase {
  private BusinessSystemsLaboratoryHtmlHashFilterFactory fact;
  private MockArchivalUnit mau;

  public void setUp() throws Exception {
    super.setUp();
    fact = new BusinessSystemsLaboratoryHtmlHashFilterFactory();
  }

 private static final String withScript =
     "<div class=\"block\">"
       + "<script type=\"text/javascript\">"
       + "var _gaq = _gaq || [];"
       + "blah blah;"
       + "</script>"
       + "</div>";
 
  private static final String withoutScript = "<div class=\"block\"></div>";
  
  private static final String withComments =
      "<div class=\"block\">"
        + "<!-- comment comment comment -->"
        + "</div>";
  
  private static final String withoutComments = "<div class=\"block\"></div>";

  public void testScriptFiltering() throws Exception {
    InputStream actIn = fact.createFilteredInputStream(mau,
        new StringInputStream(withScript), Constants.DEFAULT_ENCODING);
    assertEquals(withoutScript, StringUtil.fromInputStream(actIn));
  }

  public void testCommentsFiltering() throws Exception {
    InputStream actIn = fact.createFilteredInputStream(mau,
        new StringInputStream(withComments), Constants.DEFAULT_ENCODING);
    assertEquals(withoutComments, StringUtil.fromInputStream(actIn));
  }

}
