/*
 * $Id: DigitalCommonsRepositoryHtmlCrawlFilterFactory.java,v 1.5 2015/01/06 01:56:04 thib_gc Exp $
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

package org.lockss.plugin.bepress;

import java.io.InputStream;

import org.htmlparser.NodeFilter;
import org.htmlparser.filters.OrFilter;
import org.lockss.daemon.PluginException;
import org.lockss.filter.html.*;
import org.lockss.filter.html.HtmlNodeFilters.HasAttributeRegexFilter;
import org.lockss.plugin.*;
import org.lockss.util.Logger;

public class DigitalCommonsRepositoryHtmlCrawlFilterFactory implements FilterFactory {

  private static final Logger log =
      Logger.getLogger(DigitalCommonsRepositoryHtmlCrawlFilterFactory.class);
  
  @Override
  public InputStream createFilteredInputStream(ArchivalUnit au,
                                               InputStream in,
                                               String encoding)
      throws PluginException {
    String collectionHeading = au.getConfiguration().get("collection_heading");
    log.debug3("param collection_heading: " + collectionHeading);
    
    String regexStr = "^lockss_(?!" + collectionHeading + ")";

    NodeFilter[] filters = new NodeFilter[] {
        // filter out all years except the AU's year 		
        // <div class="lockss_2013">
        // <li class="lockss_2013">
        new HasAttributeRegexFilter("class", regexStr),
        // top right of the article for the year - <previous> and <next>
        // e.g. http://repository.cmu.edu/statistics/68/
        HtmlNodeFilters.tagWithAttribute("ul", "id", "pager"),
        // collections of type ir_book and ir_gallery have covers of the books
        // in other years (other than those in e.g. <li class="lockss_2013">)
        HtmlNodeFilters.tagWithAttribute("div", "class", "gallery-tools"),
        /* Unknown if following clauses are really necessary */
        // top banner
        HtmlNodeFilters.tagWithAttribute("div", "id", "header"),
        // breadcrumb - Home > Dietrich College > Statistics
        HtmlNodeFilters.tagWithAttribute("div", "id", "breadcrumb"),
        // left sidebar
        HtmlNodeFilters.tagWithAttribute("div", "id", "sidebar"),
        // footer
        HtmlNodeFilters.tagWithAttribute("div", "id", "footer"),
        // lockss-probe from manifest pages
        HtmlNodeFilters.tagWithAttribute("link", "lockss-probe")
    };
    
    return new HtmlFilterInputStream(in,
                                     encoding,
                                     HtmlNodeFilterTransform.exclude(new OrFilter(filters)));
  }

}