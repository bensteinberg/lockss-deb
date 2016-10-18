/*
 * $Id: TestManeyAtyponHtmlFilterFactory.java,v 1.1 2015/01/23 06:55:26 ldoan Exp $
 */

/*

Copyright (c) 2000-2015 Board of Trustees of Leland Stanford Jr. University,
all rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the \"Software\"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
STANFORD UNIVERSITY BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Except as contained in this notice, the name of Stanford University shall not
be used in advertising or otherwise to promote the sale, use or other dealings
in this Software without prior written authorization from Stanford University.

*/

package org.lockss.plugin.atypon.maney;

import java.io.*;

import junit.framework.Test;

import org.lockss.util.*;
import org.lockss.config.ConfigManager;
import org.lockss.config.Configuration;
import org.lockss.daemon.PluginException;
import org.lockss.plugin.ArchivalUnit;
import org.lockss.plugin.FilterFactory;
import org.lockss.plugin.PluginManager;
import org.lockss.plugin.PluginTestUtil;
import org.lockss.test.*;

public class TestManeyAtyponHtmlFilterFactory extends LockssTestCase {
  
  FilterFactory variantFact;
  ArchivalUnit mau;
  String tempDirPath;
  MockLockssDaemon daemon;
  PluginManager pluginMgr;
        
  private static final String PLUGIN_ID = 
      "org.lockss.plugin.atypon.maney.ClockssManeyAtyponPlugin";
  
  private static final String filteredStr = 
      "<div class=\"block\"></div>";
  
  // from toc, abs, full, ref - News & alerts box near bottom
  // with About this Journal and Editors & Editorial Board tabs  
  // http://www.maneyonline.com/toc/aac/112/8  
  // News & alerts
  private static final String withMigratedNews =
      "<div class=\"block\">" +
          "<div class=\"widget general-rich-text none\" id=\"migrated_news\">" +
          "<div class=\"wrapped 1_12\" id=\"news\">" +
          "<div class=\"widget-body body body-none \">" +
          "<div id=\"rich-text-migrated_news\" class=\"pb-rich-text\">" +
          "<p>Editor's Choice2015<br>" +
          "Professor Prof X has now chosen 5 articles.</p>" +
          "</div></div></div></div>" +
          "</div>";
  // About this journal
  private static final String withMigratedAims =
      "<div class=\"block\">" +
          "<div class=\"widget general-rich-tex\" id=\"migrated_aims\">" +
          "<div class=\"wrapped 1_12\" id=\"about\">" +
          "<div class=\"widget-body\">" +
          "<div id=\"rich-text-migrated_aims\" class=\"pb-rich-text\">" +
          "<p><em>Blah applications</p>" +
          "<p>Support including:</p>" +
          "<ul><li>Environment</li></ul>" +
          "<p>To capture</p>" +
          "<p><a href=\"/link/abc\">ABC</a>" +
          "<a href=\"/link/abcs\">ABCS</a></p>" +
          "</div></div></div></div>" +
          "</div>";
  // Editors & Editorial
  private static final String withMigratedEditors =
      "<div class=\"block\">" + 
          "<div class=\"widget general-rich-text\" id=\"migrated_editors\">" +
          "<div class=\"wrapped 1_12\" id=\"editorial\">" +
          "<div class=\"widget-body body body-none \">" +
          "<div id=\"rich-text-migrated_editors\" class=\"pb-rich-text\">" +
          "<p>Editor:<br>Prof A>email</p>" +
          "</div></div></div></div>" +
          "</div>";

  private static final String withTableReferences =
      "<div class=\"block\">" +
          "<table class=\"references\" border=\"0\">" +
          "<tbody><tr><td id=\"C1\" class=\"refnumber\">1.</td>" +
          "<td valign=\"top\">ABC<span class=\"title\">Targeting abc</span>J Xyz" +
          "<script type=\"text/javascript\">" +
          "<a href=\"javascript:popRefLink(16,'C1','111.1371')\">xref</a>" +
          "<script type=\"text/javascript\">" +
          "<a href=\"javascript:popRefLink(8,'C1','12345')\">xmed</a>" +
          "<script type=\"text/javascript\">" +
          "<a href=\"javascript:popRefLink(128,'C1','00123')\">abc</a>" +
          "</td></tr></tbody></table>" +
          "</div>";
  
