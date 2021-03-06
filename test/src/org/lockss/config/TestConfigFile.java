/*
 * $Id: TestConfigFile.java,v 1.14 2012/09/25 23:01:42 tlipkis Exp $
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

package org.lockss.config;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.net.*;
import junit.framework.*;

import org.lockss.config.Configuration;
import org.lockss.config.Tdb;
import org.lockss.config.TdbTitle;
import org.lockss.test.*;
import org.lockss.util.*;
import org.lockss.util.urlconn.*;
import static org.lockss.config.ConfigManager.KeyPredicate;

/**
 * Abstract superclass for tests of ConfigFile variants, nested in this
 * class
 */
public abstract class TestConfigFile extends LockssTestCase {
  public static Class testedClasses[] = {
    org.lockss.config.ConfigFile.class,
    org.lockss.config.FileConfigFile.class,
    org.lockss.config.HTTPConfigFile.class,
    org.lockss.config.JarConfigFile.class,
  };

  static Logger log = Logger.getLogger("TestConfigFile");

  private static final String text1 =
    "prop.1=foo\n" +
    "prop.2=bar\n" +
    "prop.3=baz";

  // valid tdb entry
  private static final String tdbText1 =
    "org.lockss.prop=value\n" +
    "org.lockss.title.title1.title=Air & Space volume 3\n" +
    "org.lockss.title.title1.plugin=org.lockss.testplugin1\n" +
    "org.lockss.title.title1.pluginVersion=4\n" +
    "org.lockss.title.title1.issn=0003-0031\n" +
    "org.lockss.title.title1.journal.link.1.type=\n" + TdbTitle.LinkType.continuedBy.toString() +
    "org.lockss.title.title1.journal.link.1.journalId=0003-0031\n" +  // link to self
    "org.lockss.title.title1.param.1.key=volume\n" +
    "org.lockss.title.title1.param.1.value=3\n" +
    "org.lockss.title.title1.param.2.key=year\n" +
    "org.lockss.title.title1.param.2.value=1999\n" +
    "org.lockss.title.title1.attributes.publisher=The Smithsonian Institution";

  // invalid tdb entry: missing "plugin"
  private static final String tdbText2 =
    "org.lockss.prop=value\n" +
    "org.lockss.title.title1.title=Air & Space volume 3\n" +
//    "org.lockss.title.title1.plugin=org.lockss.testplugin1\n" +
    "org.lockss.title.title1.pluginVersion=4\n" +
    "org.lockss.title.title1.issn=0003-0031\n" +
    "org.lockss.title.title1.journal.link.1.type=\n" + TdbTitle.LinkType.continuedBy.toString() +
    "org.lockss.title.title1.journal.link.1.journalId=0003-0031\n" +  // link to self
    "org.lockss.title.title1.param.1.key=volume\n" +
    "org.lockss.title.title1.param.1.value=3\n" +
    "org.lockss.title.title1.param.2.key=year\n" +
    "org.lockss.title.title1.param.2.value=1999\n" +
    "org.lockss.title.title1.attributes.publisher=The Smithsonian Institution";


  // valid tdb entry
  private static final String tdbXml1 =
    "<lockss-config>\n" +
    "  <property name=\"org.lockss\">\n" +
    "    <property name=\"prop\" value=\"value\" />\n" +
    "    <property name=\"title\">\n" +
    "      <property name=\"title1\">\n" +
    "        <property name=\"title\" value=\"Air &amp; Space volume 3\" />\n" +
    "        <property name=\"plugin\" value=\"org.lockss.testplugin1\" />\n" +
    "        <property name=\"pluginVersion\" value=\"4\" />\n" +
    "        <property name=\"issn\" value=\"0003-0031\" />\n" +
    "        <property name=\"journal\">\n" +
    "          <property name=\"link.1.type\" value=\"" + TdbTitle.LinkType.continuedBy.toString() + "\" />\n" +
    "          <property name=\"link.1.journalId\" value=\"0003-0031\" />\n" +  // link to self
    "        </property>\n" +
    "        <property name=\"param.1.key\" value=\"volume\" />\n" +
    "        <property name=\"param.1.value\" value=\"3\" />\n" +
    "        <property name=\"param.2.key\" value=\"year\" />\n" +
    "        <property name=\"param.2.value\" value=\"1999\" />\n" +
    "        <property name=\"attributes.publisher\" value=\"The Smithsonian Institution\" />\n" +
    "      </property>\n" +
    "    </property>\n" +
    "  </property>\n" +
  "</lockss-config>";
  
