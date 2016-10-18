/*
 * $Id: CommandLineAdapter.java,v 1.1 2014/11/12 00:15:41 thib_gc Exp $
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

package org.lockss.tdb;

import org.apache.commons.cli.CommandLine;

/**
 * <p>
 * A {@link CommandLineAccessor} adapter for Commons CLI's {@link CommandLine}.
 * </p>
 * 
 * @author Thib Guicherd-Callin
 * @since 1.67
 */
public class CommandLineAdapter implements CommandLineAccessor {

  protected CommandLine commandLine;
  
  public CommandLineAdapter(CommandLine commandLine) {
    this.commandLine = commandLine;
  }
  
  @Override
  public String[] getArgs() {
    return commandLine.getArgs();
  }

  @Override
  public boolean hasOption(String opt) {
    return commandLine.hasOption(opt);
  }

  @Override
  public String getOptionValue(String opt) {
    return commandLine.getOptionValue(opt);
  }
  
  @Override
  public String[] getOptionValues(String opt) {
    return commandLine.getOptionValues(opt);
  }
  
}