  private static final String withLiteratumMostReadWidget = 
      "<div class=\"block\">" +
          "<div class=\"widget literatumMostReadWidget none  widget-none\" " +
          "id=\"196\">" +
          "<a href=\"/action/link?jid=abc\">See more ></a>" +
          "</div>" +
          "</div>";
    
  private static final String withLiteratumMostCitedWidget = 
      "<div class=\"block\">" +    
          "<div class=\"widget literatumMostCitedWidget none  widget-none\" " +
          "id=\"984\">" +
          "<a href=\"/action/link?journalCode=dei\">See more ></a>" +
          "</div>" +
          "</div>";
    
  private static final String withPublicationListWidget = 
      "<div class=\"block\">" +    
          "<div class=\"widget publicationListWidget widget-compact\">" +
          "id=\"0fa\"><div>Foo</div></div>" +
          "</div>";
          
  private static final String withCompactJournalHeader = 
      "<div class=\"block\">" +    
          "<div class=\"wrapped \" id='compactJournalHeader'>" +
          "<div data-pb-dropzone=\"center\">" +
          "<div class=\"widget general-heading widget-compact\" id=\"18c\">" +
          "<div class=\"wrapped 111\" >" +
          "<div class=\"widget-body\"><div class=\"page-heading\">" +
          "<h1>The Title</h1>" +
          "</div></div></div></div></div></div>" +
          "</div>";
    
  private static final String withRelatedLayer = 
      "<div class=\"block\">" +  
          "<div class=\"relatedLayer\" style=\"display: block;\">" +
          "<div class=\"category\">" +
          "<h3>Original Article</h3>" +
          "<ul><li>" +
          "<a href=\"/doi/abs/11.1111/00001Y.000001\">Original blocks</a>" +
          "</li></ul>" +
          "</div></div>" +
          "</div>";
  
  private static final String withRelatedContent = 
      "<div class=\"block\">" +  
          "<div id=\"relatedContent\" class=\"tab tab-pane\">" +
          "<div class=\"category\">" +
          "<h3>Original Article</h3>" +
          "<ul><li>" +
          "<a href=\"/doi/abs/11.1111/jid.2007.11\"> The First Analysis </a>" +
          "</li></ul>" +
          "</div></div>" +
          "</div>";
  
  private static final String withPageHeader =
      "<div class=\"block\">" +  
          "<div class=\"widget pageHeaderwidget-compact\" id=\"pageHeader\">" +
          "<div class=\"page-header\">" +
          "<div data-pb-dropzone=\"main\">" +
          "<div class=\"widget general-image widget-compact\" id=\"eb6\">" +
          "<div class=\"wrapped 111\" >" +
          "<div class=\"widget-body body body-none  body-compact\">" +
          "<a href=\"/\" <img src=\"/pblink/logo.jpg\"/></a>" +
          "</div></div></div></div></div></div>" +
          "</div>";
  
  private static final String withPageFooter =
      "<div class=\"block\">" + 
          "<div class=\"widget pageFooterwidget-compact\" id=\"pageFooter\">" +
          "<div class=\"widget-body body body-none  body-compact\">" +
          "<div class=\"page-footer\">" +
          "<div class=\"widget layout-three-columns\" id=\"67a\">" +
          "<div class=\"wrapped \" >" +
          "<div class=\"widget-body body body-none  body-compact\"><ul>" +
          "<li><a href=\"http://link/about_maney/\">About Maney</a></li>" +
          "<li><a href=\"http://link/books \">Books</a></li>" +
          "<li><a href=\"/page/help\">Help & FAQs</a></li>" +
          "<li><a href=\"http://link/press_room/\">Press room</a></li>" +
          "<li><a href=\"/link/contact\">Contact us</a></li>" +
          "</ul></div></div></div></div></div></div>" +
          "</div>";
  