  // invalid tdb entry: missing "plugin"
  private static final String tdbXml2 =
    "<lockss-config>\n" +
    "  <property name=\"org.lockss\">\n" +
    "    <property name=\"prop\" value=\"value\" />\n" +
    "    <property name=\"title\">\n" +
    "      <property name=\"title1\">\n" +
    "        <property name=\"title\" value=\"Air &amp; Space volume 3\" />\n" +
//    "        <property name=\"plugin\" value=\"org.lockss.testplugin1\" />\n" +
    "        <property name=\"pluginVersion\" value=\"4\" />\n" +
    "        <property name=\"issn\" value=\"0003-0031\" />\n" +
    "        <property name=\"journal\">\n" +
    "          <property name=\"link.1.type\" value=\"" + TdbTitle.LinkType.continuedBy.toString() + "\" />\n" +
    "          <property name=\"link.1.journalId\" value=\"0003-0031\" />\n" +  // link to self
    "        </property>\n" +
    "        <property name=\"param.1.key\" value=\"volume\" />\n" +
    "        <property name=\"param.1.value\" value=\"3\" />\n" +
    "        <property name=\"param.2.key\" value=\"year\" />\n" +
    "        <property name=\"param.2.value\" value=\"1999\" />\n" +
    "        <property name=\"attributes.publisher\" value=\"The Smithsonian Institution\" />\n" +
    "      </property>\n" +
    "    </property>\n" +
    "  </property>\n" +
  "</lockss-config>";
  
/*
 */
  private static final String xml1 =
    "<lockss-config>\n" +
    "  <property name=\"prop.7\" value=\"foo\" />\n" +
    "  <property name=\"prop.8\" value=\"bar\" />\n" +
    "  <property name=\"prop.9\" value=\"baz\" />\n" +
    "</lockss-config>";

  private static final String badConfig =
    "<lockss-config>\n" +
    "  <property name=\"prop.10\" value=\"foo\" />\n" +
    "  <property name=\"prop.11\">\n" +
    "    <value>bar</value>\n" +
    "  <!-- missing closing property tag -->\n" +
    "</lockss-config>";

  TestConfigFile(String name) {
    super(name);
  }

  /** subclass must implement to create a ConfigFile instance of the
   * appropriate type, with the specified content
   */
  abstract protected ConfigFile makeConfigFile(String contents,
					       boolean isXml)
      throws IOException;

  /** subclass must implement to update the last modification time of the
   * underlying file/URL
   */
  abstract protected void updateLastModified(ConfigFile cf, long time)
      throws IOException;

  /** subclass must implement to say whether each call to
   * getConfiguration() is expected to check whether the file has been
   * modified.  If false, should only happen if setNeedsReload() has been
   * called.
   */
  abstract protected boolean isAlwaysAttempt();

  static String dateString(long time) {
    Date date = new Date(time);
    return DateTimeUtil.GMT_DATE_FORMATTER.format(date);
  }

  protected String suff(boolean isXml) {
    return isXml ? ".xml" : ".txt";
  }

  // Parameterized tests - invoked either from tests in this class or
  // subclasses

