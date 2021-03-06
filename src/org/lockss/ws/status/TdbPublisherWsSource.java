/*
 * $Id: TdbPublisherWsSource.java,v 1.1 2014/07/11 20:08:57 fergaloy-sf Exp $
 */

/*

 Copyright (c) 2014 Board of Trustees of Leland Stanford Jr. University,
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
package org.lockss.ws.status;

import org.lockss.config.TdbPublisher;
import org.lockss.ws.entities.TdbPublisherWsResult;

/**
 * Container for the information that is used as the source for a query related
 * to title database publishers.
 */
public class TdbPublisherWsSource extends TdbPublisherWsResult {
  private TdbPublisher tdbPublisher;

  private boolean namePopulated = false;

  public TdbPublisherWsSource(TdbPublisher tdbPublisher) {
    this.tdbPublisher = tdbPublisher;
  }

  @Override
  public String getName() {
    if (!namePopulated) {
      setName(tdbPublisher.getName());
      namePopulated = true;
    }

    return super.getName();
  }
}
