/*
 * $Id: OUPHtmlCrawlFilterFactory.java,v 1.2 2015/02/04 07:12:37 etenbrink Exp $
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

package org.lockss.plugin.highwire.oup;

import java.io.InputStream;

import org.htmlparser.NodeFilter;
import org.lockss.daemon.PluginException;
import org.lockss.plugin.ArchivalUnit;
import org.lockss.plugin.highwire.HighWireDrupalHtmlCrawlFilterFactory;
import org.lockss.util.Logger;

public class OUPHtmlCrawlFilterFactory extends HighWireDrupalHtmlCrawlFilterFactory {
  
  private static final Logger log = Logger.getLogger(OUPHtmlCrawlFilterFactory.class);
  
  @Override
  public InputStream createFilteredInputStream(ArchivalUnit au,
                                               InputStream in,
                                               String encoding)
      throws PluginException {
    NodeFilter[] filters = new NodeFilter[] {
        // except <div class="highwire-citation-formats-links">
        // author tool-tips changed for http://ajpheart.physiology.org/content/306/11/H1594.figures-only
        // HtmlNodeFilters.tagWithAttributeRegex("div", "class", "^author-tooltip"),
        // OUP & APS articles sometimes had view links, need to find examples
        // HtmlNodeFilters.tagWithAttributeRegex("a", "class", "hw-link"),
        // <li class="index-by-author first last">
        
    };
    
    InputStream filtered = super.createFilteredInputStream(au, in, encoding, null);
    
    return filtered;
  }
}