  /** Load file, check status, load again, check no change, force change,
   * load again, check reloaded
   */
  public Configuration testLoad(String content, boolean xml)
      throws IOException {
    ConfigFile cf = makeConfigFile(content, xml);
    assertFalse(cf.isLoaded());
    assertEquals(null, cf.getLastModified());
    assertEquals("Not yet loaded", cf.getLoadErrorMessage());
    long lastAttempt;
    long prevAttempt = cf.getLastAttemptTime();

    Configuration config = cf.getConfiguration();
    assertTrue(cf.isLoaded());
    String last = cf.getLastModified();
    assertNotNull("last modified shouldn't be null", last);
    assertNotEquals(prevAttempt, lastAttempt = cf.getLastAttemptTime());
    prevAttempt = lastAttempt;

    TimerUtil.guaranteedSleep(1);
    assertSame(config, cf.getConfiguration());
    assertEquals(last, cf.getLastModified());
    if (isAlwaysAttempt()) {
      assertNotEquals(prevAttempt, lastAttempt = cf.getLastAttemptTime());
      prevAttempt = lastAttempt;
    } else {
      assertEquals(prevAttempt, lastAttempt = cf.getLastAttemptTime());
    }
    TimerUtil.guaranteedSleep(1);
    cf.setNeedsReload();
    assertSame(config, cf.getConfiguration());
    assertEquals(last, cf.getLastModified());
    assertNotEquals(prevAttempt, lastAttempt = cf.getLastAttemptTime());
    prevAttempt = lastAttempt;

    updateLastModified(cf, TimeBase.nowMs() + Constants.SECOND);
    cf.setNeedsReload();
    assertEqualsNotSame(config, cf.getConfiguration());
    assertNotEquals(last, cf.getLastModified());
    return cf.getConfiguration();
  }

  /** Test that reading the ConfigFile causes the appropriate error
   */
  public void testCantRead(ConfigFile cf, String re) throws IOException {
    assertFalse(cf.isLoaded());
    try {
      Configuration config = cf.getConfiguration();
      fail("Shouldn't have created config: " + config);
    } catch (IOException e) {
    }
    if (re != null) {
      assertMatchesRE(re, cf.getLoadErrorMessage());
    }
  }

  /** Test that reading the specified content from a ConfigFile causes the
   * appropriate error
   */
  public void testIllContent(String content, boolean xml, String re)
      throws IOException {
    testCantRead(makeConfigFile(content, xml), re);
  }

  public void testKeyPredicate() throws IOException {
    KeyPredicate keyPred = new KeyPredicate() {
	public boolean evaluate(Object obj) {
 	  return ((String)obj).indexOf("bad") < 0;
	}
	public boolean failOnIllegalKey() {
	  return false;
	}};

    String str = "foo.bar=one\nfoo.bad=two\nfoo.foo=four\n";

    ConfigFile cf = makeConfigFile(str, false);
    cf.setKeyPredicate(keyPred);
    assertFalse(cf.isLoaded());

    Configuration config = cf.getConfiguration();
    assertEquals("one", config.get("foo.bar"));
    assertEquals("four", config.get("foo.foo"));
    assertNull(config.get("foo.bad"));
    assertFalse(config.containsKey("foo.bad"));
  }

  // Matching key should throw IOException
  public void testKeyPredicateThrow() throws IOException {
    KeyPredicate keyPred = new KeyPredicate() {
	public boolean evaluate(Object obj) {
 	  return ((String)obj).indexOf("bad") < 0;
	}
	public boolean failOnIllegalKey() {
	  return true;
	}};

    String str = "foo.bar=one\nfoo.bad=two\nfoo.foo=four\n";

    ConfigFile cf = makeConfigFile(str, false);
    cf.setKeyPredicate(keyPred);
    assertFalse(cf.isLoaded());
    try {
      Configuration config = cf.getConfiguration();
      fail("Loading config file with illegal key should throw");
    } catch (IOException e) {
    }

    // this one doesn't match, shouldn't throw
    String str2 = "foo.bar=one\nfoo.dab=two\nfoo.foo=four\n";

    ConfigFile cf2 = makeConfigFile(str2, false);
    cf2.setKeyPredicate(keyPred);
    assertFalse(cf2.isLoaded());

    Configuration config2 = cf2.getConfiguration();
    assertEquals("one", config2.get("foo.bar"));
    assertEquals("four", config2.get("foo.foo"));
    assertEquals("two", config2.get("foo.dab"));
  }