  private static final String withJournalHeader =
      "<div class=\"block\">" + 
          "<div class=\"wrapped \" id=\"Journal Header\">" +
          "<div class=\"widget-body body body-box \">" +
          "<div class=\"pb-columns row-fluid\"> " +
          "<div class=\"widget layout-two-columns\" id=\"df9\">" +                                             
          "<div data-pb-dropzone=\"left\" >" +                                                                                                                            
          "<a href=\"/loi/jid\">" +                                                                                                                                               
          "<img src=\"/link/image50px.jpg\"/>" +                                                                                            
          "</a></div></div></div></div></div>" +                                                                                                                                                          
          "</div>";  
  
  private static final String withAccessOptions = 
      "<div class=\"block\">" + 
          "<ul class=\"horz-list float-right access-options\">" +
          "<li><img src=\"/imagesrc/access_full.gif\">" +
          "<span>Full access</span></li>" +
          "<li><img src=\"/imagesrc/openAccess.png\">" +
          "<span>Open access</span></li></ul>" +
          "</div>";
 
  private static final String withAccessIconContainer =  
      "<div class=\"block\">" +
          "<td class=\"accessIconContainer\"><div>" +
          "<img src=\"/imagessrc/access_free.gif\" alt=\"Free Access\"" +
          "title=\"Free Access\" class=\"accessIcon freeAccess\"></div></td>" +
          "</div>"; 
  
  private static final String withLiteratumAd =  
      "<div class=\"block\">" +
          "<div class=\"widget literatumAd none  widget-none\" id=\"526\">" +
          "<div class=\"wrapped 1_12\">" +
          "<div class=\"widget-body body body-none \"><div class=\"pb-ad\">" +
          "<a href=\"/action/ad\"\"><img src=\"/adsrc/advert.jpg\"></a>" +
          "</div></div></div></div>" +
          "</div>";
  
  private static final String withSocietyLogo =
      "<div class=\"block\">" +                                                                                                                                                           
          "<div class=\"wrapped \" id='Society Logo'>" +                                                                                                                      
          "<header class=\"widget-header header-titlebar \">Abc</header>" +                                                                                   
          "<div class=\"widget-body body body-titlebar \">" +                                                                                                             
          "<div class=\"pb-columns row-fluid\">" +                                                                                                                                
          "<a href=\"http://www.batod.org.uk/\" title=\"atitle\">" +                                                                        
          "<img src=\"/logosrc/logo75px.jpg\" alt=\"atitle\"/></a>" +
          "</div></div></div>" +                                                                                                                                                              
          "</div>"; 

  private static final String withMigratedInformation =
      "<div class=\"block\">" +  
          "<section class=\"widget\" id=\"migrated_information\">" +
          "<div class=\"wrapped \" >" +
          "<header class=\"widget-header\">Journal services</header>" +
          "<div class=\"widget-body body body-titlebar \">" +
          "<div class='row-fluid'>" +
          "<div class='width_1_2'><ul class='decoratedLinks'><li>" +
          "<a href='/pricing/dei'>Subscriptions</a></li><li>" +
          "<a href='/pricing/dei'>Access options</a></li><li>" +
          "<a href='/action/recommendation'>Recommend</a></li><li>" +
          "<a href='/advertising/dei'>Advertising</a></li></ul></div>" +
          "<div class='width_1_2'><ul class='decoratedLinks'><li>" +
          "<a href='/bibliometrics/dei'>Bibliometrics</a></li><li>" +
          "<a href='/page/redirect/backissues'>Back issues</a></li><li>" +
          "<a href='/page/redirect/permissions'>Permissions</a></li><li>" +
          "<a href='/page/redirect/reprints'>Reprints</a></li></ul>" +
          "</div></div></div>" +
          "</div>" +
          "</section>" +
          "</div>";
  
  private static final String withMigratedForAuthors = 
      "<div class=\"block\">" + 
          "<section class=\"widget\" id=\"migrated_forauthors\">" +
          "<div class=\"wrapped \">" +
          "<h1 class=\"widget-header header-titlebar \">For authors</h1>" +
          "<div class=\"widget-body body body-titlebar \">" +
          "<ul class=\"decoratedLinks\">" +
          "<li><a href=\"/abc/jid\">Instructions for authors</a></li>" +
          "</ul></div></div>" +
          "</section>" +
          "</div>";
  
