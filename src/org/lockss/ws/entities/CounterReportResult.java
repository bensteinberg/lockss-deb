/*
 * $Id: CounterReportResult.java,v 1.2 2014/06/05 05:48:47 fergaloy-sf Exp $
 */

/*

 Copyright (c) 2013-2014 Board of Trustees of Leland Stanford Jr. University,
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
package org.lockss.ws.entities;

import javax.activation.DataHandler;

/**
 * A wrapper for a COUNTER report provided by the server.
 */
public class CounterReportResult {
  private String fileName;
  private DataHandler dataHandler;

  /**
   * Provides the name of the file with the requested report.
   *
   * @return a String with the name of the file.
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Provides the content of the requested report.
   *
   * @return a DataHandler through which to obtain the content of the report.
   */
  public DataHandler getDataHandler() {
    return dataHandler;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setDataHandler(DataHandler dataHandler) {
    this.dataHandler = dataHandler;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("CounterReportResult [fileName=");
    builder.append(fileName);
    builder.append(", dataHandler=");
    builder.append(dataHandler);
    builder.append("]");
    return builder.toString();
  }
}