  // Test cases.  These will be run once for each ConfigFile variant

  /** Load a props file */
  public void testLoadText() throws IOException {
    Configuration config = testLoad(text1, false);
    assertEquals("foo", config.get("prop.1"));
    assertEquals("bar", config.get("prop.2"));
    assertEquals("baz", config.get("prop.3"));
  }

  /** Load valid Tdb property file with a single entry */
  public void testValidLoadTdbText() throws IOException {
    Configuration config = testLoad(tdbText1, false);
    assertEquals("value", config.get("org.lockss.prop"));
    
    Tdb tdb = config.getTdb();
    assertNotNull(tdb);
    assertEquals(1, tdb.getTdbAuCount());
  }

  /** Load invalid Tdb property file with a single entry */
  public void testInvalidLoadTdbText() throws IOException {
    Configuration config = testLoad(tdbText2, false);
    assertEquals("value", config.get("org.lockss.prop"));
    
    Tdb tdb = config.getTdb();
    assertNull(tdb);
  }

  /** Load an XML file */
  public void testLoadXml() throws IOException {
    Configuration config = testLoad(xml1, true);
    assertEquals("foo", config.get("prop.7"));
    assertEquals("bar", config.get("prop.8"));
    assertEquals("baz", config.get("prop.9"));
  }

  /** Try to load a bogus XML file */
  public void testIllXml() throws IOException {
    testIllContent(badConfig, true, "SAXParseException");
  }

  /** Load valid Tdb XML file with a single entry */
  public void testValidLoadTdbXml() throws IOException {
    Configuration config = testLoad(tdbXml1, true);
    assertEquals("value", config.get("org.lockss.prop"));
    
    Tdb tdb = config.getTdb();
    assertNotNull(tdb);
    assertEquals(1, tdb.getTdbAuCount());
  }

  /** Load invalid Tdb XML file with a single entry */
  public void testInvalidLoadTdbXml() throws IOException {
    Configuration config = testLoad(tdbXml2, true);
    assertEquals("value", config.get("org.lockss.prop"));
    
    Tdb tdb = config.getTdb();
    assertNull(tdb);
  }

  public void testGeneration() throws IOException {
    ConfigFile cf = makeConfigFile("aa=54", false);

    assertFalse(cf.isLoaded());
    ConfigFile.Generation gen = cf.getGeneration();
    assertTrue(cf.isLoaded());
    assertEquals(cf.getConfiguration(), gen.getConfig());

    TimerUtil.guaranteedSleep(1);
    assertEquals(gen.getGeneration(), cf.getGeneration().getGeneration());
    assertEquals(gen.getUrl(), cf.getGeneration().getUrl());
    assertEquals(gen.getConfig(), cf.getGeneration().getConfig());

    TimerUtil.guaranteedSleep(1);
    cf.setNeedsReload();
    assertEquals(cf.getConfiguration(), gen.getConfig());

    updateLastModified(cf, TimeBase.nowMs() + Constants.SECOND);
    cf.setNeedsReload();
    ConfigFile.Generation gen2 = cf.getGeneration();
    assertEquals(gen.getGeneration() + 1, gen2.getGeneration());
    assertEqualsNotSame(gen.getConfig(), gen2.getConfig());
  }

  /** Test FileConfigFile */
  public static class TestFile extends TestConfigFile {
    public TestFile(String name) {
      super(name);
    }

    protected ConfigFile makeConfigFile(String contents, boolean isXml)
	throws IOException {
      return new FileConfigFile(FileTestUtil.urlOfString(contents,
							 suff(isXml)));
    }