  private static final String withLiteratumSerialSubjects = 
      "<div class=\"block\">" + 
          "<div class=\"widget literatumSerialSubjects\" id=\"4f7\">" +
          "<div class=\"wrapped 111\">" +
          "<div class=\"widget-body body body-none \">" +
          "<ul class=\"categoryList\">" +
          "<li class=\"sub-hs\">" +
          "<a href=\"/health\">Health Sciences</a>" +
          "</li></ul></div></div></div>" +
          "</div>";
  
  private static final String withLiteratumBookIssueNavigation =
      "<div class=\"block\">" +                                                                                                                                                              
          "<div class=\"widget literatumBookIssueNavigation\" id=\"843\">" +                                               
          "<div class=\"widget-body body body-none \">" +
          "<div class=\"pager issueBookNavPager\">" +                                                                          
          "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +                                                                                                      
          "<tr><td class=\"journalNavLeftTd\">" +                                                                                                                         
          "<div class=\"prev placedLeft\">" +                                                                                                                 
          "<a href=\"/toc/jid/13/2\">< PREV</a>" +                                                                                                                                        
          "</div></td></tr></table>" +                                                                                                                                                        
          "</div></div></div>" +                                                                                                                                                          
          "</div>"; 
  
  private static final String withMathJaxMessage =
      "<div class=\"block\">" +  
          "<div id=\"MathJax_Message\" style=\"display: none;\"></div>" +
          "</div>";
      
  private static final String withLiteratumRelatedContentSearch =
      "<div class=\"block\">" +  
          "<section class=\"widg literatumRelatedContentSearch\" id=\"6a9\">" +
          "<header class=\"widget-header \">Related content search</header>" +
          "<div class=\"widget-body\"><div class=\"relatedContentForm\">" +
          "<form action=\"/action/searchDispatcher\" " +
          "name=\"relatedSearchForm\" method=\"post\">" +
          "<div class=\"relatedAuthors relatedTerms\">" +
          "<span>By Author</span>" +
          "<ul><li><input type=\"checkbox\" name=\"Contrib\" value=\"Zat\">" +
          "<label>Blah</label></li></ul></div>" +
          "<input class=\"searchButtons\" type=\"submit\" value=\"Search\" />" +
          "</form>" +
          "</div></div>" +
          "</section>" +
          "</div>";      

    private static final String withArticleToolsWidgetExceptDownloadCitation =  
      "<div class=\"block\">" +  
          "<section class=\"widget literatumArticleToolsWidget\" id=\"594\">" +
          "<ul class=\"linkList blockLinks separators centered\">" +
          "<li class=\"addToFavs\"><a href=\"/action/fav-link\">Fav</a></li>" +
          "<li class=\"downloadCitations\"><a href=\"/action/showCitFormats?" +
          "doi=1.11111%2Fjid.2013.111\">Export citation</a></li>" +
          "</ul></section>" +
          "</div>";

    // id tag also got filtered
    private static final String articleToolsWidgetFiltered = 
      "<div class=\"block\">" +  
          "<section class=\"widget literatumArticleToolsWidget\" >" +
          "<ul class=\"linkList blockLinks separators centered\">" +
          "<li class=\"downloadCitations\"><a href=\"/action/showCitFormats?" +
          "doi=1.11111%2Fjid.2013.111\">Export citation</a></li>" +
          "</ul></section>" +
          "</div>";
    
  protected ArchivalUnit createAu()
      throws ArchivalUnit.ConfigurationException {
    return PluginTestUtil.createAndStartAu(PLUGIN_ID,  arrsAuConfig());
  }
  
  private Configuration arrsAuConfig() {
    Configuration conf = ConfigManager.newConfiguration();
    conf.put("base_url", "http://www.example.com/");
    conf.put("journal_id", "abc");
    conf.put("volume_name", "99");
    return conf;
  }
  
