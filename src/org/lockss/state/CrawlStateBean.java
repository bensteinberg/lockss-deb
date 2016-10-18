/*
 * $Id: CrawlStateBean.java,v 1.4 2004/04/01 02:44:31 eaalto Exp $
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


package org.lockss.state;

/**
 * CrawlStateBean is a settable version of the CrawlState to allow marshalling.
 */
public class CrawlStateBean extends CrawlState {
  /**
   * Simple constructor to allow bean creation during unmarshalling.
   */
  public CrawlStateBean() { }

  /**
   * Constructor to create the bean from a CrawlState prior to marshalling.
   * @param state the CrawlState
   */
  CrawlStateBean(CrawlState state) {
    this.type = state.getType();
    this.status = state.getStatus();
    this.startTime = state.getStartTime();
  }

  /**
   * Sets the crawl type.
   * @param theType the type
   */
  public void setType(int theType) {
    type = theType;
  }

  /**
   * Sets the status of the crawl.
   * @param theStatus the status
   */
  public void setStatus(int theStatus) {
    status = theStatus;
  }

  /**
   * Sets the start time of the crawl.
   * @param theTime the time
   */
  public void setStartTime(long theTime) {
    startTime = theTime;
  }

}