    protected void updateLastModified(ConfigFile cf, long time)
	throws IOException {
      FileConfigFile fcf = (FileConfigFile)cf;
      File file = fcf.makeFile();
      file.setLastModified(time);
    }

    protected boolean isAlwaysAttempt() {
      return true;
    }

    // Test cases

    public void testNotFound() throws IOException {
      testCantRead(new FileConfigFile("/file/not/found"),
		   "FileNotFoundException");
    }

    // Ensure storedConfig() of a sealed config doesn't make a copy
    public void testStoredConfigSealed() throws IOException {
      FileConfigFile fcf = (FileConfigFile)makeConfigFile("a=1\nb1=a", false);
      Configuration c = fcf.getConfiguration();
      Configuration c2 = ConfigurationUtil.fromArgs("x", "y");
      assertSame(c, fcf.getConfiguration());
      assertNotSame(c2, fcf.getConfiguration());
      c2.seal();
      fcf.storedConfig(c2);
      assertSame(c2, fcf.getConfiguration());
    }

    // Ensure storedConfig() of an unsealed config does make a copy
    public void testStoredConfigUnsealed() throws IOException {
      FileConfigFile fcf = (FileConfigFile)makeConfigFile("a=1\nb1=a", false);
      Configuration c = fcf.getConfiguration();
      Configuration c2 = ConfigurationUtil.fromArgs("x", "y");
      assertSame(c, fcf.getConfiguration());
      assertNotSame(c2, fcf.getConfiguration());
      fcf.storedConfig(c2);
      assertEqualsNotSame(c2, fcf.getConfiguration());
    }
  }

  /** Test JarConfigFile */
  public static class TestJar extends TestConfigFile {
    public TestJar(String name) {
      super(name);
    }

    protected ConfigFile makeConfigFile(String contents, boolean isXml)
	throws IOException {
      String jarName = getTempDir().getAbsolutePath() +
	File.separator + "test.jar";
      String entryName = "testent." + suff(isXml);
      // Create a jar file with a single resource (a text file)
      Map entries = new HashMap();
      entries.put(entryName, contents);
      JarTestUtils.createStringJar(jarName, entries);
      String url = UrlUtil.makeJarFileUrl(jarName, entryName);
      return new JarConfigFile(url);
    }

    protected void updateLastModified(ConfigFile cf, long time)
	throws IOException {
      JarConfigFile jcf = (JarConfigFile)cf;
      File file = jcf.getFile();
      file.setLastModified(time);
    }

    protected boolean isAlwaysAttempt() {
      return true;
    }

    // Test cases

    public void testNotFound() throws IOException {
      JarConfigFile jcf;

      jcf = new JarConfigFile("jar:file:///file/not/found!/who.cares");
      testCantRead(jcf, "(ZipException|FileNotFoundException)");

      String jarName = getTempDir().getAbsolutePath() +
	File.separator + "test.jar";
      Map entries = new HashMap();
      entries.put("foo.bar", "bletch");
      JarTestUtils.createStringJar(jarName, entries);
      String url = UrlUtil.makeJarFileUrl(jarName, "no.such.entry");
      jcf = new JarConfigFile(url);
      testCantRead(jcf, "FileNotFoundException");
    }
  }

  /** Test HTTPConfigFile */
  public static class TestHttp extends TestConfigFile {
    public TestHttp(String name) {
      super(name);
    }

    protected ConfigFile makeConfigFile(String contents, boolean isXml)
	throws IOException {
      return new MyHttpConfigFile("http://foo.bar/lockss" + suff(isXml),
				  contents);
    }

    protected void updateLastModified(ConfigFile cf, long time)
	throws IOException {
      MyHttpConfigFile hcf = (MyHttpConfigFile)cf;
      hcf.setLastModified(time);
    }

    protected boolean isAlwaysAttempt() {
      return false;
    }