  private static void doFilterTest(ArchivalUnit au, 
      FilterFactory fact, String nameToHash, String expectedStr) 
          throws PluginException, IOException {
    InputStream actIn; 
    actIn = fact.createFilteredInputStream(au, 
        new StringInputStream(nameToHash), Constants.DEFAULT_ENCODING);
    assertEquals(expectedStr, StringUtil.fromInputStream(actIn));
  }
  
  public void startMockDaemon() {
    daemon = getMockLockssDaemon();
    pluginMgr = daemon.getPluginManager();
    pluginMgr.setLoadablePluginsReady(true);
    daemon.setDaemonInited(true);
    pluginMgr.startService();
    daemon.getAlertManager();
    daemon.getCrawlManager();
  }
  
  public void setUp() throws Exception {
    super.setUp();
    tempDirPath = setUpDiskSpace();
    startMockDaemon();
    mau = createAu();
  }
  
  // Variant to test with Crawl Filter
  public static class TestCrawl extends TestManeyAtyponHtmlFilterFactory {
    public void testFiltering() throws Exception {
      variantFact = new ManeyAtyponHtmlCrawlFilterFactory();
      doFilterTest(mau, variantFact, withMigratedNews, filteredStr); 
      doFilterTest(mau, variantFact, withMigratedAims, filteredStr); 
      doFilterTest(mau, variantFact, withMigratedEditors, filteredStr); 
      doFilterTest(mau, variantFact, withTableReferences, filteredStr); 
      doFilterTest(mau, variantFact, withLiteratumMostReadWidget, filteredStr); 
      doFilterTest(mau, variantFact, withLiteratumMostCitedWidget, 
                   filteredStr); 
      doFilterTest(mau, variantFact, withPublicationListWidget, filteredStr); 
      doFilterTest(mau, variantFact, withCompactJournalHeader, filteredStr); 
      doFilterTest(mau, variantFact, withRelatedLayer, filteredStr);      
      doFilterTest(mau, variantFact, withRelatedContent, filteredStr);            
    }    
  }

  // Variant to test with Hash Filter
   public static class TestHash extends TestManeyAtyponHtmlFilterFactory {   
     public void testFiltering() throws Exception {
      variantFact = new ManeyAtyponHtmlHashFilterFactory();
        doFilterTest(mau, variantFact, withPageHeader, filteredStr); 
        doFilterTest(mau, variantFact, withPageFooter, filteredStr); 
        doFilterTest(mau, variantFact, withJournalHeader, filteredStr); 
        doFilterTest(mau, variantFact, withAccessOptions, filteredStr); 
        doFilterTest(mau, variantFact, withAccessIconContainer, filteredStr); 
        doFilterTest(mau, variantFact, withLiteratumAd, filteredStr); 
        doFilterTest(mau, variantFact, withSocietyLogo, filteredStr); 
        doFilterTest(mau, variantFact, withMigratedInformation, filteredStr); 
        doFilterTest(mau, variantFact, withMigratedForAuthors, filteredStr); 
        doFilterTest(mau, variantFact, withLiteratumMostReadWidget, 
                    filteredStr); 
        doFilterTest(mau, variantFact, withLiteratumMostCitedWidget, 
                     filteredStr); 
        doFilterTest(mau, variantFact, withPublicationListWidget, filteredStr); 
        doFilterTest(mau, variantFact, withLiteratumSerialSubjects, 
                     filteredStr); 
        doFilterTest(mau, variantFact, withMigratedNews, filteredStr); 
        doFilterTest(mau, variantFact, withMigratedAims, filteredStr); 
        doFilterTest(mau, variantFact, withMigratedEditors, filteredStr);         
        doFilterTest(mau, variantFact, withLiteratumBookIssueNavigation, 
                     filteredStr);         
        doFilterTest(mau, variantFact, withMathJaxMessage, filteredStr);         
        doFilterTest(mau, variantFact, withLiteratumRelatedContentSearch, 
                     filteredStr); 
        doFilterTest(mau, variantFact, 
                     withArticleToolsWidgetExceptDownloadCitation, 
                     articleToolsWidgetFiltered);                 
     }
  }
  
  public static Test suite() {
    return variantSuites(new Class[] {
        TestCrawl.class,
        TestHash.class
      });
  }
  
}

