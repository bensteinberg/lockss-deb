/*
 * $Id: BaseAtyponRisMetadataExtractorFactory.java,v 1.4 2014/10/08 16:11:26 alexandraohlson Exp $
 */

/*

 Copyright (c) 2000-2012 Board of Trustees of Leland Stanford Jr. University,
 all rights reserved.

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of his software and associated documentation files (the "Software"), to deal
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

package org.lockss.plugin.atypon;

import java.io.IOException;

import org.lockss.daemon.*;

import org.lockss.extractor.*;
import org.lockss.plugin.ArchivalUnit;
import org.lockss.plugin.CachedUrl;
import org.lockss.util.Logger;

/*
 *  NOTE: I have found the following:
 *  JO is often used (incorrectly) but consistently as the abbreviated form of the journal title, use JF and then T2 in preference
 *  Y1 usually is the same as DA, but not always, use DA if it's there
 *  DO NOT pick up the "UR" because it often points to dx.doi.org stable URL which 
 *  will not exist in the AU.  We must manually set the access_url
TY  - JOUR
T1  - <article title>
AU  - <author>
AU  - <other author>
Y1  - <date, often same as DA, often slightly later>
PY  - <year of pub>
DA  - <date of pub>
N1  - doi: 10.1137/100798910
DO  - 10.1137/100798910 
T2  - <journal title>
JF  - <journal title>
JO  - <abbreviated journal title>
SP  - <start page>
EP  - <end page>
VL  - <volume>
IS  - <issue>
PB  - <publisher but possibly imprint>
SN  - <issn>
M3  - doi: 10.1137/100798910
UR  - http://dx.doi.org/10.1137/100798910
Y2  - <later date - meaning?>
ER  -  
 * 
 */
public class BaseAtyponRisMetadataExtractorFactory
implements FileMetadataExtractorFactory {
  static Logger log = Logger.getLogger("BaseAtyponRisMetadataExtractorFactory");

  public FileMetadataExtractor createFileMetadataExtractor(MetadataTarget target,
      String contentType)
          throws PluginException {

    log.debug3("Inside Base Atypon Metadata extractor factory for RIS files");

    BaseAtyponRisMetadataExtractor ba_ris = new BaseAtyponRisMetadataExtractor();

    ba_ris.addRisTag("A1", MetadataField.FIELD_AUTHOR);
    // Do not use UR listed in the ris file! It will get set to full text CU by daemon
    return ba_ris;
  }

  public static class BaseAtyponRisMetadataExtractor
  extends RisMetadataExtractor {

    // override this to do some additional attempts to get valid data before emitting
    @Override
    public void extract(MetadataTarget target, CachedUrl cu, FileMetadataExtractor.Emitter emitter) 
        throws IOException, PluginException {
      ArticleMetadata am = extract(target, cu); 
      
      /* 
       * if, due to overcrawl, we got to a page that didn't have anything
       * valid, eg "this page not found" html page
       * don't emit empty metadata (because defaults would get put in
       * Must do this after cooking, because it checks size of cooked info
       */
      if (am.isEmpty()) {
        return;
      }

      /*
       * RIS data can be variable.  We don't have any way to add priority to
       * the cooking of data, so fallback to alternate values manually
       */
      if (am.get(MetadataField.FIELD_PUBLICATION_TITLE) == null) {
        if (am.getRaw("T2") != null) {
          am.put(MetadataField.FIELD_PUBLICATION_TITLE, am.getRaw("T2"));
        } else if (am.getRaw("JO") != null) {
          am.put(MetadataField.FIELD_PUBLICATION_TITLE, am.getRaw("JO")); // might be unabbreviated version
        }
      } 
      if (am.get(MetadataField.FIELD_DATE) == null) {
        if (am.getRaw("Y1") != null) { // if DA wasn't there, use Y1
          am.put(MetadataField.FIELD_DATE, am.getRaw("Y1"));
        }
      }
           
      // Only emit if this item is likely to be from this AU
      // protect against counting overcrawled articles
      ArchivalUnit au = cu.getArchivalUnit();
      if (!BaseAtyponMetadataUtil.metadataMatchesTdb(au, am)) {
        return;
      }
      
      /*
       * Fill in DOI, publisher, other information available from
       * the URL or TDB 
       * CORRECT the access.url if it is not in the AU
       */
      BaseAtyponMetadataUtil.completeMetadata(cu, am);
      emitter.emitMetadata(cu, am);
    }

  }

}