    // Test cases

    public void testNotFound() throws IOException {
      String url;
      MyHttpConfigFile hcf;

      hcf = new MyHttpConfigFile("http://a.b/not/found");
      hcf.setResponseCode(404);
      testCantRead(hcf, "FileNotFoundException");

      url = "http://a.b:80:81/malformed.url";
      hcf = new MyHttpConfigFile(url);
      hcf.setExecuteException(new MalformedURLException(url));
      testCantRead(hcf, "MalformedURLException");
    }

    public void testForbidden() throws IOException {
      String url;
      MyHttpConfigFile hcf;

      url = "http://a.b/forbidden";
      hcf = new MyHttpConfigFile(url,
				 "Error page with no hint, shouldn't " +
				 "end up in config file error msg");
      hcf.setResponseCode(403);
      hcf.setResponseMessage("Forbidden");
      testCantRead(hcf, "403: Forbidden$");

      hcf = new MyHttpConfigFile(url,
				 "foobar \n" +
				 "locksshint: this is a hint endhint");
      hcf.setResponseCode(403);
      hcf.setResponseMessage("Forbidden");
      testCantRead(hcf, "403: Forbidden\nthis is a hint$");
    }

    public void testXLockssInfo() throws IOException {
      InputStream in = new StringInputStream(xml1);
      MyHttpConfigFile hcf =
	new MyHttpConfigFile("http://foo.bar/lockss.xml", in);
      hcf.setProperty("X-Lockss-Info", "daemon=1.3.1");
      Configuration config = hcf.getConfiguration();
      assertTrue(hcf.isLoaded());
      assertEquals("daemon=1.3.1", hcf.connReqHdrs.get("X-Lockss-Info"));
      hcf.connReqHdrs.clear();
      hcf.setProperty("X-Lockss-Info", null);
      hcf.getConfiguration();
      assertTrue(hcf.isLoaded());
      assertEquals(null, hcf.connReqHdrs.get("X-Lockss-Info"));
      assertEquals(null, hcf.proxyHost);
    }

    public void testGzip() throws IOException {
      InputStream zin = new GZIPpedInputStream(xml1);
      MyHttpConfigFile hcf =
	new MyHttpConfigFile("http://foo.bar/lockss.xml", zin);
      hcf.setContentEncoding("gzip");
      Configuration config = hcf.getConfiguration();
      assertTrue(hcf.isLoaded());
    }

    // Ensure null message in exception doesn't cause problems
    // Not specific to HTTPConfigFile, but handy to test here because we can
    // make the subclass throw
    public void testNullExceptionMessage() throws IOException {
      MyHttpConfigFile hcf =
	new MyHttpConfigFile("http://foo.bar/lockss.xml", "");
      hcf.setExecuteException(new IOException((String)null));
      try {
	Configuration config = hcf.getConfiguration();
	fail("Should throw");
      } catch (NullPointerException e) {
	fail("Null exception message caused", e);
      } catch (IOException e) {
      }
    }

    public void testSetConnectionPool1() throws IOException {
      MyHttpConfigFile hcf =
	new MyHttpConfigFile("http://foo.bar/lockss.xml");
      LockssUrlConnectionPool pool = new LockssUrlConnectionPool();
      hcf.setConnectionPool(pool);
      assertSame(pool, hcf.getConnectionPool());
    }

    public void testSetConnectionPool2() throws IOException {
      MyHttpConfigFile hcf =
	new MyHttpConfigFile("http://foo.bar/lockss.xml");
      LockssUrlConnectionPool pool = hcf.getConnectionPool();
      assertNotNull(pool);
      assertSame(pool, hcf.getConnectionPool());
    }

