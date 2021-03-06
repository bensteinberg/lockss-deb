/*
 * $Id: IngentaKeywordFilterFactory.java,v 1.1 2015/01/30 23:41:00 etenbrink Exp $
 */

/*

Copyright (c) 2000-2015 Board of Trustees of Leland Stanford Jr. University,
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

package org.lockss.plugin.ingenta;

import java.io.*;

import org.lockss.daemon.PluginException;
import org.lockss.filter.CssLinkFilter;
import org.lockss.filter.FilterUtil;
import org.lockss.filter.StringFilter;
import org.lockss.plugin.ArchivalUnit;
import org.lockss.plugin.FilterFactory;
import org.lockss.util.*;


/** Filter that removes line starting with keyword */
public class IngentaKeywordFilterFactory implements FilterFactory {
  
  private static Logger log = Logger.getLogger(IngentaKeywordFilterFactory.class);
  
  public class KeywordFilter extends CssLinkFilter {
    
    public KeywordFilter(Reader reader, int bufferCapacity, String origStr,
        String regexStr, String replaceStr) {
      super(reader, bufferCapacity, origStr, regexStr, replaceStr);
    }
  }
  
  protected String regexStr;
  
  @Override
  public InputStream createFilteredInputStream(ArchivalUnit au,
      InputStream in, String encoding) throws PluginException {
    
    KeywordFilter kwf = new KeywordFilter(FilterUtil.getReader(in, encoding),
        4096, "keyword = ", "\".*\"", "");
    return new ReaderInputStream(kwf, encoding);
  }
}
