/*
* $Id: PsmAction.java,v 1.3 2005/06/24 18:32:54 tlipkis Exp $
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
package org.lockss.protocol.psm;

import java.util.*;

/**
 * Base class for actions executed in response to event or state entry;
 * must complete quickly.
 */
public abstract class PsmAction {
  /** Perform the action, return the next event.
   * @param triggerEvent the event that caused this action to run.  On
   * entry to a state, it's the event that caused the state transition.
   * @param interp the state interpreter, from which the action can get the
   * user object.
   * @return the next event
   */
  protected abstract PsmEvent run(PsmEvent triggerEvent, PsmInterp interp);

  /** This is currently not reliable, as any action can return a
   * PsmWaitEvent.
   */
  final boolean isWaitAction() {
    return this instanceof PsmWait;
  }
}