    public void testProxy() throws IOException {
      String phost = "phost.foo";
      int pport = 1234;
      ConfigurationUtil.addFromArgs(ConfigManager.PARAM_PROPS_PROXY,
				    phost + ":" + pport);
      InputStream in = new StringInputStream(xml1);
      MyHttpConfigFile hcf =
	new MyHttpConfigFile("http://foo.bar/lockss.xml", in);
      Configuration config = hcf.getConfiguration();
      assertEquals(phost, hcf.proxyHost);
      assertEquals(pport, hcf.proxyPort);
    }

  }

  /** HTTPConfigFile that uses a programmable MockLockssUrlConnection */
  static class MyHttpConfigFile extends HTTPConfigFile {
    Map map = new HashMap();
    String lastModified;
    String contentEncoding = null;
    int resp = 200;
    String respMsg = null;
    IOException executeExecption;
    Properties connReqHdrs = new Properties();
    String proxyHost = null;
    int proxyPort = -1;

    public MyHttpConfigFile(String url) {
      this(url, "");
    }

    public MyHttpConfigFile(String url, String content) {
      super(url);
      map.put(url, content);
      lastModified = dateString(TimeBase.nowMs());
    }

    public MyHttpConfigFile(String url, InputStream content) {
      super(url);
      map.put(url, content);
      lastModified = dateString(TimeBase.nowMs());
    }

    protected LockssUrlConnection openUrlConnection(String url)
	throws IOException {
      MyMockLockssUrlConnection conn = new MyMockLockssUrlConnection();
      conn.setURL(url);
      return conn;
    }

    // Setters to control MyMockLockssUrlConnection

    void setLastModified(long time) {
      lastModified = dateString(time);
    }

    void setResponseCode(int code) {
      resp = code;
    }

    void setResponseMessage(String msg) {
      respMsg = msg;
    }

    void setExecuteException(IOException e) {
      executeExecption = e;
    }

    void setContentEncoding(String encoding) {
      contentEncoding = encoding;
    }

    class MyMockLockssUrlConnection extends MockLockssUrlConnection {

      public MyMockLockssUrlConnection() throws IOException {
	super();
      }

      public MyMockLockssUrlConnection(String url) throws IOException {
	super(url);
      }

      public void execute() throws IOException {
	super.execute();
	String url = getURL();

	Object o = map.get(url);
	if (o == null) {
	  this.setResponseCode(404);
	} else {
	  if (executeExecption != null) {
	    throw executeExecption;
	  }
	  String ifSince = getRequestProperty("if-modified-since");
	  if (ifSince != null && ifSince.equalsIgnoreCase(lastModified)) {
	    this.setResponseCode(HttpURLConnection.HTTP_NOT_MODIFIED);
	  } else {
	    if (o instanceof String) {
	      this.setResponseInputStream(new StringInputStream((String)o));
	    } else if (o instanceof InputStream) {
	      this.setResponseInputStream((InputStream)o);
	    } else {
	      throw new UnsupportedOperationException("Unknown result stream type " + o.getClass());
	    }
	    this.setResponseHeader("last-modified", lastModified);
	    if (contentEncoding != null) {
	      this.setResponseHeader("Content-Encoding", contentEncoding);
	    }
	    this.setResponseCode(resp);
	    this.setResponseMessage(respMsg);
	  }
	}
      }

      public void setRequestProperty(String key, String value) {
	super.setRequestProperty(key, value);
	connReqHdrs.setProperty(key, value);
      }

      public String getResponseContentEncoding() {
	return contentEncoding;
      }
      public long getResponseContentLength() {
	String url = getURL();

	Object o = map.get(url);
	if (o != null &&o instanceof String) {
	  return ((String)o).length();
	}
	return 0;
      }

      public boolean canProxy() {
	return true;
      }

      @Override
      public void setProxy(String host, int port) {
	MyHttpConfigFile.this.proxyHost = host;
	MyHttpConfigFile.this.proxyPort = port;
      }
    }
  }

  public static Test suite() {
    return variantSuites(new Class[] {TestFile.class,
				      TestHttp.class,
				      TestJar.class});
  }
}
